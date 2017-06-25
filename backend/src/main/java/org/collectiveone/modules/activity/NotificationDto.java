package org.collectiveone.modules.activity;

import org.collectiveone.modules.users.AppUserDto;

public class NotificationDto {

	private AppUserDto subscriberUserDto;
	private String subscriberState;
	private ActivityDto activityDto;
	private String state;
	
	public AppUserDto getSubscriberUserDto() {
		return subscriberUserDto;
	}
	public void setSubscriberUserDto(AppUserDto subscriberUserDto) {
		this.subscriberUserDto = subscriberUserDto;
	}
	public String getSubscriberState() {
		return subscriberState;
	}
	public void setSubscriberState(String subscriberState) {
		this.subscriberState = subscriberState;
	}
	public ActivityDto getActivityDto() {
		return activityDto;
	}
	public void setActivityDto(ActivityDto activityDto) {
		this.activityDto = activityDto;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}
