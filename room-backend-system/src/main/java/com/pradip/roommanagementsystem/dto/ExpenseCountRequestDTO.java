package com.pradip.roommanagementsystem.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Map;

@Data
public class ExpenseCountRequestDTO {

    @NotBlank(message = "From Must Present")
    private Date from;

    @NotBlank(message = "To Must Present")
    private Date to;

    private int persons;

    private Map<String, Integer> extraExpenses;
}
