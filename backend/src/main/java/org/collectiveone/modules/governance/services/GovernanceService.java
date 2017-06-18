package org.collectiveone.modules.governance.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.governance.dto.DecisionMakerDto;
import org.collectiveone.modules.governance.enums.DecisionMakerRole;
import org.collectiveone.modules.governance.enums.DecisionVerdict;
import org.collectiveone.modules.governance.enums.GovernanceType;
import org.collectiveone.modules.governance.model.DecisionMaker;
import org.collectiveone.modules.governance.model.Governance;
import org.collectiveone.modules.governance.repositories.DecisionMakerRepositoryIf;
import org.collectiveone.modules.governance.repositories.GovernanceRepositoryIf;
import org.collectiveone.modules.initiatives.model.Initiative;
import org.collectiveone.modules.users.repositories.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GovernanceService {

	@Autowired
	private GovernanceRepositoryIf governanceRepository;
	
	@Autowired
	private DecisionMakerRepositoryIf decisionMakerRepository;
	
	@Autowired
	AppUserRepositoryIf appUserRepository;
	
	
	@Transactional
	public Governance create(Initiative initiative) {

		Governance governance = new Governance();
		
		governance.setInitiative(initiative);
		governance.setType(GovernanceType.ROLES);
		
		return governanceRepository.save(governance);

	}
	
	@Transactional
	public DecisionVerdict createSubInitiative(UUID parentInitiativeId, UUID creatorId) {
		
		DecisionVerdict verdict = null;
		Governance governance = governanceRepository.findByInitiative_Id(parentInitiativeId);
		
		switch (governance.getType()) {
		case ROLES:
			if (isAdmin(governance.getId(), creatorId)) {
				verdict = DecisionVerdict.APPROVED;
			} else {
				verdict = DecisionVerdict.DENIED;
			}
			break;
		}
		
		return verdict;
	}
	
	private Boolean isAdmin(UUID governanceId, UUID userId) {
		DecisionMaker decisionMaker = decisionMakerRepository.findByGovernance_IdAndUser_C1Id(governanceId, userId);
		
		if (decisionMaker != null) {
			if (decisionMaker.getRole() == DecisionMakerRole.ADMIN) {
				return true;
			}
		}
		
		return false;
	}
	
	@Transactional
	public DecisionMaker getDecisionMaker(UUID governaceId, UUID userId) {
		return decisionMakerRepository.findByGovernance_IdAndUser_C1Id(governaceId, userId);
	}
	
	
	@Transactional
	public List<DecisionMakerDto> getDecisonMakers(UUID initiativeId) {
		Governance governance = governanceRepository.findByInitiative_Id(initiativeId);
		
		List<DecisionMakerDto> decisionMakerDtos = new ArrayList<DecisionMakerDto>();
		for (DecisionMaker decisionMaker : governance.getDecisionMakers()) {
			decisionMakerDtos.add(decisionMaker.toDto());
		}
		
		return decisionMakerDtos;
	}
	
	@Transactional
	public PostResult postDecisonMakers(UUID governanceId, UUID userId, DecisionMakerRole role) {
		DecisionMaker decisionMaker = addDecisionMaker(governanceId, userId, role);
		if (decisionMaker != null) {
			return new PostResult("success", "contributor added",  decisionMaker.getId().toString());
		} 
		
		return new PostResult("error", "decision maker not added", "");
	}
	
	@Transactional
	public DecisionMaker addDecisionMaker(UUID governanceId, UUID userId, DecisionMakerRole role) {
		Governance governance = governanceRepository.findById(governanceId);
		
		DecisionMaker decisionMaker = new DecisionMaker();
		decisionMaker.setGovernance(governance);
		decisionMaker.setUser(appUserRepository.findByC1Id(userId));
		decisionMaker.setRole(role);
		
		decisionMaker = decisionMakerRepository.save(decisionMaker);
		governance.getDecisionMakers().add(decisionMaker);
		
		governanceRepository.save(governance);
		
		return decisionMaker;
	}
	
	@Transactional
	public boolean deleteDecisionMaker(UUID governanceId, UUID userId) {
		DecisionMaker decisionMaker = decisionMakerRepository.findByGovernance_IdAndUser_C1Id(governanceId, userId);
		if(decisionMaker != null) {
			decisionMakerRepository.delete(decisionMaker);
		}
		
		return true;
	}
}
