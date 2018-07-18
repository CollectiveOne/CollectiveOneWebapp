package org.collectiveone.modules.model.dto;

import java.util.ArrayList;
import java.util.List;

import org.collectiveone.modules.model.PermissionConfig;
import org.collectiveone.modules.tokens.dto.AssetsDto;
import org.collectiveone.modules.users.AppUserDto;

public class MemberDto {
	private String id;
	private AppUserDto user;
	private String role;
	private List<AssetsDto> receivedAssets = new ArrayList<AssetsDto>();
	
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
		else return PermissionConfig.GUEST.toString();
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<AssetsDto> getReceivedAssets() {
		return receivedAssets;
	}

	public void setReceivedAssets(List<AssetsDto> receivedAssets) {
		this.receivedAssets = receivedAssets;
	}
	

}
