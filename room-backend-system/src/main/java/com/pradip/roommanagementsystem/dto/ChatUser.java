package com.pradip.roommanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ChatUser {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private boolean onlineStatus;
}
