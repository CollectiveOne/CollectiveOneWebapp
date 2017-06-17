package org.collectiveone.services;

import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.model.basic.DecisionRealm;
import org.collectiveone.model.enums.DecisionMakerRole;
import org.collectiveone.model.enums.DecisionVerdict;
import org.collectiveone.model.support.DecisionMaker;
import org.collectiveone.repositories.DecisionRealmRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DecisionService {

	@Autowired
	private DecisionRealmRepositoryIf decisionRealmRepository;
	
	
	@Transactional
	public DecisionVerdict createSubInitiative(UUID parentInitiativeId, UUID creatorId) {
		
		DecisionVerdict verdict = null;
		DecisionRealm realm = decisionRealmRepository.findByInitiative_Id(parentInitiativeId);
		
		switch (realm.getType()) {
		case ROLES:
			if (isAdmin(realm.getId(), creatorId)) {
				verdict = DecisionVerdict.APPROVED;
			} else {
				verdict = DecisionVerdict.DENIED;
			}
			break;
		}
		
		return verdict;
	}
	
	private Boolean isAdmin(UUID realmId, UUID userId) {
		DecisionMaker decisionMaker = decisionRealmRepository.findByIdAndDecisionMakers_User_C1Id(realmId, userId);
		
		if (decisionMaker != null) {
			if (decisionMaker.getRole() == DecisionMakerRole.ADMIN) {
				return true;
			}
		}
		
		return false;
	}
	
	
	
}
