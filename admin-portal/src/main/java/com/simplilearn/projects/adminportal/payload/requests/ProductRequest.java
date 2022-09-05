package com.simplilearn.projects.adminportal.payload.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ProductRequest {
	private Long id;
	private String name;
	private String description;
	private Long categoryid;
	private String categoryname;
	private String categorytype;
	private String categorydesc;
}
