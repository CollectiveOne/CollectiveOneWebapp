package org.collectiveone.modules.decisions.repositories;

import java.util.UUID;

import org.collectiveone.modules.decisions.model.DecisionMaker;
import org.collectiveone.modules.decisions.model.DecisionRealm;
import org.springframework.data.repository.CrudRepository;

public interface DecisionRealmRepositoryIf extends CrudRepository<DecisionRealm, UUID> {
	
	DecisionRealm findByInitiative_Id(UUID initiativeId);
	
	DecisionMaker findByIdAndDecisionMakers_User_C1Id(UUID realmId, UUID userId);
	
}
