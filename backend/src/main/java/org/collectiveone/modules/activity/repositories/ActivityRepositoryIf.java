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
	
	/* if filterByType is TRUE. results will be filtered based on activity type
	 * 	if includeExcludeType is TRUE, results with activity type == type will be included
	 * 	if includeExcludeType is FALSE, results with activity type != type will be included.
	 * If filterByType is FALSE, all results, independently of the type, will be included */
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
			+ "AND (((:filterByType = FALSE OR act.type  = :type) AND (:filterByType = FALSE OR :includeExcludeType = TRUE)) "
			+ "OR   ((:filterByType = FALSE OR act.type != :type) AND (:filterByType = FALSE OR :includeExcludeType = FALSE))) "
			+ "ORDER BY act.timestamp DESC")
	Page<Activity> findOfSectionsOrCardsAndType(
			@Param("sectionIds") List<UUID> sectionIds, 
			@Param("cardWrapperIds") List<UUID> cardsWrappersIds, 
			@Param("filterByType") Boolean filterByType, 
			@Param("type") ActivityType type, 
			@Param("includeExcludeType") Boolean inlcludeExcludeTypeFlag, 
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

}
