package com.pradip.roommanagementsystem.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ExpenseCountResponseDTO {
    private Long totalAmount;
    private int persons;
    private Long perHeadAmount;
}
