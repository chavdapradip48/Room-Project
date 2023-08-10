package com.pradip.roommanagementsystem.dto.projection;

import org.springframework.beans.factory.annotation.Value;

public interface UserCalculator {
    Long getId();
    String getEmail();
    @Value("#{target.firstName + ' ' + target.lastName}")
    String getFullName();
    String getMobile();
}
