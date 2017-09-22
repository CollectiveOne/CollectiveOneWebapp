package org.collectiveone.modules.activity.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.activity.Notification;
import org.collectiveone.modules.activity.enums.NotificationEmailState;
import org.collectiveone.modules.activity.enums.NotificationState;
import org.collectiveone.modules.activity.enums.SubscriberEmailNotificationsState;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRepositoryIf extends CrudRepository<Notification, UUID> {

	List<Notification> findTop10BySubscriber_User_C1IdOrderByCreationDateDesc(UUID userId);
	
	List<Notification> findBySubscriber_User_C1IdAndState(UUID userId, NotificationState state);
	
	List<Notification> findBySubscriber_EmailNotificationsStateAndEmailState(SubscriberEmailNotificationsState subscriberEmailNotificationState, NotificationEmailState notificationEmailState);
}
