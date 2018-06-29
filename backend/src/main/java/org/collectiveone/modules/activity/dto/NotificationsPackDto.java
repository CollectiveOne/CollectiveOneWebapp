package org.collectiveone.modules.activity.dto;

import java.util.ArrayList;
import java.util.List;

public class NotificationsPackDto {
	Integer totalUnread;
	List<NotificationDto> notifications = new ArrayList<NotificationDto>();
	
	public Integer getTotalUnread() {
		return totalUnread;
	}
	public void setTotalUnread(Integer totalUnread) {
		this.totalUnread = totalUnread;
	}
	public List<NotificationDto> getNotifications() {
		return notifications;
	}
	public void setNotifications(List<NotificationDto> notifications) {
		this.notifications = notifications;
	}
}
