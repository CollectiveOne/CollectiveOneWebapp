package org.collectiveone.modules.activity.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.activity.Activity;
import org.collectiveone.modules.activity.enums.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ActivityRepositoryIf extends CrudRepository<Activity, UUID> {
	
	Activity findTop1ByAssignation_IdAndTypeOrderByTimestampDesc(UUID assignationId, ActivityType type);
	
	@Query("SELECT act FROM Activity act WHERE act.modelSection.id IN ?1 OR act.modelCardWrapper.id IN ?2 ORDER BY act.timestamp DESC")
	Page<Activity> findOfSectionsAndCards(List<UUID> sectionIds, List<UUID> cardsWrappersIds, Pageable pageable);
	
	@Query("SELECT act FROM Activity act WHERE (act.modelSection.id IN ?1 OR act.modelCardWrapper.id IN ?2) AND (act.type = ?3) ORDER BY act.timestamp DESC")
	Page<Activity> findOfSectionsAndCardsAndType(List<UUID> sectionIds, List<UUID> cardsWrappersIds, ActivityType type, Pageable pageable);
	
	
	@Query("SELECT act FROM Activity act WHERE act.modelSection.id IN ?1 ORDER BY act.timestamp DESC")
	Page<Activity> findOfSections(List<UUID> sectionIds, Pageable pageable);
	
	@Query("SELECT act FROM Activity act WHERE act.modelSection.id IN ?1 AND act.type = ?2 ORDER BY act.timestamp DESC")
	Page<Activity> findOfSectionsAndType(List<UUID> sectionIds, ActivityType type, Pageable pageable);
	
	
	@Query("SELECT act FROM Activity act WHERE act.modelCardWrapper.id = ?1 ORDER BY act.timestamp DESC")
	Page<Activity> findOfCard(UUID cardsWrappersId, Pageable pageable);
	
	@Query("SELECT act FROM Activity act WHERE act.modelCardWrapper.id = ?1 AND act.type = ?2 ORDER BY act.timestamp DESC")
	Page<Activity> findOfCardAndType(UUID cardsWrappersId, ActivityType type, Pageable pageable);
	
	
	@Query("SELECT act FROM Activity act WHERE act.initiative.id IN ?1 ORDER BY act.timestamp DESC")
	Page<Activity> findOfInitiatives(List<UUID> initiativesId, Pageable pageable);
	
	@Query("SELECT act FROM Activity act WHERE act.initiative.id IN ?1 AND act.type = ?2 ORDER BY act.timestamp DESC")
	Page<Activity> findOfInitiativesAndType(List<UUID> initiativesId, ActivityType type, Pageable pageable);
	
	@Query("SELECT act FROM Activity act WHERE act.modelCardWrapper.id = ?1 AND act.type = ?2 ORDER BY act.timestamp DESC")
	List<Activity> findOfCard(UUID cardWrapperId, ActivityType type);	
	
}
