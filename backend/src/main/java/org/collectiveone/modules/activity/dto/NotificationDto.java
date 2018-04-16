package org.collectiveone.modules.activity.dto;

import org.collectiveone.modules.users.AppUserDto;

public class NotificationDto {

	private String id;
	private AppUserDto subscriberUser;
	private String subscriberState;
	private ActivityDto activity;
	private String state;
	private String pushState;
	private String pushMessage;
	
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
	public String getPushState() {
		return pushState;
	}
	public void setPushState(String pushState) {
		this.pushState = pushState;
	}
	public void setPushMessage(String pushMessage) {
		this.pushMessage = pushMessage;
	}
	public String getPushMessage() {
		return pushMessage;
	}
	
}
