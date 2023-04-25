package com.pradip.roommanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserExpenseDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;

    public UserExpenseDTO(Long id) {
        this.id = id;
    }
}
