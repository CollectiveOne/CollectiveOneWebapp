package org.collectiveone.modules.contexts.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.contexts.entities.Commit;
import org.collectiveone.modules.contexts.entities.StagedElement;
import org.collectiveone.modules.contexts.entities.enums.StageStatus;
import org.collectiveone.modules.contexts.entities.enums.StageType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CommitRepositoryIf extends CrudRepository<Commit, UUID> {

	@Query("SELECT stg FROM Commit comm "
			+ "JOIN comm.stagedElements stg "
			+ "WHERE comm.id = :commitId "
			+ "AND stg.type = :type")
	public List<StagedElement> getStagedElementsOfType(
			@Param("commitId") UUID commitId,
			@Param("type") StageType type); 
	
	default StagedElement getStageMetadata(UUID commitId) {
		List<StagedElement> stageElements = getStagedElementsOfType(commitId, StageType.METADATA);
		return stageElements.size() == 1 ? stageElements.get(0) : null;
	}
	
	@Query("SELECT subc.perspective.id FROM Commit comm "
			+ "JOIN comm.stagedElements stg "
			+ "JOIN stg.subcontext subc "
			+ "WHERE comm.id = :commitId "
			+ "AND stg.type = 'SUBCONTEXT'")
	public List<UUID> findSubperspectivesIds(
			@Param("commitId") UUID commitId);
	
	@Query("SELECT comm.author.id FROM Commit comm "
			+ "JOIN comm.stagedElements stg "
			+ "WHERE stg.id = :stagedElementId")
	public UUID findAuthorIdFromStagedElementId(@Param("stagedElementId") UUID stagedElementId);
	
	@Query("SELECT stg FROM Commit comm "
			+ "JOIN comm.stagedElements stg "
			+ "WHERE comm.id = :commitId "
			+ "AND stg.status = :status")
	public List<StagedElement> findStagedElementsWithStatus(
			@Param("commitId") UUID commitId, 
			@Param("status") StageStatus status);
	
	
}
