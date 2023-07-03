package com.pradip.roommanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchedulerJobNotification {
    private String jobName;
    private String jobTurn;
    private long delay;
}
