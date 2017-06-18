package org.collectiveone.web.dto;

public class MemberDto {
	private String id;
	private String initiativeId;
	private AppUserDto user;
	private String role;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInitiativeId() {
		return initiativeId;
	}

	public void setInitiativeId(String intiativeId) {
		this.initiativeId = intiativeId;
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
