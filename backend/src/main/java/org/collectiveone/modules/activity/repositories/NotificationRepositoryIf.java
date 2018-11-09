package org.collectiveone.modules.activity.repositories;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.collectiveone.modules.activity.Notification;
import org.collectiveone.modules.activity.enums.NotificationState;
import org.collectiveone.modules.activity.enums.SubscriberEmailSummaryPeriodConfig;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface NotificationRepositoryIf extends CrudRepository<Notification, UUID> {

	Optional<Notification> findById(UUID notificationId);
	
	@Query("SELECT notif FROM Notification notif WHERE notif.subscriber.user.c1Id = ?1 AND notif.id IN ?2")
	List<Notification> findByIdIn(UUID userId, List<UUID> notificationIds);
	
	@Query("SELECT notif FROM Notification notif JOIN notif.activity act WHERE notif.subscriber.user.c1Id = ?1 ORDER BY act.timestamp DESC")
	List<Notification> findOfUser(UUID userId, Pageable page);
	
	@Query("SELECT notif FROM Notification notif "
			+ "JOIN notif.activity act "
			+ "LEFT JOIN act.modelCardWrapperAddition crdWrpAdd "
			+ "LEFT JOIN act.modelSubsection subsec "
			+ "WHERE notif.subscriber.user.c1Id = :userId "
			+ "AND notif.inAppState IN :states "
			+ "AND (act.modelSection.id IN :sectionIds "
			+ "OR act.fromSection.id IN :sectionIds "
			+ "OR act.onSection.id IN :sectionIds "
			+ "OR act.modelCardWrapper.id IN :cardWrappersIds "
			+ "OR subsec.section.id IN :sectionIds "
			+ "OR subsec.parentSection.id IN :sectionIds "
			+ "OR crdWrpAdd.section.id IN :sectionIds "
			+ "OR crdWrpAdd.cardWrapper.id IN :cardWrappersIds) "
			+ "ORDER BY act.timestamp DESC")
	List<Notification> findOfUserInSections(
			@Param("userId") UUID userId, 
			@Param("states") List<NotificationState> states, 
			@Param("sectionIds") List<UUID> sectionIds, 
			@Param("cardWrappersIds") List<UUID> cardWrappersIds, 
			Pageable page);	
	
	/* WARNING ALMOST REPEATED QUERY USED FOR COUNTING! */
	@Query("SELECT COUNT(notif) FROM Notification notif "
			+ "JOIN notif.activity act "
			+ "LEFT JOIN act.modelCardWrapperAddition crdWrpAdd "
			+ "LEFT JOIN act.modelSubsection subsec "
			+ "WHERE notif.subscriber.user.c1Id = :userId "
			+ "AND notif.inAppState = 'PENDING' "
			+ "AND (act.modelSection.id IN :sectionIds "
			+ "OR act.fromSection.id IN :sectionIds "
			+ "OR act.onSection.id IN :sectionIds "
			+ "OR act.modelCardWrapper.id IN :cardWrappersIds "
			+ "OR subsec.section.id IN :sectionIds "
			+ "OR subsec.parentSection.id IN :sectionIds "
			+ "OR crdWrpAdd.section.id IN :sectionIds "
			+ "OR crdWrpAdd.cardWrapper.id IN :cardWrappersIds)")
	Integer countOfUserInSections(
			@Param("userId") UUID userId, 
			@Param("sectionIds") List<UUID> sectionIds, 
			@Param("cardWrappersIds") List<UUID> cardWrappersIds);	
	
	@Query("SELECT notif FROM Notification notif "
            + "JOIN notif.activity act "
            + "LEFT JOIN act.modelCardWrapperAddition crdWrpAdd "
			+ "LEFT JOIN act.modelSubsection subsec "
            + "WHERE notif.subscriber.user.c1Id = :userId "
            + "AND notif.inAppState IN :states "
            + "AND (act.initiative.id IN :initsIds "
            + "OR act.modelSection.id IN :sectionIds "
			+ "OR act.fromSection.id IN :sectionIds "
			+ "OR act.onSection.id IN :sectionIds "
			+ "OR act.modelCardWrapper.id IN :cardWrappersIds "
			+ "OR subsec.section.id IN :sectionIds "
			+ "OR subsec.parentSection.id IN :sectionIds "
			+ "OR crdWrpAdd.section.id IN :sectionIds "
			+ "OR crdWrpAdd.cardWrapper.id IN :cardWrappersIds) "
			+ "ORDER BY act.timestamp DESC")
    List<Notification> findOfUserInInitiativesAndSection(
    		@Param("userId") UUID userId, 
    		@Param("states") List<NotificationState> states, 
    		@Param("initsIds") List<UUID> initiativeIds, 
    		@Param("sectionIds") List<UUID> sectionIds,
    		@Param("cardWrappersIds") List<UUID> cardWrappersIds, 
    		Pageable page);
	
	/* WARNING ALMOST REPEATED QUERY USED FOR COUNTING! */
	@Query("SELECT COUNT(notif) FROM Notification notif "
            + "JOIN notif.activity act "
            + "LEFT JOIN act.modelCardWrapperAddition crdWrpAdd "
			+ "LEFT JOIN act.modelSubsection subsec "
            + "WHERE notif.subscriber.user.c1Id = :userId "
            + "AND notif.inAppState = 'PENDING' "
            + "AND (act.initiative.id IN :initsIds "
            + "OR act.modelSection.id IN :sectionIds "
			+ "OR act.fromSection.id IN :sectionIds "
			+ "OR act.onSection.id IN :sectionIds "
			+ "OR act.modelCardWrapper.id IN :cardWrappersIds "
			+ "OR subsec.section.id IN :sectionIds "
			+ "OR subsec.parentSection.id IN :sectionIds "
			+ "OR crdWrpAdd.section.id IN :sectionIds "
			+ "OR crdWrpAdd.cardWrapper.id IN :cardWrappersIds)")
    Integer countOfUserInInitiativesAndSection(
    		@Param("userId") UUID userId, 
    		@Param("initsIds") List<UUID> initiativeIds, 
    		@Param("sectionIds") List<UUID> sectionIds,
    		@Param("cardWrappersIds") List<UUID> cardWrappersIds);
	
	List<Notification> findBySubscriber_User_C1IdAndInAppState(UUID userId, NotificationState notificationState);
	
	@Query("SELECT notif FROM Notification notif "
			+ "JOIN notif.subscriber subs "
			+ "WHERE notif.emailNowState = ?1 "
			+ "ORDER BY subs.user.c1Id")
	List<Notification> findByEmailNowStateOrderedByUser(NotificationState notificationState);
	
	@Query("SELECT notif FROM Notification notif "
			+ "JOIN notif.subscriber subs "
			+ "WHERE subs.emailSummaryPeriodConfig = ?1 "
			+ "AND notif.emailSummaryState < ?2 "
			+ "ORDER BY subs.user.c1Id")
	List<Notification> findByPeriodConfigAndEmailSummaryStateOrderedByUser(
			SubscriberEmailSummaryPeriodConfig config, 
			NotificationState notificationEmailState);
	
	@Modifying
	@Query("DELETE FROM Notification notif "
			+ "WHERE notif.creationDate < ?1")
	void deleteOlderThan(Timestamp date);
}
