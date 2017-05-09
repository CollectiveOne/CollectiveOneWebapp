package org.collectiveone.services;

import javax.transaction.Transactional;

import org.collectiveone.model.AppUser;
import org.collectiveone.model.Initiative;
import org.collectiveone.repositories.AppUserRepositoryIf;
import org.collectiveone.web.dto.NewInitiativeDto;
import org.collectiveone.web.dto.PostResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitiativeService {
	
	@Autowired
	AppUserRepositoryIf appUserRepository;

	@Transactional
	public PostResult create(String auth0Id, NewInitiativeDto initiativeDto) {
		
		AppUser creator = appUserRepository.findByAuth0Id(auth0Id);
		Initiative initiative = new Initiative();
		initiative.setCreator(creator);
		initiative.setDriver(initiativeDto.getDriver());
		initiative.setEnabled(true);
		initiative.setName(initiativeDto.getName());
		
		return new PostResult(true, "initiative created");
	}
	
}
