package org.collectiveone.service;

import org.collectiveone.modules.users.AppUserDto;

public class AppUserAuthDto {
	private AppUserDto user;
	private String authToken;
	private String rootPerspectiveId;
	
	public AppUserDto getUser() {
		return user;
	}
	public void setUser(AppUserDto user) {
		this.user = user;
	}
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	public String getRootPerspectiveId() {
		return rootPerspectiveId;
	}
	public void setRootPerspectiveId(String rootPerspectiveId) {
		this.rootPerspectiveId = rootPerspectiveId;
	}
	
}
