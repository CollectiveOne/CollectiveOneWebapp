package org.collectiveone.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.model.AppUser;
import org.collectiveone.model.Initiative;
import org.collectiveone.model.InitiativeRelationship;
import org.collectiveone.model.TokenType;
import org.collectiveone.model.enums.InitiativeRelationshipType;
import org.collectiveone.model.enums.TokenHolderType;
import org.collectiveone.repositories.AppUserRepositoryIf;
import org.collectiveone.repositories.InitiativeRelationshipRepositoryIf;
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
	InitiativeRelationshipRepositoryIf initiativeRelationshipRepository;
	
	
	public PostResult init(UUID c1Id, NewInitiativeDto initiativeDto) {
		/* create the initiative */
		Initiative initiative = create(c1Id, initiativeDto);
		
		TokenType token = null;
		if (initiativeDto.getParentInitiativeId() == null) {
			/* create a token for this initiative */
			token = tokenService.create(initiativeDto.getTokenName(), "T");
			initiative.setTokenType(token);
			initiative = initiativeRepository.save(initiative);
			tokenService.mintToHolder(token.getId(), initiative.getId(), initiativeDto.getTokens(), TokenHolderType.INITIATIVE);
			return new PostResult("success", "initiative created and tokens created");
			
		} else {
			Initiative parent = initiativeRepository.findById(UUID.fromString(initiativeDto.getParentInitiativeId()));
			
			/* link to parent initiative */
			link(initiative.getId(), parent.getId(), InitiativeRelationshipType.IS_DETACHED_SUB);
			
			/* transfer tokens from parent to child */
			token = parent.getTokenType();
			tokenService.transfer(token.getId(), parent.getId(), initiative.getId(), initiativeDto.getTokens(), TokenHolderType.INITIATIVE);
			return new PostResult("success", "sub initiative created and tokens transferred");
		}
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
	
	@Transactional
	public String link(UUID initaitiveId, UUID otherInitaitiveId, InitiativeRelationshipType type) {
		Initiative initiative = initiativeRepository.findById(initaitiveId); 
		Initiative otherInitaitive = initiativeRepository.findById(otherInitaitiveId); 
		
		InitiativeRelationship relationship = new InitiativeRelationship();
		relationship.setInitiative(initiative);
		relationship.setType(type);
		relationship.setOfInitiative(otherInitaitive);
		relationship = initiativeRelationshipRepository.save(relationship);
		
		initiative.getRelationships().add(relationship);
		initiativeRepository.save(initiative);
		
		return "success";
	}
	
	public InitiativeDto get(UUID id) {
		Initiative initiative = initiativeRepository.findById(id); 
		
		InitiativeDto initiativeDto = initiative.toDto();
		
		if(initiative.getTokenType() != null) {
			double existingTokens = tokenService.getTotalExisting(initiative.getTokenType().getId());
			initiativeDto.setTotalExistingTokens(existingTokens);
			
			double remainingTokens = tokenService.getHolder(initiative.getTokenType().getId(), initiative.getId()).getTokens();
			initiativeDto.setRemainingTokens(remainingTokens);
			
			double tokensAssignedToInitiatives = tokenService.getTotalAssignedToOtherInitiatives(initiative.getTokenType().getId(), initiative.getId());
			initiativeDto.setTotalAssignedToOtherInitiatives(tokensAssignedToInitiatives);
			
			double tokensAssignedToUsers = tokenService.getTotalAssignedToUsers(initiative.getTokenType().getId());
		initiativeDto.setTotalAssignedToUsers(tokensAssignedToUsers);
		}
		
		return initiativeDto;
	}
	
	@Transactional
	public GetResult<List<InitiativeDto>> getOfUser(UUID userC1Id) {
		/* return the initiatives of a user but inserting initiatives that are 
		 * sub-initiatives as sub-elements */
		
		AppUser user = appUserRepository.findByC1Id(userC1Id);
		List<Initiative> superInitiatives = initiativeRepository.findSuperInitiativesOfContributor(user.getC1Id());
		
		/* get all initiatives in which the user is a contributor */
		List<InitiativeDto> initiativesDtos = new ArrayList<InitiativeDto>();
		for (Initiative initiative : superInitiatives) {
			InitiativeDto dto = initiative.toDto();
			
			/* look for the full sub-initiative tree of each super initiative */
			List<InitiativeDto> subInitiatives = getSubinitiativesTree(initiative.getId());
			dto.setSubInitiatives(subInitiatives);
			
			initiativesDtos.add(dto);
		}
		
		return new GetResult<List<InitiativeDto>>("success", "initiatives retrieved", initiativesDtos);
				
	}
	
	@Transactional
	public List<InitiativeDto> getSubinitiativesTree(UUID initiativeId) {
		Initiative initiative = initiativeRepository.findById(initiativeId); 
		List<Initiative> subIniatiatives = initiativeRepository.findInitiativesWithRelationship(initiative.getId(), InitiativeRelationshipType.IS_DETACHED_SUB);
		
		List<InitiativeDto> subinitiativeDtos = new ArrayList<InitiativeDto>();
		
		for (Initiative subinitiative : subIniatiatives) {
			InitiativeDto subinitiativeDto = subinitiative.toDto();
			
			/* recursively call this for its own sub-initiatives */
			List<InitiativeDto> subsubIniatiativesDto = getSubinitiativesTree(subinitiative.getId());
			subinitiativeDto.setSubInitiatives(subsubIniatiativesDto);
			
			subinitiativeDtos.add(subinitiativeDto);
		}
		
		return subinitiativeDtos;
	}
	
	@Transactional
	public GetResult<List<InitiativeDto>> searchBy(String q) {
		List<Initiative> initiatives = initiativeRepository.searchBy(q);
		List<InitiativeDto> initiativesDtos = new ArrayList<InitiativeDto>();
		
		for(Initiative initiative : initiatives) {
			initiativesDtos.add(initiative.toDto());
		}
		
		return new GetResult<List<InitiativeDto>>("succes", "initiatives returned", initiativesDtos);
		
	}
}
