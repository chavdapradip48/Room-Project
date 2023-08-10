package com.pradip.roommanagementsystem.service;

import com.pradip.roommanagementsystem.dto.*;
import com.pradip.roommanagementsystem.dto.projection.ExpenseProjection;
import com.pradip.roommanagementsystem.dto.projection.UserCalculator;
import com.pradip.roommanagementsystem.entity.Expense;
import com.pradip.roommanagementsystem.entity.User;
import com.pradip.roommanagementsystem.exception.InvalidInputException;
import com.pradip.roommanagementsystem.exception.ResourceNotFoundException;
import com.pradip.roommanagementsystem.repository.ExpenseRepository;
import com.pradip.roommanagementsystem.repository.UserRepository;
import com.pradip.roommanagementsystem.security.util.JwtUtils;
import com.pradip.roommanagementsystem.util.GeneralUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

@Service
@Slf4j
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    
    @Autowired
    private GeneralUtil generalUtil;

    @Autowired
    private JwtUtils jwtUtils;

    public List<ExpenseProjection> getAllExpenses() {
            List<ExpenseProjection> allBy = expenseRepository.findAllByOrderByCreatedAtDesc(ExpenseProjection.class);
//        List<ExpenseProjection> allBy = expenseRepository.findAllBy(ExpenseProjection.class);
        if (allBy.isEmpty())
            throw new ResourceNotFoundException("Expense not available.");

        return allBy;
    }


    public Expense  addExpense(ExpenseDTO expenseDTO) {
        if (!userRepository.existsById(expenseDTO.getUser().getId()))
            throw new ResourceNotFoundException("User is not exist.");

        Expense expense = generalUtil.convertObject(expenseDTO, Expense.class);
        User user = new User();
        user.setId(expenseDTO.getUser().getId());
        expense.setUser(user);
        return expenseRepository.save(expense);
    }

    public ExpenseProjection getExpenseByExpenseId(Long id) {
        Optional<ExpenseProjection> byId = expenseRepository.findById(id, ExpenseProjection.class);
        if (byId.isEmpty())
            throw new ResourceNotFoundException("Expense not available with specified id.");
        return  byId.get();
    }

    public List<ExpenseProjection> getExpenseByUserId(Long userId) {
        if (!expenseRepository.existsByUserId(userId))
            throw new ResourceNotFoundException("User not have any expenses or might user not exist.");

//        List<ExpenseProjection> byUserId = expenseRepository.findByUserId(userId);
        List<ExpenseProjection> byUserId = expenseRepository.findByUserIdOrderByCreatedAtDesc(userId);

        if (byUserId.isEmpty())
            throw new ResourceNotFoundException("Expense not available with specified user id.");

        return  byUserId;
    }

    public String deleteExpenseByUserId(Long userId) {
        if (!expenseRepository.existsByUserId(userId))
            throw new ResourceNotFoundException("User not exist.");
        expenseRepository.deleteByUserId(userId);
        return "Expenses deleted successfully of user";
    }

    public String deleteExpenseById(Long expenseId) {
        if(!expenseRepository.existsById(expenseId))
            throw new ResourceNotFoundException("User not have any expenses or might user not exist.");
        expenseRepository.deleteById(expenseId);
        return "Expense deleted successfully";
    }

    public Object getDashboardData(String token) {
        int year= LocalDate.now().getYear(), month = new Date().getMonth() + 1;
        DashboardDTO dashboardDTO=new DashboardDTO();
        List<Object[]> monthlyExpenseDataByYear = expenseRepository.findMonthlyExpenseDataByYear(year);
        Map<String, Long> monthlyExpenseData = new TreeMap<>(Comparator.comparing(Month::valueOf));

        if(monthlyExpenseDataByYear != null && monthlyExpenseDataByYear.isEmpty())
            throw new ResourceNotFoundException("Expenses are not available with "+year+" year.");

        for (Object[] result : monthlyExpenseDataByYear)
            monthlyExpenseData.put(getMonthName((Integer) result[0]).toUpperCase(), (Long) result[1]);

        Long tempCcurrentMonthExp = expenseRepository.sumAmountByYearAndMonth(year, month);
        Long tempTotalExp = expenseRepository.sumAmountByYear(year);
        Long tempMyTotalExp = expenseRepository.sumAmountByUserEmailAndMonth(jwtUtils.getUserNameFromJwtToken(token), year, month);
        Long tempPreviousMonthExp = expenseRepository.sumAmountByYearAndMonth(year, month - 1);

        if (tempTotalExp != null) dashboardDTO.setTotalAmount(tempTotalExp);
        if (tempMyTotalExp != null) dashboardDTO.setMyTotalAmount(tempMyTotalExp);
        if (tempCcurrentMonthExp != null) dashboardDTO.setCurrentMonthAmount(tempCcurrentMonthExp);
        if (tempPreviousMonthExp != null) dashboardDTO.setPreviousMonthAmount(tempPreviousMonthExp);

        dashboardDTO.setGraphData(monthlyExpenseData);
        dashboardDTO.setCurrentPreviousMonthPercent(calculatePercentageChange(tempPreviousMonthExp,tempCcurrentMonthExp));

        return  dashboardDTO;
    }

    private String getMonthName(Integer monthNumber) {
        return Month.of(monthNumber).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    public float calculatePercentageChange(Long previousExp, Long currentExp) {
        if (currentExp == null) currentExp=0L;
        if (previousExp == null) previousExp=0L;
        if (previousExp.equals(0L)) return 100.0F;

        return (float) (((double)(currentExp - previousExp)) / previousExp * 100);
    }

    public ExpenseCountResponseDTO countExpenses(ExpenseCountRequestDTO expenseCountRequestDTO) {

        if (expenseCountRequestDTO.getFrom().compareTo(expenseCountRequestDTO.getTo()) > 0){
            throw new InvalidInputException("Start Date must be less than end date.");
        }

        Long totalAmount = 0L;

        Long totalAmountDB = expenseRepository.sumByAmountFromToAndPaymentMode(expenseCountRequestDTO.getFrom(), expenseCountRequestDTO.getTo(), Arrays.asList(PaymentMode.PERSONAL));

        if(totalAmountDB != null)
            totalAmount = totalAmountDB;

        totalAmount += expenseCountRequestDTO.getExtraExpenses().values().stream().mapToInt(i -> i).sum();

        ExpenseCountResponseDTO expenseCountResponseDTO = new ExpenseCountResponseDTO();
        expenseCountResponseDTO.setTotalAmount(totalAmount);
        expenseCountResponseDTO.setPerHeadAmount(totalAmount / expenseCountRequestDTO.getPersons());
        expenseCountResponseDTO.setPersons(expenseCountRequestDTO.getPersons());

        return expenseCountResponseDTO;
    }

    public Object calculateMothlyExpenses(ExpenseCalculatorRequestDTO expenseCalculatorRequestDTO) {

        Date calculationFrom = expenseCalculatorRequestDTO.getFrom();
        Date calculationTo = expenseCalculatorRequestDTO.getTo();

        if (calculationFrom.compareTo(calculationTo) > 0){
            throw new InvalidInputException("Start Date must be less than end date.");
        }

        // Calculate all the expenses
        long totalAmount = 0L;
        long totalAmountDB = expenseRepository.sumByAmountFromToAndPaymentMode(calculationFrom, calculationTo, Arrays.asList(PaymentMode.PERSONAL));
        if(totalAmountDB != 0L) totalAmount = totalAmountDB;
        int totalFixedMonthlyExpenses = expenseCalculatorRequestDTO.getFixedMonthlyExpenses().values().stream().mapToInt(i -> i).sum();
        int totalVariableMonthlyExpenses = expenseCalculatorRequestDTO.getVariableMonthlyExpenses().values().stream().mapToInt(i -> i).sum();;

        // Fetch persons
        List<Integer> fullPersons = expenseCalculatorRequestDTO.getFullPersons();
        List<Integer> halfPersons = expenseCalculatorRequestDTO.getHalfPersons();
        List<Integer> onVacationPersons = expenseCalculatorRequestDTO.getOnVacationPersons();
        int fullHalfSize = fullPersons.size() + halfPersons.size();

        int totalCalculatedFixedPerHead = totalFixedMonthlyExpenses / (fullHalfSize + onVacationPersons.size());

        ExpenseCalculatorResponseDTO expenseCalculatorResponseDTO = new ExpenseCalculatorResponseDTO();
        List<ExpenseCalculatorPersons> expenseCalculatorPersonList = new ArrayList<>();

        if (!fullPersons.isEmpty()) {
            for (Integer person : fullPersons) {
                int fullpersonCalExp = (int) (totalCalculatedFixedPerHead + ((totalVariableMonthlyExpenses +totalAmount) / (fullHalfSize)) + 900);
                expenseCalculatorPersonList.add(getExpenseCalculatorPersons("Full", person, fullpersonCalExp, calculationFrom, calculationTo));
            }
        }

        if (!halfPersons.isEmpty()) {
            for (Integer person : halfPersons) {
                int fullpersonCalExp = (int) (totalCalculatedFixedPerHead + (((totalVariableMonthlyExpenses +totalAmount) / (fullHalfSize)) + 900) / 2);
                expenseCalculatorPersonList.add(getExpenseCalculatorPersons("Half", person, fullpersonCalExp, calculationFrom, calculationTo));
            }
        }

        if (!onVacationPersons.isEmpty()) {
            for (Integer person : onVacationPersons) {
                expenseCalculatorPersonList.add(getExpenseCalculatorPersons("OnVacation", person, totalCalculatedFixedPerHead, calculationFrom,calculationTo));
            }
        }

        expenseCalculatorResponseDTO.setTo(calculationTo);
        expenseCalculatorResponseDTO.setFrom(calculationFrom);
        expenseCalculatorResponseDTO.setTotalAmount((int) (totalAmount + totalVariableMonthlyExpenses + totalFixedMonthlyExpenses));
        expenseCalculatorResponseDTO.setPersons(expenseCalculatorPersonList);

        return expenseCalculatorResponseDTO;
    }

    private ExpenseCalculatorPersons getExpenseCalculatorPersons(String personType, int person, int totalCalculatedAmt, Date from, Date to) {
        UserCalculator userCalculator = (UserCalculator) userService.getUserById((long) person, "UserCalculator");

        int myExp =expenseRepository.sumByAmountFromToAndPaymentModeMy(from, to, Arrays.asList(PaymentMode.PERSONAL),userCalculator.getId());

        ExpenseCalculatorPersons expenseCalculatorPersons = new ExpenseCalculatorPersons();
        expenseCalculatorPersons.setPerson(userCalculator);
        expenseCalculatorPersons.setTotalCalcualtedMonthlyAmount(totalCalculatedAmt);
        expenseCalculatorPersons.setTotalMyMonthlyAmount(myExp);
        expenseCalculatorPersons.setTotalPayableAmount(totalCalculatedAmt - myExp);
        expenseCalculatorPersons.setPersonType(personType);

        return expenseCalculatorPersons;
    }
}
