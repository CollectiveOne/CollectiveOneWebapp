package org.collectiveone.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.collectiveone.model.AppUser;
import org.collectiveone.model.Initiative;
import org.collectiveone.repositories.InitiativeRepositoryIf;
import org.collectiveone.web.dto.GetResult;
import org.collectiveone.web.dto.InitiativeDto;
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
	
	@Transactional
	public GetResult<List<InitiativeDto>> getOfUser(String auth0Id) {
		/* return the initiatives of a user but inserting initiatives that are 
		 * sub-initiatives as sub-elements */
		
		AppUser user = appUserService.get(auth0Id);
		List<Initiative> initiatives = initiativeRepository.findOfContributor(user.getC1Id());
			
		List<InitiativeDto> initiativesDtos = new ArrayList<InitiativeDto>();
		for (Initiative initiative : initiatives) {
			initiativesDtos.add(initiative.toDto());
		}
		
		return new GetResult<List<InitiativeDto>>("success", "initiatives retrieved", initiativesDtos);
				
	}
	
	
}
