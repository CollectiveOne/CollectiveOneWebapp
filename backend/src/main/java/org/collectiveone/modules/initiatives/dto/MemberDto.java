package org.collectiveone.modules.initiatives.dto;

import org.collectiveone.modules.governance.enums.DecisionMakerRole;
import org.collectiveone.modules.users.dto.AppUserDto;

public class MemberDto {
	private String id;
	private AppUserDto user;
	private String role;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AppUserDto getUser() {
		return user;
	}

	public void setUser(AppUserDto user) {
		this.user = user;
	}

	public String getRole() {
		if (role != null) return role;
		else return DecisionMakerRole.ALIEN.toString();
	}

	public void setRole(String role) {
		this.role = role;
	}
	
}
