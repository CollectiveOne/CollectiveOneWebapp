package org.collectiveone.modules.activity.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.activity.Activity;
import org.collectiveone.modules.activity.enums.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ActivityRepositoryIf extends CrudRepository<Activity, UUID> {
	
	Activity findTop1ByAssignation_IdAndTypeOrderByTimestampDesc(UUID assignationId, ActivityType type);
	
	/* CHECK https://docs.google.com/spreadsheets/d/11M405BZg7lJWr_G_qjeaAecCT8Q5wTQXLjuHfZyhjP0/edit#gid=0 */
	@Query("SELECT act FROM Activity act "
			+ "LEFT JOIN act.modelCardWrapperAddition crdWrpAdd "
			+ "LEFT JOIN act.modelSubsection subsec "
			+ "WHERE (act.modelSection.id IN :sectionIds "
			+ "OR act.modelCardWrapper.id IN :cardWrapperIds "
			+ "OR subsec.section.id IN :sectionIds "
			+ "OR subsec.parentSection.id IN :sectionIds "
			+ "OR act.onSection.id IN :sectionIds "
			+ "OR act.fromSection.id IN :sectionIds "
			+ "OR crdWrpAdd.section.id IN :sectionIds "
			+ "OR crdWrpAdd.cardWrapper.id IN :cardWrapperIds) "
			+ "AND ((:addMessages = TRUE AND act.type = :type) OR (:addEvents = TRUE AND act.type != :type)) "
			+ "ORDER BY act.timestamp DESC")
	Page<Activity> findOfSectionsOrCardsAndType(
			@Param("sectionIds") List<UUID> sectionIds, 
			@Param("cardWrapperIds") List<UUID> cardsWrappersIds, 
			@Param("type") ActivityType type,
			@Param("addMessages") Boolean addMessages,
			@Param("addEvents") Boolean addEvents, 	
			Pageable pageable);
	
	@Query("SELECT act FROM Activity act "
			+ "WHERE act.initiative.id IN ?1 "
			+ "ORDER BY act.timestamp DESC")
	Page<Activity> findOfInitiatives(List<UUID> initiativesId, Pageable pageable);
	
	@Query("SELECT act FROM Activity act "
			+ "WHERE act.initiative.id IN ?1 "
			+ "AND act.type = ?2 "
			+ "ORDER BY act.timestamp DESC")
	Page<Activity> findOfInitiativesAndType(List<UUID> initiativesId, ActivityType type, Pageable pageable);
	
	@Query("SELECT act FROM Activity act "
			+ "WHERE act.modelCardWrapper.id = ?1 "
			+ "AND act.type = ?2 ORDER BY act.timestamp DESC")
	List<Activity> findOfCard(UUID cardWrapperId, ActivityType type);	
	
	@Query("SELECT act FROM Activity act "
			+ "WHERE act.message.id = ?1 "
			+ "AND act.type = 'MESSAGE_POSTED'")
	Activity findOfMessagePosted(UUID messageId);	

}
