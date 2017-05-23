package org.collectiveone.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.model.basic.AppUser;
import org.collectiveone.model.basic.Initiative;
import org.collectiveone.model.basic.TokenType;
import org.collectiveone.model.enums.ContributorRole;
import org.collectiveone.model.enums.InitiativeRelationshipType;
import org.collectiveone.model.enums.TokenHolderType;
import org.collectiveone.model.extensions.InitiativeContributor;
import org.collectiveone.model.extensions.InitiativeRelationship;
import org.collectiveone.repositories.AppUserRepositoryIf;
import org.collectiveone.repositories.InitiativeRelationshipRepositoryIf;
import org.collectiveone.repositories.InitiativeRepositoryIf;
import org.collectiveone.web.dto.AppUserWithRoleDto;
import org.collectiveone.web.dto.AssetsDto;
import org.collectiveone.web.dto.GetResult;
import org.collectiveone.web.dto.InitiativeDto;
import org.collectiveone.web.dto.NewInitiativeDto;
import org.collectiveone.web.dto.PostResult;
import org.collectiveone.web.dto.TransferDto;
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
		
		if (!initiativeDto.getAsSubinitiative()) {
			/* create a token for this initiative */
			TokenType token = tokenService.create(initiativeDto.getOwnTokens().getAssetName(), "T");
			initiative.setTokenType(token);
			initiative = initiativeRepository.save(initiative);
			tokenService.mintToHolder(token.getId(), initiative.getId(), initiativeDto.getOwnTokens().getOwnedByThisHolder(), TokenHolderType.INITIATIVE);
			return new PostResult("success", "initiative created and tokens created");
			
		} else {
			Initiative parent = initiativeRepository.findById(UUID.fromString(initiativeDto.getParentInitiativeId()));
			
			/* link to parent initiative */
			link(initiative.getId(), parent.getId(), InitiativeRelationshipType.IS_DETACHED_SUB);
			
			/* transfer parent assets to child */
			
			for (TransferDto thisTransfer : initiativeDto.getOtherAssetsTransfers()) {
				tokenService.transfer(UUID.fromString(thisTransfer.getAssetId()), parent.getId(), initiative.getId(), thisTransfer.getValue(), TokenHolderType.INITIATIVE);
			}
			
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
				InitiativeContributor contributor = new InitiativeContributor();
				contributor.setUser(appUserRepository.findByC1Id(UUID.fromString(user.getC1Id())));
				contributor.setRole(ContributorRole.ADMIN);
				
				initiative.getContributors().add(contributor);
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
	
	/** Get key data from an initiative: id, name and driver but no
	 * data from its assets */
	@Transactional
	public InitiativeDto getLight(UUID id) {
		return  initiativeRepository.findById(id).toDto();
	}
	
	/** Get the light data by calling getLight, and then add data
	 * about the assets held by an initiative */
	@Transactional
	public InitiativeDto getWithOwnAssets(UUID id) {
		Initiative initiative = initiativeRepository.findById(id); 
		InitiativeDto initiativeDto = getLight(initiative.getId());

		/* set own tokens data */
		if(initiative.getTokenType() != null) {
			AssetsDto ownTokens = tokenService.getTokensOfHolderDto(initiative.getTokenType().getId(), initiative.getId());
			ownTokens.setHolderName(initiative.getName());
			initiativeDto.setOwnTokens(ownTokens);
		}
		
		/* set other assets data */
		List<TokenType> otherTokens = null;
		if(initiative.getTokenType() != null) {
			otherTokens = tokenService.getTokenTypesHeldByOtherThan(initiative.getId(), initiative.getTokenType().getId());
		} else {
			otherTokens = tokenService.getTokenTypesHeldBy(initiative.getId());
		}
		
		for (TokenType otherToken : otherTokens) {
			AssetsDto otherAsset = tokenService.getTokensOfHolderDto(otherToken.getId(), initiative.getId());
			otherAsset.setHolderName(initiative.getName());
			initiativeDto.getOtherAssets().add(otherAsset);
		}
		
		return initiativeDto;
	}
	
	

	/** Get the distribution of an asset starting from a given initiative
	 * and going though all its sub-initiatives recursively */
	@Transactional
	public AssetsDto getTokenDistribution(UUID tokenId, UUID initiativeId) {
		Initiative initiative = initiativeRepository.findById(initiativeId); 
		
		AssetsDto assetDto = tokenService.getTokensOfHolderDto(tokenId, initiative.getId());
		assetDto.setHolderName(initiative.getName());
			
		/* get of sub-initiatives */
		List<Initiative> subinitiatives = initiativeRepository.findInitiativesWithRelationship(initiative.getId(), InitiativeRelationshipType.IS_DETACHED_SUB);
		for (Initiative subinitiative : subinitiatives) {
			/* recursively call on all sub-initiatives */
			AssetsDto subInitiativeAssetsDto = getTokenDistribution(tokenId, subinitiative.getId());
			subInitiativeAssetsDto.setHolderName(subinitiative.getName());
			
			assetDto.getOwnedBySubinitiatives().add(subInitiativeAssetsDto);
		}
		
		return assetDto;
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
