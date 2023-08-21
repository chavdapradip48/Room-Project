package com.pradip.roommanagementsystem.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pradip.roommanagementsystem.dto.*;
import com.pradip.roommanagementsystem.dto.projection.UserCalculator;
import com.pradip.roommanagementsystem.entity.ExpenseCalculator;
import com.pradip.roommanagementsystem.exception.InvalidInputException;
import com.pradip.roommanagementsystem.repository.ExpenseCalculatorRepository;
import com.pradip.roommanagementsystem.repository.ExpenseRepository;
import com.pradip.roommanagementsystem.util.GeneralUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExpenseCalculatorService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseCalculatorRepository expenseCalculatorRepository;

    @Autowired
    private UserService userService;
    
    @Autowired
    private GeneralUtil generalUtil;

    public Object calculateExpensesAndStore(ExpenseCalculatorRequestDTO expenseCalculatorRequestDTO, boolean isStore, boolean isAdmin) {
        ExpenseCalculatorResponseDTO expenseCalculatorResponseDTO = calculateMothlyExpenses(expenseCalculatorRequestDTO);

        if (isStore && isAdmin) {
            ExpenseCalculator expenseCalculator = addExpenseCalculatorData(expenseCalculatorRequestDTO);
            expenseCalculatorResponseDTO.setId(expenseCalculator.getId());
        }

        return expenseCalculatorResponseDTO;
    }


    public ExpenseCalculatorResponseDTO calculateMothlyExpenses(ExpenseCalculatorRequestDTO expCalcDTO) {

        if (expCalcDTO.getHalfPersons().isEmpty() && expCalcDTO.getFullPersons().isEmpty() && expCalcDTO.getOnVacationPersons().isEmpty())
            throw new InvalidInputException("!! Persons are not selected");

        Date calculationFrom = expCalcDTO.getFrom(),calculationTo = expCalcDTO.getTo();

        if (calculationFrom.compareTo(calculationTo) > 0)
            throw new InvalidInputException("Start Date must be less than end date.");

        // Calculate all the expenses
        long totalAmount = 0L;
        Long totalAmountDB = expenseRepository.sumByAmountFromToAndPaymentMode(calculationFrom, calculationTo, Arrays.asList(PaymentMode.PERSONAL));
        if(totalAmountDB != null) totalAmount = totalAmountDB;
        int totalFixedMonthlyExpenses = expCalcDTO.getFixedMonthlyExpenses().values().stream().mapToInt(i -> i).sum();
        int totalVariableMonthlyExpenses = expCalcDTO.getVariableMonthlyExpenses().values().stream().mapToInt(i -> i).sum();;

        // Fetch persons
        List<Integer> fullPersons = expCalcDTO.getFullPersons();
        List<Integer> halfPersons = expCalcDTO.getHalfPersons();
        List<Integer> onVacationPersons = expCalcDTO.getOnVacationPersons();

        int fullHalfSize = fullPersons.size() + halfPersons.size();
        int totalCalculatedFixedPerHead = totalFixedMonthlyExpenses / (fullHalfSize + onVacationPersons.size());
        long variableTotaPerHeadl = (((totalVariableMonthlyExpenses +totalAmount) / (fullHalfSize)) + 900);

        List<ExpenseCalculatorPersons> expenseCalculatorPersonList = new ArrayList<>();

        if (!fullPersons.isEmpty())
            for (Integer person : fullPersons)
                expenseCalculatorPersonList.add(getExpenseCalculatorPersons("Full", person, (int) (totalCalculatedFixedPerHead + variableTotaPerHeadl), calculationFrom, calculationTo));

        if (!halfPersons.isEmpty())
            for (Integer person : halfPersons)
                expenseCalculatorPersonList.add(getExpenseCalculatorPersons("Half", person, (int) (totalCalculatedFixedPerHead + (variableTotaPerHeadl) / 2), calculationFrom, calculationTo));

        if (!onVacationPersons.isEmpty())
            for (Integer person : onVacationPersons)
                expenseCalculatorPersonList.add(getExpenseCalculatorPersons("OnVacation", person, totalCalculatedFixedPerHead, calculationFrom,calculationTo));

        return new ExpenseCalculatorResponseDTO(expCalcDTO.getId(), expenseCalculatorPersonList,(int) (totalAmount + totalVariableMonthlyExpenses + totalFixedMonthlyExpenses), calculationFrom, calculationTo);
    }

    private ExpenseCalculatorPersons getExpenseCalculatorPersons(String personType, int person, int totalCalculatedAmt, Date from, Date to) {
        UserCalculator userCalculator = (UserCalculator) userService.getUserById((long) person, "UserCalculator");

        Long myExp =expenseRepository.sumByAmountFromToAndPaymentModeMy(from, to, Arrays.asList(PaymentMode.PERSONAL),userCalculator.getId());

        ExpenseCalculatorPersons expenseCalculatorPersons = new ExpenseCalculatorPersons();
        expenseCalculatorPersons.setPerson(userCalculator);
        expenseCalculatorPersons.setTotalCalcualtedMonthlyAmount(totalCalculatedAmt);
        expenseCalculatorPersons.setPersonType(personType);

        if (myExp == null) myExp = 0L;

        int myExpInt= Math.toIntExact(myExp);
        expenseCalculatorPersons.setTotalMyMonthlyAmount(myExpInt);
        expenseCalculatorPersons.setTotalPayableAmount(totalCalculatedAmt - myExpInt);

        return expenseCalculatorPersons;
    }

    private ExpenseCalculator addExpenseCalculatorData(ExpenseCalculatorRequestDTO dto) {
        ExpenseCalculator expenseCalculator = new ExpenseCalculator();

        expenseCalculator.setFixedMonthlyExpenses(generalUtil.parseObjectToJson(dto.getFixedMonthlyExpenses()));
        expenseCalculator.setVariableMonthlyExpenses(generalUtil.parseObjectToJson(dto.getVariableMonthlyExpenses()));
        expenseCalculator.setFullPersons(generalUtil.parseObjectToJson(dto.getFullPersons()));
        expenseCalculator.setHalfPersons(generalUtil.parseObjectToJson(dto.getHalfPersons()));
        expenseCalculator.setOnVacationPersons(generalUtil.parseObjectToJson(dto.getOnVacationPersons()));
        expenseCalculator.setDurationFrom(dto.getFrom());
        expenseCalculator.setDurationTo(dto.getTo());

        return expenseCalculatorRepository.save(expenseCalculator);
    }

    public List<Object> getAllExpenseCalculatorData(String fetchType) {
        List<ExpenseCalculator> calculatedData = expenseCalculatorRepository.findAll(Sort.by("createdAt").descending());

        if(calculatedData.isEmpty())
            throw  new EntityNotFoundException("Calculated expenses data is not found.");

        return calculatedData.stream().map(expenseCalculator -> {
            ExpenseCalculatorResponseDTO dto = getExpenseCalculatorEntityToDTO(expenseCalculator);
            if (fetchType.equals("Full"))
                return dto;
            return new ExpenseCalculatorListingDTO(dto.getId(),dto.getPersons().size(), dto.getTotalAmount(),dto.getFrom(),dto.getTo());
        }).collect(Collectors.toList());
    }

    public ExpenseCalculatorResponseDTO getExpenseCalculatorById(long expenseCalculatorId) {
        Optional<ExpenseCalculator> expenseCalculator = expenseCalculatorRepository.findById(expenseCalculatorId);

        if(expenseCalculator.isEmpty())
            throw  new EntityNotFoundException("Calculated expenses data is not found.");

        return getExpenseCalculatorEntityToDTO(expenseCalculator.get());
    }

    public Object deleteExpenseCalculatorById(long expenseCalculatorId) {

        if(!expenseCalculatorRepository.existsById(expenseCalculatorId))
            throw  new EntityNotFoundException("Calculated expenses data is not found.");

        expenseCalculatorRepository.deleteById(expenseCalculatorId);
        return true;
    }

    private ExpenseCalculatorResponseDTO getExpenseCalculatorEntityToDTO(ExpenseCalculator expenseCalculator) {
        TypeReference<List<Integer>> listTypeReference = new TypeReference<List<Integer>>() {};
        ExpenseCalculatorRequestDTO requestDTO = new ExpenseCalculatorRequestDTO();

        requestDTO.setFixedMonthlyExpenses(mapOperations(expenseCalculator.getFixedMonthlyExpenses()));
        requestDTO.setVariableMonthlyExpenses(mapOperations(expenseCalculator.getVariableMonthlyExpenses()));
        requestDTO.setFullPersons(generalUtil.parseJsonToObject(expenseCalculator.getFullPersons(), listTypeReference));
        requestDTO.setHalfPersons(generalUtil.parseJsonToObject(expenseCalculator.getHalfPersons(), listTypeReference));
        requestDTO.setOnVacationPersons(generalUtil.parseJsonToObject(expenseCalculator.getOnVacationPersons(), listTypeReference));
        requestDTO.setFrom(expenseCalculator.getDurationFrom());
        requestDTO.setTo(expenseCalculator.getDurationTo());
        requestDTO.setId(expenseCalculator.getId());

        return calculateMothlyExpenses(requestDTO);
    }

    private Map<String, Integer> mapOperations(String monthlyExpenses) {
        return generalUtil.parseJsonToObject(monthlyExpenses, new TypeReference<Map<String, String>>() {}).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> Integer.parseInt(entry.getValue())));
    }
}
