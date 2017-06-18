package org.collectiveone.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.model.basic.DecisionRealm;
import org.collectiveone.model.enums.DecisionMakerRole;
import org.collectiveone.model.enums.DecisionVerdict;
import org.collectiveone.model.support.DecisionMaker;
import org.collectiveone.repositories.AppUserRepositoryIf;
import org.collectiveone.repositories.DecisionMakerRepositoryIf;
import org.collectiveone.repositories.DecisionRealmRepositoryIf;
import org.collectiveone.web.dto.DecisionMakerDto;
import org.collectiveone.web.dto.PostResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DecisionService {

	@Autowired
	private DecisionRealmRepositoryIf decisionRealmRepository;
	
	@Autowired
	private DecisionMakerRepositoryIf decisionMakerRepository;
	
	@Autowired
	AppUserRepositoryIf appUserRepository;
	
	
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
	
	@Transactional
	public List<DecisionMakerDto> getDecisonMakers(UUID initiativeId) {
		DecisionRealm realm = decisionRealmRepository.findByInitiative_Id(initiativeId);
		
		List<DecisionMakerDto> decisionMakerDtos = new ArrayList<DecisionMakerDto>();
		for (DecisionMaker decisionMaker : realm.getDecisionMakers()) {
			decisionMakerDtos.add(decisionMaker.toDto());
		}
		
		return decisionMakerDtos;
	}
	
	@Transactional
	public PostResult postDecisonMakers(UUID initiativeId, UUID c1Id, DecisionMakerRole role) {
		DecisionMaker decisionMaker = addDecisionMaker(initiativeId, c1Id, role);
		if (decisionMaker != null) {
			return new PostResult("success", "contributor added",  decisionMaker.getId().toString());
		} 
		
		return new PostResult("error", "decision maker not added", "");
	}
	
	@Transactional
	public DecisionMaker addDecisionMaker(UUID initiativeId, UUID c1Id, DecisionMakerRole role) {
		DecisionRealm realm = decisionRealmRepository.findByInitiative_Id(initiativeId);
		
		DecisionMaker decisionMaker = new DecisionMaker();
		decisionMaker.setRealm(realm);
		decisionMaker.setUser(appUserRepository.findByC1Id(c1Id));
		decisionMaker.setRole(role);
		
		decisionMaker = decisionMakerRepository.save(decisionMaker);
		realm.getDecisionMakers().add(decisionMaker);
		
		decisionRealmRepository.save(realm);
		
		return decisionMaker;
	}
}
