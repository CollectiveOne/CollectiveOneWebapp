package org.collectiveone.modules.governance;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface DecisionMakerRepositoryIf extends CrudRepository<DecisionMaker, UUID> {
	
	DecisionMaker findByGovernance_IdAndUser_C1Id(UUID governanceId, UUID userId);
	
	List<DecisionMaker> findByGovernance_IdAndRole(UUID governanceId, DecisionMakerRole role);
	
}
