package org.collectiveone.modules.activity.repositories;

import java.sql.Timestamp;
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
	
	@Query("SELECT notif FROM Notification notif WHERE notif.subscriber.user.c1Id = ?1 AND notif.id IN ?2")
	List<Notification> findByIdIn(UUID userId, List<UUID> notificationIds);
	
	@Query("SELECT notif FROM Notification notif JOIN notif.activity act WHERE notif.subscriber.user.c1Id = ?1 ORDER BY act.timestamp DESC")
	List<Notification> findOfUser(UUID userId, Pageable page);
	
	@Query("SELECT notif FROM Notification notif "
			+ "JOIN notif.activity act "
			+ "LEFT JOIN act.modelCardWrapperAddition crdWrpAdd "
			+ "WHERE notif.subscriber.user.c1Id = ?1 "
			+ "AND notif.inAppState IN ?2 "
			+ "AND (act.modelSection.id IN ?3 "
			+ "OR act.fromSection.id IN ?3 "
			+ "OR act.onSection.id IN ?3 "
			+ "OR act.modelCardWrapper.id IN ?4 "
			+ "OR crdWrpAdd.section.id IN ?3 "
			+ "OR crdWrpAdd.cardWrapper.id IN ?4) "
			+ "ORDER BY act.timestamp DESC")
	List<Notification> findOfUserInSections(UUID userId, List<NotificationState> states, List<UUID> sectionIds, List<UUID> cardWrappersIds, Pageable page);	
	
	/* WARNING ALMOST REPEATED QUERY USED FOR COUNTING! */
	@Query("SELECT COUNT(notif) FROM Notification notif "
			+ "JOIN notif.activity act "
			+ "LEFT JOIN act.modelCardWrapperAddition crdWrpAdd "
			+ "WHERE notif.subscriber.user.c1Id = ?1 "
			+ "AND notif.inAppState = 'PENDING' "
			+ "AND (act.modelSection.id IN ?2 "
			+ "OR act.fromSection.id IN ?2 "
			+ "OR act.onSection.id IN ?2 "
			+ "OR act.modelCardWrapper.id IN ?3 "
			+ "OR crdWrpAdd.section.id IN ?2 "
			+ "OR crdWrpAdd.cardWrapper.id IN ?3)")
	Integer countOfUserInSections(UUID userId, List<UUID> sectionIds, List<UUID> cardWrappersIds);	
	
	@Query("SELECT notif FROM Notification notif "
            + "JOIN notif.activity act "
            + "WHERE notif.subscriber.user.c1Id = ?1 "
            + "AND notif.inAppState IN ?2 "
            + "AND act.initiative.id IN ?3 "
            + "ORDER BY act.timestamp DESC")
    List<Notification> findOfUserInInitiatives(UUID userId, List<NotificationState> states, List<UUID> initiativeIds, Pageable page);
	
	/* WARNING ALMOST REPEATED QUERY USED FOR COUNTING! */
	@Query("SELECT COUNT(notif) FROM Notification notif "
            + "JOIN notif.activity act "
            + "WHERE notif.subscriber.user.c1Id = ?1 "
            + "AND notif.inAppState = 'PENDING' "
            + "AND act.initiative.id IN ?2")
    Integer countOfUserInInitiatives(UUID userId, List<UUID> initiativeIds);
	
	List<Notification> findBySubscriber_User_C1IdAndInAppState(UUID userId, NotificationState notificationState);
	
	@Query("SELECT notif FROM Notification notif JOIN notif.activity act WHERE notif.emailNowState = ?1 AND act.timestamp < ?2")
	List<Notification> findByEmailNowStateOlderThan(NotificationState notificationState, Timestamp timestamp);
	
	List<Notification> findBySubscriber_EmailSummaryPeriodConfigAndEmailSummaryState(SubscriberEmailSummaryPeriodConfig config, NotificationState notificationEmailState);
}
