package com.simplilearn.projects.adminportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simplilearn.projects.adminportal.modal.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	User findByEmailAndPassword(String email, String existingPassword);

	User findByEmail(String email);

}
