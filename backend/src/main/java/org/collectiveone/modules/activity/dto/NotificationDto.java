package org.collectiveone.modules.activity.dto;

import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserDto;

public class NotificationDto {

	private String id;
	private AppUserDto subscriber;
	private AppUserDto triggerUser;
	private String subscriberState;
	private ActivityDto activity;
	private String emailState;
	private String pushState;
	private String message;
	private String url;
	private Boolean isHtml;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public AppUserDto getTriggerUser() {
		return triggerUser;
	}
	public void setTriggerUser(AppUserDto triggerUser) {
		this.triggerUser = triggerUser;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getEmailState() {
		return emailState;
	}
	public void setEmailState(String emailState) {
		this.emailState = emailState;
	}
	public String getPushState() {
		return pushState;
	}
	public void setPushState(String pushState) {
		this.pushState = pushState;
	}
	public Boolean getIsHtml() {
		return isHtml;
	}
	public void setIsHtml(Boolean isHtml) {
		this.isHtml = isHtml;
	}
	public AppUserDto getSubscriber() {
		return subscriber;
	}
	public void setSubscriber(AppUserDto subscriber) {
		this.subscriber = subscriber;
	}
	
}
