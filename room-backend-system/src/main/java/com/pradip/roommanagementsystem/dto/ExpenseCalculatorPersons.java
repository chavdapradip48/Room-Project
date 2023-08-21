package com.pradip.roommanagementsystem.dto;

import com.pradip.roommanagementsystem.dto.projection.UserCalculator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseCalculatorPersons {
    private UserCalculator person;
    private Integer totalCalcualtedMonthlyAmount;
    private Integer totalMyMonthlyAmount;
    private Integer totalPayableAmount;
    private String personType;
}
