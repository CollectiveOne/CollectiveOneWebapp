package org.collectiveone.web.dto;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class UserNewDto {
	@Size(min=4, max=30, message="username must longer than 4 characters")
	private String username;
	@NotEmpty(message="a valid email must be provided")
	private String email;
	@Size(min=4, max=30, message="password must be longer than 8 characters")
	private String password;
	@NotEmpty
	private String passwordConfirm;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username.trim();
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email.trim();
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password.trim();
	}
	public String getPasswordConfirm() {
		return passwordConfirm;
	}
	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm.trim();
	}
}
