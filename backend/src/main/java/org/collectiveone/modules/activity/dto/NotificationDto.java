package org.collectiveone.modules.activity.dto;

public class NotificationDto {

	private String id;
	private SubscriberDto subscriber;
	private ActivityDto activity;
	private String inAppState;
	private String pushState;
	private String emailNowState;
	private String emailSummaryState;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public SubscriberDto getSubscriber() {
		return subscriber;
	}
	public void setSubscriber(SubscriberDto subscriber) {
		this.subscriber = subscriber;
	}
	public ActivityDto getActivity() {
		return activity;
	}
	public void setActivity(ActivityDto activityDto) {
		this.activity = activityDto;
	}
	public String getInAppState() {
		return inAppState;
	}
	public void setInAppState(String inAppState) {
		this.inAppState = inAppState;
	}
	public String getPushState() {
		return pushState;
	}
	public void setPushState(String pushState) {
		this.pushState = pushState;
	}
	public String getEmailNowState() {
		return emailNowState;
	}
	public void setEmailNowState(String emailNowState) {
		this.emailNowState = emailNowState;
	}
	public String getEmailSummaryState() {
		return emailSummaryState;
	}
	public void setEmailSummaryState(String emailSummaryState) {
		this.emailSummaryState = emailSummaryState;
	}
	
}
