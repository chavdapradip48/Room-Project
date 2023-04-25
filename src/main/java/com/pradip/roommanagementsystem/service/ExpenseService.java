package com.pradip.roommanagementsystem.service;

import com.pradip.roommanagementsystem.dto.ExpenseDTO;
import com.pradip.roommanagementsystem.dto.projection.ExpenseProjection;
import com.pradip.roommanagementsystem.entity.Expense;
import com.pradip.roommanagementsystem.exception.ResourceNotFoundException;
import com.pradip.roommanagementsystem.repository.ExpenseRepository;
import com.pradip.roommanagementsystem.repository.UserRepository;
import com.pradip.roommanagementsystem.util.GeneralUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GeneralUtil generalUtil;

    public List<ExpenseProjection> getAllExpenses() {
        List<ExpenseProjection> allBy = expenseRepository.findAllBy(ExpenseProjection.class);
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
}
