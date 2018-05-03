package org.collectiveone.modules.activity.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.activity.Notification;
import org.collectiveone.modules.activity.enums.NotificationState;
import org.collectiveone.modules.activity.enums.SubscriberEmailSummaryPeriodConfig;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRepositoryIf extends CrudRepository<Notification, UUID> {

	Notification findById(UUID notificationId);
	
	@Query("SELECT notif FROM Notification notif JOIN notif.activity act WHERE notif.subscriber.user.c1Id = ?1 ORDER BY act.timestamp DESC")
	List<Notification> findOfUser(UUID userId, Pageable page);
	
	@Query("SELECT notif FROM Notification notif "
			+ "JOIN notif.activity act "
			+ "WHERE notif.subscriber.user.c1Id = ?1 "
			+ "AND notif.inAppState = ?2 "
			+ "AND (act.modelSection.id IN ?3 "
			+ "OR act.modelCardWrapper.id IN ?4) "
			+ "ORDER BY act.timestamp DESC")
	List<Notification> findOfUserInSections(UUID userId, NotificationState state, List<UUID> sectionIds, List<UUID> cardWrappersIds, Pageable page);	
	
	
	@Query("SELECT notif FROM Notification notif "
            + "JOIN notif.activity act "
            + "WHERE notif.subscriber.user.c1Id = ?1 "
            + "AND notif.inAppState = ?2 "
            + "AND act.initiative.id IN ?3 "
            + "ORDER BY act.timestamp DESC")
    List<Notification> findOfUserInInitiatives(UUID userId, NotificationState state, List<UUID> initiativeIds, Pageable page);
	
	List<Notification> findBySubscriber_User_C1IdAndInAppState(UUID userId, NotificationState notificationState);
	
	List<Notification> findByEmailNowState(NotificationState notificationState);
	
	List<Notification> findBySubscriber_EmailSummaryPeriodConfigAndEmailSummaryState(SubscriberEmailSummaryPeriodConfig config, NotificationState notificationEmailState);
}
