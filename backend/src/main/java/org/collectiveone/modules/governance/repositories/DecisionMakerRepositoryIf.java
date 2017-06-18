package org.collectiveone.modules.governance.repositories;

import java.util.UUID;

import org.collectiveone.modules.governance.model.DecisionMaker;
import org.springframework.data.repository.CrudRepository;

public interface DecisionMakerRepositoryIf extends CrudRepository<DecisionMaker, UUID> {
	
	DecisionMaker findByGovernance_IdAndUser_C1Id(UUID governanceId, UUID userId);
}
