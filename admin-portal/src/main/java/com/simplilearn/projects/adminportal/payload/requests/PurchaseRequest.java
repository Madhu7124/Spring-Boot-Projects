package com.simplilearn.projects.adminportal.payload.requests;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class PurchaseRequest {
	private Long id;
	private Long categoryid;
	private Long productid;
	private Date purchasedate;
}
