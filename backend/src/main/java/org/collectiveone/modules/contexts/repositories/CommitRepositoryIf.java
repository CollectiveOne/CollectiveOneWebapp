package org.collectiveone.modules.contexts.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.contexts.entities.Commit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CommitRepositoryIf extends CrudRepository<Commit, UUID> {

	public Commit findById(UUID id);
	
	@Query("SELECT subc.perspective.id FROM Commit com "
			+ "JOIN com.subcontextStaged stagedSubc "
			+ "JOIN stagedSubc.subcontext subc "
			+ "WHERE com.id = :commitId ")
	public List<UUID> findSubperspectivesIds(@Param("commitId") UUID commitId);
	
}
