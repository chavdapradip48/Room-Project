package com.pradip.roommanagementsystem.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseCalculatorListingDTO {
    private Long id;
    private int totalPerson;
    private int totalAmount;
    private Date from;
    private Date to;
}
