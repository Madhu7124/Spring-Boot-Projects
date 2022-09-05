package com.simplilearn.projects.adminportal.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UserInfoResponse {
	private Long id;
	private String username;
	private String email;
	private List<String> roles;
}
