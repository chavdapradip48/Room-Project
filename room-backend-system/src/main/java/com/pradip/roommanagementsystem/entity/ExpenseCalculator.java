package com.pradip.roommanagementsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "calculated_expenses")
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseCalculator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fixedMonthlyExpenses;

    @NotBlank
    private String variableMonthlyExpenses;

    @NotBlank
    private String fullPersons;

    @NotBlank
    private String halfPersons;

    @NotBlank
    private String onVacationPersons;

    @Column(nullable = false)
    private Timestamp durationFrom;

    @Column(nullable = false)
    private Timestamp durationTo;

    @CreationTimestamp
    @Column(nullable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp updatedAt;

}

