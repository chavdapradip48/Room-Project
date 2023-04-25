package com.pradip.roommanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.pradip.roommanagementsystem.entity.User;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class ExpenseDTO {
    private Long id;

    @NotBlank
    private PaymentMode paymentMode;

    @NotBlank
    private Long amount;

    @NotBlank
    private String description;

    @JsonProperty("user")
    @NotNull
    private UserExpenseDTO user;

//    private Timestamp createdAt;
//
//    private Timestamp updatedAt;
}
