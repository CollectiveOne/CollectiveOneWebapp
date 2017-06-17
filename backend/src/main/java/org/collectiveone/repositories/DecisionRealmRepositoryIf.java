package org.collectiveone.repositories;

import java.util.UUID;

import org.collectiveone.model.basic.DecisionRealm;
import org.collectiveone.model.support.DecisionMaker;
import org.springframework.data.repository.CrudRepository;

public interface DecisionRealmRepositoryIf extends CrudRepository<DecisionRealm, UUID> {
	
	DecisionRealm findByInitiative_Id(UUID initiativeId);
	
	DecisionMaker findByIdAndDecisionMakers_User_C1Id(UUID realmId, UUID userId);
	
}
