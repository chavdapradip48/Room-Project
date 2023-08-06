package com.pradip.roommanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNormalDTO {
    private Long id;
    private String fullName;
    private String email;
    private String profilePhoto;
    private boolean enabled;
    public UserNormalDTO(Long id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }
}
