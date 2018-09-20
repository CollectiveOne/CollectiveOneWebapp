package org.collectiveone.modules.contexts.repositories;

import java.util.UUID;

import org.collectiveone.modules.contexts.Commit;
import org.collectiveone.modules.contexts.Trail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TrailRepositoryIf extends CrudRepository<Trail, UUID> {

	public Trail findById(UUID id);
	
	@Query("SELECT commit FROM Trail trail "
			+ "JOIN trail.workingCommits commit "
			+ "WHERE trail.id = :trailId "
			+ "AND commit.author.c1Id = :authorId")
	public Commit findWorkingCommit(@Param("trailId") UUID trailId, @Param("authorId") UUID authorId);
	
	
}
