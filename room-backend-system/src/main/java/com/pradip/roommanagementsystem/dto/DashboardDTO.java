package com.pradip.roommanagementsystem.dto;

import lombok.Data;

import java.util.Map;

@Data
public class DashboardDTO {
    private Long totalAmount;
    private Long myTotalAmount;
    private Long currentMonthAmount;
    private Long previousMonthAmount;
    private float currentPreviousMonthPercent;
    private Map<String, Long> graphData;
}
