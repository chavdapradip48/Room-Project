package com.pradip.roommanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    @JsonIgnore
    private String password;
    private boolean enabled;
}
