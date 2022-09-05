package com.simplilearn.projects.adminportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simplilearn.projects.adminportal.modal.ERole;
import com.simplilearn.projects.adminportal.modal.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
