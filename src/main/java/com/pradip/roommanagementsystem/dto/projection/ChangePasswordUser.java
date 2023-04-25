package com.pradip.roommanagementsystem.dto.projection;

import com.pradip.roommanagementsystem.entity.Otp;

public interface ChangePasswordUser {
    Long getId();
    String getFirstName();
    String getLastName();
    String getEmail();
    Otp getOtp();
}
