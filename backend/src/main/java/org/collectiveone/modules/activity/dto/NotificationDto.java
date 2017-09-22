package org.collectiveone.modules.activity.dto;

import org.collectiveone.modules.users.AppUserDto;

public class NotificationDto {

	private String id;
	private AppUserDto subscriberUser;
	private String subscriberState;
	private ActivityDto activity;
	private String state;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public AppUserDto getSubscriberUser() {
		return subscriberUser;
	}
	public void setSubscriberUser(AppUserDto subscriberUserDto) {
		this.subscriberUser = subscriberUserDto;
	}
	public String getSubscriberState() {
		return subscriberState;
	}
	public void setSubscriberState(String subscriberState) {
		this.subscriberState = subscriberState;
	}
	public ActivityDto getActivity() {
		return activity;
	}
	public void setActivity(ActivityDto activityDto) {
		this.activity = activityDto;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}
