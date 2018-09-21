package org.collectiveone.modules.contexts.repositories;

import java.util.UUID;

import org.collectiveone.modules.contexts.Commit;
import org.collectiveone.modules.contexts.Perspective;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PerspectiveRepositoryIf extends CrudRepository<Perspective, UUID> {

	public Perspective findById(UUID id);
	
	@Query("SELECT commit FROM Perspective persp "
			+ "JOIN persp.workingCommits commit "
			+ "WHERE persp.id = :perspId "
			+ "AND commit.author.c1Id = :authorId")
	public Commit findWorkingCommit(@Param("perspId") UUID trailId, @Param("authorId") UUID authorId);
	
	
}
