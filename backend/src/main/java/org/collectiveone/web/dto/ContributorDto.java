package org.collectiveone.web.dto;

public class ContributorDto {
	private String id;
	private String intiativeId;
	private AppUserDto user;
	private String role;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIntiativeId() {
		return intiativeId;
	}

	public void setIntiativeId(String intiativeId) {
		this.intiativeId = intiativeId;
	}

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
