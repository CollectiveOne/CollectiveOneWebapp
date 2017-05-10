package org.collectiveone.services;

import javax.transaction.Transactional;

import org.collectiveone.model.AppUser;
import org.collectiveone.model.Initiative;
import org.collectiveone.repositories.InitiativeRepositoryIf;
import org.collectiveone.web.dto.NewInitiativeDto;
import org.collectiveone.web.dto.PostResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitiativeService {
	
	@Autowired
	AppUserService appUserService;
	
	@Autowired
	InitiativeRepositoryIf initiativeRepository;

	@Transactional
	public PostResult create(String auth0Id, NewInitiativeDto initiativeDto) {
		
		AppUser creator = appUserService.get(auth0Id);
		
		if (creator != null) {
			Initiative initiative = new Initiative();
			initiative.setCreator(creator);
			initiative.setDriver(initiativeDto.getDriver());
			initiative.setEnabled(true);
			initiative.setName(initiativeDto.getName());
			
			initiativeRepository.save(initiative);

			return new PostResult("success", "initiative created");
		} else {		
			return new PostResult("error", "user not found");
		}
	}
	
}
