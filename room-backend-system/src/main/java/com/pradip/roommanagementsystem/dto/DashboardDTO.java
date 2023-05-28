package com.pradip.roommanagementsystem.dto;

import lombok.Data;

import java.util.Map;

@Data
public class DashboardDTO {
    private long totalAmount;
    private long myTotalAmount;
    private long currentMonthAmount;
    private long previousMonthAmount;
    private float currentPreviousMonthPercent;
    private Map<String, Long> graphData;
}
