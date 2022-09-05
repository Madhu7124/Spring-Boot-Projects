package com.simplilearn.projects.adminportal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.httpBasic().and().authorizeRequests().antMatchers(HttpMethod.GET, "/api/admin/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.POST, "/api/admin").hasRole("ADMIN")
				.antMatchers(HttpMethod.PUT, "/api/admin/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.PATCH, "/api/admin/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.DELETE, "/api/admin/**").hasRole("ADMIN").and().csrf().disable()
				.formLogin().loginPage("/api/login").permitAll().and().logout().permitAll();
	}

	/*
	 * @Bean public UserDetailsService userDetailsService() {
	 * 
	 * User.UserBuilder users = User.withDefaultPasswordEncoder();
	 * 
	 * InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
	 * manager.createUser(users.username("admin").password("password").roles("ADMIN"
	 * ).build()); return manager; }
	 */

}
