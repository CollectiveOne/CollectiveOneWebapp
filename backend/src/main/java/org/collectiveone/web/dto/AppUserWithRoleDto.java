package org.collectiveone.web.dto;

public class AppUserWithRoleDto extends AppUserDto {
	private String role;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
