package com.pradip.roommanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Data
public class JobDTO {
    private String jobName;

    private String triggerName;

    private String jobClass;

    private String cronExpression;

    private Map<String, Object> jobData;

    private Integer numberOfJobExecutingCount;
    private String currentTurn;

    private String nextTurn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
    private Date jobStartTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
    private Date lastFireTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
    private Date nextFireTime;

    private String status;
}