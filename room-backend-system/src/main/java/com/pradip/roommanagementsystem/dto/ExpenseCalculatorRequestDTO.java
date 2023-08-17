package com.pradip.roommanagementsystem.dto;


import com.pradip.roommanagementsystem.entity.User;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class ExpenseCalculatorRequestDTO {
    private Long id;
    private Map<String, Integer> fixedMonthlyExpenses;
    private Map<String, Integer> variableMonthlyExpenses;
    private List<Integer> fullPersons;
    private List<Integer> halfPersons;
    private List<Integer> onVacationPersons;
    @NotBlank(message = "From Must Present")
    private Timestamp from;
    @NotBlank(message = "To Must Present")
    private Timestamp to;
}
