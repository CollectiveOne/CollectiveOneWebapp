package org.collectiveone.modules.activity.dto;

public class NotificationDto {

	private String id;
	private SubscriberDto subscriber;
	private ActivityDto activity;
	private String inAppState;
	private String pushState;
	private String emailNowState;
	private String emailSummaryState;
	private String message;
	private String url;
	private Boolean isHtml;
	
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
	public Boolean getIsHtml() {
		return isHtml;
	}
	public void setIsHtml(Boolean isHtml) {
		this.isHtml = isHtml;
	}
	
}
