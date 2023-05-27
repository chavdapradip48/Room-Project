package com.pradip.roommanagementsystem.service;

import com.pradip.roommanagementsystem.dto.DashboardDTO;
import com.pradip.roommanagementsystem.dto.ExpenseDTO;
import com.pradip.roommanagementsystem.dto.projection.ExpenseProjection;
import com.pradip.roommanagementsystem.entity.Expense;
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
            throw new ResourceNotFoundException("User not exist.");
        return expenseRepository.save(generalUtil.convertObject(expenseDTO, Expense.class));
    }

    public ExpenseProjection getExpenseByExpenseId(Long id) {
        Optional<ExpenseProjection> byId = expenseRepository.findById(id, ExpenseProjection.class);
        if (byId.isEmpty())
            throw new ResourceNotFoundException("Expense not available with specified id.");
        return  byId.get();
    }

    public List<ExpenseProjection> getExpenseByUserId(Long userId) {
        System.out.println(expenseRepository.existsByUserId(userId));
        if (!expenseRepository.existsByUserId(userId))
            throw new ResourceNotFoundException("User not have any expenses or might user not exist.");

        List<ExpenseProjection> byUserId = expenseRepository.findByUserId(userId);
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
        int year= LocalDate.now().getYear();
        Long totalExp=0L, myTotalExp=0L, currentMonthExp=0L, previousMonthExp=0L;
        DashboardDTO dashboardDTO=new DashboardDTO();
        List<Object[]> monthlyExpenseDataByYear = expenseRepository.findMonthlyExpenseDataByYear(year);

        if(monthlyExpenseDataByYear != null && monthlyExpenseDataByYear.isEmpty())
            throw new ResourceNotFoundException("Expenses are not available with "+year+" year.");

        Map<String, Long> monthlyExpenseData = new TreeMap<>(Comparator.comparing(Month::valueOf));
        int month = new Date().getMonth() + 1;

        for (Object[] result : monthlyExpenseDataByYear)
            monthlyExpenseData.put(getMonthName((Integer) result[0]).toUpperCase(), (Long) result[1]);
        currentMonthExp=expenseRepository.sumAmountByYearAndMonth(year,month);
        totalExp=expenseRepository.sumAmountByYear(year);
        myTotalExp=expenseRepository.sumAmountByUserEmailAndMonth(jwtUtils.getUserNameFromJwtToken(token), year, month);
        previousMonthExp=expenseRepository.sumAmountByYearAndMonth(year,month-1);

        dashboardDTO.setGraphData(monthlyExpenseData);
        dashboardDTO.setCurrentMonthAmount(currentMonthExp);
        dashboardDTO.setPreviousMonthAmount(previousMonthExp);
        dashboardDTO.setMyTotalAmount(myTotalExp);
        dashboardDTO.setCurrentPreviousMonthPercent(calculatePercentageChange(previousMonthExp,currentMonthExp));
        dashboardDTO.setTotalAmount(totalExp);
        return  dashboardDTO;
    }

    private String getMonthName(Integer monthNumber) {
        return Month.of(monthNumber).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    public float calculatePercentageChange(Long previousExp, Long currentExp) {
        double difference = currentExp - previousExp;
        float percentageChange = (float) (difference / previousExp * 100);
        return percentageChange;
    }
}
