package org.collectiveone.modules.activity;

import org.collectiveone.modules.users.AppUserDto;

public class SubscriberDto {
	
	private String id;
	private AppUserDto user;
	private String elementId;
	private String elementType;
	private String state;
	private String emailNotificationsState;
	
	
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
	public String getElementId() {
		return elementId;
	}
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	public String getElementType() {
		return elementType;
	}
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getEmailNotificationsState() {
		return emailNotificationsState;
	}
	public void setEmailNotificationsState(String emailNotificationsState) {
		this.emailNotificationsState = emailNotificationsState;
	}

}
