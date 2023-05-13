package com.pradip.roommanagementsystem.dto.projection;

import com.pradip.roommanagementsystem.entity.Address;
import com.pradip.roommanagementsystem.entity.Role;

import java.util.List;

public interface UserDTO {
	Long getId();
	String getEmail();
	String getFirstName();
	String getLastName();
	String getMobile();
	String getGender();
	Address getAddress();
	boolean isEnabled();
	boolean isLocked();
	List<Role> getRoles();
	
	String getProfilePhoto();
}