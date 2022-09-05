package com.simplilearn.projects.adminportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simplilearn.projects.adminportal.modal.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	Product findByName(String name);

}
