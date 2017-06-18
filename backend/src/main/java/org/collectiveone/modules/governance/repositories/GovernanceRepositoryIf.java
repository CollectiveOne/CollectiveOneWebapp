package org.collectiveone.modules.governance.repositories;

import java.util.UUID;

import org.collectiveone.modules.governance.model.DecisionMaker;
import org.collectiveone.modules.governance.model.Governance;
import org.springframework.data.repository.CrudRepository;

public interface GovernanceRepositoryIf extends CrudRepository<Governance, UUID> {
	
	Governance findById(UUID governanceId);
	
	Governance findByInitiative_Id(UUID initiativeId);
	
	DecisionMaker findByIdAndDecisionMakers_User_C1Id(UUID realmId, UUID userId);
	
}
