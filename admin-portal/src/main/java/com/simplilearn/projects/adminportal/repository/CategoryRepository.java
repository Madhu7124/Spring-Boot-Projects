package com.simplilearn.projects.adminportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simplilearn.projects.adminportal.modal.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	Category findByNameAndType(String categoryname, String categorytype);

}
