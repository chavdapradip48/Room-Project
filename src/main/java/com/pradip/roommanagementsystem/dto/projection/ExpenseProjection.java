package com.pradip.roommanagementsystem.dto.projection;

import com.pradip.roommanagementsystem.entity.Address;
import com.pradip.roommanagementsystem.entity.Role;
import com.pradip.roommanagementsystem.entity.User;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


public interface ExpenseProjection {
    Long getId();
    String getPaymentMode();
    String getAmount();
    String getDescription();
    UserPersonal getUser();
    Timestamp getCreatedAt();
    Timestamp getUpdatedAt();


}
