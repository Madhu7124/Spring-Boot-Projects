package com.simplilearn.projects.adminportal.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simplilearn.projects.adminportal.modal.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

	List<Purchase> findByCategoryId(Long categoryId);

	List<Purchase> findByPurchaseDate(Date purchaseDate);

}
