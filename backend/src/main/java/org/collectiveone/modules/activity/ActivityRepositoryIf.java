package org.collectiveone.modules.activity;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface ActivityRepositoryIf extends CrudRepository<Activity, UUID> {
	
	Activity findTop1ByAssignation_IdAndTypeOrderByTimestampDesc(UUID assignationId, ActivityType type);
	
}
