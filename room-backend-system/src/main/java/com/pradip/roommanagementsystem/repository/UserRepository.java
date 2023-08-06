package com.pradip.roommanagementsystem.repository;

import com.pradip.roommanagementsystem.dto.UserNormalDTO;
import com.pradip.roommanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);
	Boolean existsByEmail(String email);
	<T> List<T> findAllBy(Class<T> projectionType);
	<T> Optional<T> findById(Long id, Class<T> type);
	<T> Optional<T> findByEmail(String email, Class<T> className);
	@Query("SELECT new com.pradip.roommanagementsystem.dto.UserNormalDTO(u.id, CONCAT(u.firstName, ' ', u.lastName)) FROM User u")
	List<UserNormalDTO> findUsersIdFullname();
	@Query("SELECT new com.pradip.roommanagementsystem.dto.UserNormalDTO(u.id, CONCAT(u.firstName, ' ', u.lastName), u.email , u.profilePhoto, u.enabled) FROM User u")
	List<UserNormalDTO> findUsersIdFullnameProfile();
}
