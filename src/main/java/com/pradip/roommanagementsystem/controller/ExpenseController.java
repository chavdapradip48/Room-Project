package com.pradip.roommanagementsystem.controller;

import com.pradip.roommanagementsystem.dto.ApiResponse;
import com.pradip.roommanagementsystem.dto.ExpenseDTO;
import com.pradip.roommanagementsystem.dto.UserExpenseDTO;
import com.pradip.roommanagementsystem.dto.projection.ExpenseProjection;
import com.pradip.roommanagementsystem.entity.Expense;
import com.pradip.roommanagementsystem.service.ExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@Slf4j
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/expense")
    public ResponseEntity<ApiResponse<List<ExpenseProjection>>> getAllExpenses() {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "Expenses fetched successfully.", expenseService.getAllExpenses())
        );
    }

    @GetMapping("/expense/{id}")
    public ResponseEntity<ApiResponse<ExpenseProjection>> getExpenseByExpenseId(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<ExpenseProjection>(HttpStatus.OK.value(),
                "Expense fetched successfully.", expenseService.getExpenseByExpenseId(id))
        );
    }

    @GetMapping("/{userId}/expense")
    public ResponseEntity<ApiResponse<List<ExpenseProjection>>> getExpenseByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(new ApiResponse<List<ExpenseProjection>>(HttpStatus.OK.value(),
                "Expense fetched successfully.", expenseService.getExpenseByUserId(userId))
        );
    }

    @PostMapping("{userId}/expense")
    public ResponseEntity<ApiResponse<Expense>> addExpense(@Valid @RequestBody ExpenseDTO expenseDTO,
                                                           @PathVariable Long userId){
        expenseDTO.setUser(new UserExpenseDTO(userId));
        return ResponseEntity.ok(new ApiResponse<Expense>(HttpStatus.OK.value(),
                "Expense saved successfully.", expenseService.addExpense(expenseDTO))
        );
    }

    @DeleteMapping("{userId}/expense")
    public ResponseEntity<ApiResponse<String>> deleteExpenseByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(new ApiResponse<String>(HttpStatus.OK.value(),
                expenseService.deleteExpenseByUserId(userId), null)
        );
    }
    @PutMapping("{userId}/expense/{expenseId}")
    public ResponseEntity<ApiResponse<Expense>> updateExpense(@Valid @RequestBody ExpenseDTO expenseDTO,
                                                           @PathVariable Long expenseId,
                                                              @PathVariable Long userId){
        expenseDTO.setUser(new UserExpenseDTO(userId));
        expenseDTO.setId(expenseId);
        return ResponseEntity.ok(new ApiResponse<Expense>(HttpStatus.OK.value(),
                "User saved successfully.", expenseService.addExpense(expenseDTO))
        );
    }

    @DeleteMapping("/expense/{expenseId}")
    public ResponseEntity<ApiResponse<String>> deleteExpenseById(@PathVariable Long expenseId) {
        return ResponseEntity.ok(new ApiResponse<String>(HttpStatus.OK.value(),
                expenseService.deleteExpenseById(expenseId), null)
        );
    }
}
