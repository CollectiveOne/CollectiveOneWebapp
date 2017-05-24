package org.collectiveone.web.dto;

public class ContributorDto {
	private AppUserDto user;
	private String role;
	
	public AppUserDto getUser() {
		return user;
	}

	public void setUser(AppUserDto user) {
		this.user = user;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
