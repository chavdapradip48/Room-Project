package com.pradip.roommanagementsystem.controller;

import com.pradip.roommanagementsystem.dto.ApiResponse;
import com.pradip.roommanagementsystem.dto.ExpenseCalculatorRequestDTO;
import com.pradip.roommanagementsystem.service.ExpenseCalculatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/user/expense/calculator")
@CrossOrigin(origins = "*")
@Slf4j
public class ExpenseCalculatorController {
    @Autowired
    private ExpenseCalculatorService expenseCalculatorService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllCalculatedExpenses(@RequestParam(name = "type", defaultValue = "Listing") String type) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "Calculated expenses fetched successfully.", expenseCalculatorService.getAllExpenseCalculatorData(type))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getCalculatedExpensesById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "Calculated expense fetched successfully.", expenseCalculatorService.getExpenseCalculatorById(id))
        );
    }

    @PostMapping("/calculate")
    public ResponseEntity<ApiResponse<?>> getExpenseCount(
            @Valid @RequestBody ExpenseCalculatorRequestDTO expenseCountRequestDTO,
            @RequestParam(name = "isStore", defaultValue = "false") boolean isStore, HttpServletRequest request) {
        boolean isAdmin = request.isUserInRole("ROLE_ADMIN");
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "Expenses calculated successfully.", expenseCalculatorService.calculateExpensesAndStore(expenseCountRequestDTO, isStore, isAdmin))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteCalculatedExpensesById(@PathVariable Long id) {
        expenseCalculatorService.deleteExpenseCalculatorById(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "Calculated expense deleted successfully.", null)
        );
    }
}
