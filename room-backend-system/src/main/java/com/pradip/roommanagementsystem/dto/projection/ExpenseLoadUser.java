package com.pradip.roommanagementsystem.dto.projection;


import org.springframework.beans.factory.annotation.Value;

public interface ExpenseLoadUser {
    Long getId();
    @Value("#{target.firstName + ' ' + target.lastName}")
    String getFullName();
}
