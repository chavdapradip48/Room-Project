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
	boolean isEnabled();
	boolean isLocked();
	Address getAddress();
	List<Role> getRoles();
}