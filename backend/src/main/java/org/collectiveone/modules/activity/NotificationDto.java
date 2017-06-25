package org.collectiveone.modules.activity;

import org.collectiveone.modules.users.AppUser;

public class NotificationDto {

	private AppUser subscriberUserDto;
	private String subscriberState;
	private ActivityDto activityDto;
	private String state;
	
	public AppUser getSubscriberUserDto() {
		return subscriberUserDto;
	}
	public void setSubscriberUserDto(AppUser subscriberUserDto) {
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
