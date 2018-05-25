package org.collectiveone.modules.activity;

import java.util.ArrayList;
import java.util.List;

public class NotificationsHereAndUnder {
	List<Notification> notificationsHere = new ArrayList<Notification>();
	List<Notification> notificationsUnder = new ArrayList<Notification>();
	
	public List<Notification> getNotificationsHere() {
		return notificationsHere;
	}
	public void setNotificationsHere(List<Notification> notificationsHere) {
		this.notificationsHere = notificationsHere;
	}
	public List<Notification> getNotificationsUnder() {
		return notificationsUnder;
	}
	public void setNotificationsUnder(List<Notification> notificationsUnder) {
		this.notificationsUnder = notificationsUnder;
	}

}
