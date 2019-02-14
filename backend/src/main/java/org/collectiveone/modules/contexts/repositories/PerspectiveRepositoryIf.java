package org.collectiveone.modules.contexts.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.contexts.entities.Commit;
import org.collectiveone.modules.contexts.entities.Perspective;
import org.collectiveone.modules.contexts.entities.StagedElement;
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
	
	@Query("SELECT commit FROM Perspective persp "
			+ "JOIN persp.workingCommits commit "
			+ "WHERE persp.id = :perspId "
			+ "AND commit.author.id = :authorId")
	public Commit findWorkingCommit(@Param("perspId") UUID perspectiveId, @Param("authorId") UUID authorId);
	
	@Query("SELECT commit.stagedElements FROM Perspective persp "
			+ "JOIN persp.workingCommits commit "
			+ "WHERE persp.id = :perspId "
			+ "AND commit.author.id = :authorId")
	public List<StagedElement> findStagedElements(@Param("perspId") UUID perspectiveId, @Param("authorId") UUID authorId);
	
	@Query("SELECT persp.head FROM Perspective persp "
			+ "WHERE persp.id = :perspId")
	public Commit findHead(@Param("perspId") UUID perspectiveId);
}
