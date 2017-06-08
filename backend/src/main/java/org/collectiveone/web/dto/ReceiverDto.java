package org.collectiveone.web.dto;

public class ReceiverDto {
	
	private AppUserDto user;
	private double percent;
	
	
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
	
}
