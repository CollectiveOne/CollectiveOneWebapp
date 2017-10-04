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
	
	@Query("SELECT act FROM Activity act WHERE act.modelView.id = ?1 OR act.modelSection.id IN ?2 OR act.modelCardWrapper.id IN ?3 ORDER BY act.timestamp DESC")
	Page<Activity> findAllOfViewSectionsAndCards(UUID viewId, List<UUID> sectionIds, List<UUID> cardsWrappersIds, Pageable pageable);
	
}
