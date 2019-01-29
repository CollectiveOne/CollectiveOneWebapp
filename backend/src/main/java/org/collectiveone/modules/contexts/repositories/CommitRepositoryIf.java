package org.collectiveone.modules.contexts.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.contexts.entities.Commit;
import org.collectiveone.modules.contexts.entities.StageElement;
import org.collectiveone.modules.contexts.entities.StageType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CommitRepositoryIf extends CrudRepository<Commit, UUID> {

	@Query("SELECT stg FROM Commit comm "
			+ "JOIN comm.elementsStaged stg "
			+ "WHERE comm.id = :commitId "
			+ "AND stg.type = :type")
	public List<StageElement> getStageElementsOfType(
			@Param("commitId") UUID commitId,
			@Param("type") StageType type); 
	
	default StageElement getStageMetadata(UUID commitId) {
		List<StageElement> stageElements = getStageElementsOfType(commitId, StageType.METADATA);
		return stageElements.size() == 1 ? stageElements.get(0) : null;
	}
	
	@Query("SELECT subc.perspective.id FROM Commit comm "
			+ "JOIN comm.elementsStaged stg "
			+ "JOIN stg.subcontext subc "
			+ "WHERE comm.id = :commitId "
			+ "AND stg.type = 'SUBCONTEXT'")
	public List<UUID> findSubperspectivesIds(
			@Param("commitId") UUID commitId);
	
	
}
