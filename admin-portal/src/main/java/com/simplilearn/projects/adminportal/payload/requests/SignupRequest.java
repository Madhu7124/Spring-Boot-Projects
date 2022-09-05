package com.simplilearn.projects.adminportal.payload.requests;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class SignupRequest {
	private String username;
	private String email;
	private String password;
	private Set<String> role;
}
