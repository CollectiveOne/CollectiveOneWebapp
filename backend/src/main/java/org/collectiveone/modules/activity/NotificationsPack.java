package org.collectiveone.modules.activity;

import java.util.ArrayList;
import java.util.List;

public class NotificationsPack {
	Integer totalUnread;
	List<Notification> notifications = new ArrayList<Notification>();
	
	public Integer getTotalUnread() {
		return totalUnread;
	}
	public void setTotalUnread(Integer totalUnread) {
		this.totalUnread = totalUnread;
	}
	public List<Notification> getNotifications() {
		return notifications;
	}
	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}
	
}
