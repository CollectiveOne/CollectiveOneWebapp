package org.collectiveone.modules.contexts.repositories;

import java.util.UUID;

import org.collectiveone.modules.contexts.entities.Perspective;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PerspectiveRepositoryIf extends CrudRepository<Perspective, UUID> {

	@Query("SELECT persp FROM Perspective persp "
			+ "JOIN persp.context ctx "
			+ "WHERE ctx.id = :contextId")
	public Page<Perspective> findByContextId(@Param("contextId") UUID contextId, Pageable page);
	
	@Query("SELECT commit.id FROM Perspective persp "
			+ "JOIN persp.workingCommits commit "
			+ "WHERE persp.id = :perspId "
			+ "AND commit.author.id = :authorId")
	public UUID findWorkingCommitId(@Param("perspId") UUID perspectiveId, @Param("authorId") UUID authorId);
	
}
