package com.pradip.roommanagementsystem.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseCalculatorResponseDTO {
    private Long id;
    private List<ExpenseCalculatorPersons> persons;
    private Integer totalAmount;
    @NotBlank(message = "From Must Present")
    private Date from;
    @NotBlank(message = "To Must Present")
    private Date to;
}
