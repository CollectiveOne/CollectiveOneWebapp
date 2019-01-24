package org.collectiveone.modules.contexts.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.contexts.entitiesRedundant.PerspectiveCache;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PerspectiveCacheRepositoryIf extends CrudRepository<PerspectiveCache, UUID> {

	public PerspectiveCache findByPerspectiveId(UUID id);
	
	@Query("SELECT subc.perspective.id "
			+ "FROM PerspectiveCache perspCache "
			+ "JOIN perspCache.subcontexts subc "
			+ "WHERE perspCache.perspective.id = :onPerspectiveId")
	public List<UUID> findSubperspectivesIds(
			@Param("onPerspectiveId") UUID onPerspectiveId);
	
	
}
