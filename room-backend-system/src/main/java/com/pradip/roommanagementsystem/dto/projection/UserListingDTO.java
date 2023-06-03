package com.pradip.roommanagementsystem.dto.projection;

import com.pradip.roommanagementsystem.entity.Address;
import com.pradip.roommanagementsystem.entity.Role;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public interface UserListingDTO {
	Long getId();
	String getEmail();
	@Value("#{target.firstName + ' ' + target.lastName}")
	String getFullName();
	boolean isEnabled();
	String getProfilePhoto();
}