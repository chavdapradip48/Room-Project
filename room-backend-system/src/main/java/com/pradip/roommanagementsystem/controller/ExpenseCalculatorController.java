package com.pradip.roommanagementsystem.controller;

import com.pradip.roommanagementsystem.dto.ApiResponse;
import com.pradip.roommanagementsystem.dto.ExpenseCalculatorRequestDTO;
import com.pradip.roommanagementsystem.service.ExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.io.IOException;

@RestController
@RequestMapping("/user/expense/calculator")
@CrossOrigin(origins = "*")
@Slf4j
public class ExpenseCalculatorController {
    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllCalculatedExpenses() throws IOException {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "Calculated expenses fetched successfully.", expenseService.getAllExpenseCalculatorData())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getCalculatedExpensesById(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "Calculated expense fetched successfully.", expenseService.getExpenseCalculatorById(id))
        );
    }

    @PostMapping("/calculate")
    public ResponseEntity<ApiResponse<?>> getExpenseCount(
            @Valid @RequestBody ExpenseCalculatorRequestDTO expenseCountRequestDTO,@PathParam("isStore") boolean isStore) throws IOException {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "Expenses calculated successfully.", expenseService.calculateExpensesAndStore(expenseCountRequestDTO, isStore))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteCalculatedExpensesById(@PathVariable Long id) {
        expenseService.deleteExpenseCalculatorById(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "Calculated expense deleted successfully.", null)
        );
    }
}
