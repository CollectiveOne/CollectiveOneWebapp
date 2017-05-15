package org.collectiveone.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.model.AppUser;
import org.collectiveone.model.Initiative;
import org.collectiveone.model.TokenType;
import org.collectiveone.repositories.AppUserRepositoryIf;
import org.collectiveone.repositories.InitiativeRepositoryIf;
import org.collectiveone.web.dto.AppUserWithRoleDto;
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
	TokenService tokenService;
	
	@Autowired
	AppUserRepositoryIf appUserRepository;
	
	@Autowired
	InitiativeRepositoryIf initiativeRepository;
	
	@Autowired
	

	public PostResult init(UUID c1Id, NewInitiativeDto initiativeDto) {
		/* create the initiative */
		Initiative initiative = create(c1Id, initiativeDto);
		
		TokenType token = null;
		if (initiativeDto.getParentInitiativeId() == null) {
			/* create a token for this initiative */
			token = tokenService.create(initiativeDto.getTokenName(), "T");
		} else {
			/* TODO
			Initiative parent = initiativeRepository.findById(UUID.fromString(initiativeDto.getParentInitiativeId()));
			token = parent.getTokenType();
			*/
		}
		
		tokenService.mintToInitiative(token.getId(), initiative.getId(), initiativeDto.getnTokens());
		
		return new PostResult("success", "initiative created and tokens assigned");
	}
	
	@Transactional
	public Initiative create(UUID c1Id, NewInitiativeDto initiativeDto) {
		
		AppUser creator = appUserRepository.findByC1Id(c1Id);
		
		if (creator != null) {
			Initiative initiative = new Initiative();
			
			/* Basic properties*/
			initiative.setName(initiativeDto.getName());
			initiative.setDriver(initiativeDto.getDriver());
			initiative.setCreator(creator);
			initiative.setCreationDate(new Timestamp(System.currentTimeMillis()));
			initiative.setEnabled(true);
			
			/* List of Contributors */
			for (AppUserWithRoleDto user : initiativeDto.getContributors()) {
				initiative.getContributors().add(appUserRepository.findByC1Id(UUID.fromString(user.getC1Id())));
			}
			
			return initiativeRepository.save(initiative);
			
		} else {		
			return null;
		}
	}
	
	public InitiativeDto get(UUID id) {
		return initiativeRepository.findById(id).toDto();
	}
	
	@Transactional
	public GetResult<List<InitiativeDto>> getOfUser(UUID userC1Id) {
		/* return the initiatives of a user but inserting initiatives that are 
		 * sub-initiatives as sub-elements */
		
		AppUser user = appUserRepository.findByC1Id(userC1Id);
		List<Initiative> initiatives = initiativeRepository.findOfContributor(user.getC1Id());
			
		List<InitiativeDto> initiativesDtos = new ArrayList<InitiativeDto>();
		for (Initiative initiative : initiatives) {
			initiativesDtos.add(initiative.toDto());
		}
		
		return new GetResult<List<InitiativeDto>>("success", "initiatives retrieved", initiativesDtos);
				
	}
	
	@Transactional
	public GetResult<List<InitiativeDto>> searchBy(String q) {
		List<Initiative> initiatives = initiativeRepository.searchBy(q);
		List<InitiativeDto> initiativesDtos = new ArrayList<InitiativeDto>();
		
		for(Initiative initiative : initiatives) {
			InitiativeDto dto = initiative.toDto();
			double remainingTokens = tokenService.getHolder(initiative.getTokenType().getId(), initiative.getId()).getTokens();
			dto.setRemainingTokens(remainingTokens);
			initiativesDtos.add(dto);
		}
		
		return new GetResult<List<InitiativeDto>>("succes", "initiatives returned", initiativesDtos);
		
	}
}
