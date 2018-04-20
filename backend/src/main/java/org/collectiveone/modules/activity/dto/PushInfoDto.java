package org.collectiveone.modules.activity.dto;

import org.collectiveone.modules.activity.enums.NotificationPushState;

public class PushInfoDto {
	
	private NotificationPushState state;
	private String message;
	private String url;
	private String icon;
	
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public NotificationPushState getState() {
		return state;
	}
	public void setState(NotificationPushState state) {
		this.state = state;
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
	
}
