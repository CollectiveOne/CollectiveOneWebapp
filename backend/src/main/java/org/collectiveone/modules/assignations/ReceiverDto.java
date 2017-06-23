package org.collectiveone.modules.assignations;

import org.collectiveone.modules.users.AppUserDto;

public class ReceiverDto {
	
	private String id;
	private AppUserDto user;
	private double percent;
	private String state;
	
	
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
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
}
