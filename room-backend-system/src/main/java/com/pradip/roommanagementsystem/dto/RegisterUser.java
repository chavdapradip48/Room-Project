package com.pradip.roommanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pradip.roommanagementsystem.entity.Address;
import com.pradip.roommanagementsystem.entity.Role;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

@Data
@Component
public class RegisterUser {
    private long id;
    @Email
    private String email;

    @Size(max = 15)
    private String mobile;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String gender;

    private String profilePhoto;

    private Address address;

    @NotBlank
    private String password;
}
