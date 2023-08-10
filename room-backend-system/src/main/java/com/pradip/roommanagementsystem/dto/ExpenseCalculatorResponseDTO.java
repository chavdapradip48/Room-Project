package com.pradip.roommanagementsystem.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class ExpenseCalculatorResponseDTO {
    private List<ExpenseCalculatorPersons> persons;
    private Integer totalAmount;
    @NotBlank(message = "From Must Present")
    private Date from;
    @NotBlank(message = "To Must Present")
    private Date to;
}
