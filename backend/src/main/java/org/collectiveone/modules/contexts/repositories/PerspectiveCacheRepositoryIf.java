package org.collectiveone.modules.contexts.repositories;

import java.util.UUID;

import org.collectiveone.modules.contexts.entitiesRedundant.PerspectiveCache;
import org.springframework.data.repository.CrudRepository;

public interface PerspectiveCacheRepositoryIf extends CrudRepository<PerspectiveCache, UUID> {

	public PerspectiveCache findByPerspectiveId(UUID id);
	
	
}
