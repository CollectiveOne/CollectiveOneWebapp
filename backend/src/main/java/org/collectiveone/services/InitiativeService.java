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
import org.collectiveone.model.extensions.Contributor;
import org.collectiveone.model.extensions.ContributorTransfer;
import org.collectiveone.model.extensions.InitiativeRelationship;
import org.collectiveone.model.extensions.InitiativeTransfer;
import org.collectiveone.repositories.AppUserRepositoryIf;
import org.collectiveone.repositories.ContributorRepositoryIf;
import org.collectiveone.repositories.ContributorTransferRepositoryIf;
import org.collectiveone.repositories.InitiativeRelationshipRepositoryIf;
import org.collectiveone.repositories.InitiativeRepositoryIf;
import org.collectiveone.repositories.InitiativeTransferRepositoryIf;
import org.collectiveone.web.dto.AssetsDto;
import org.collectiveone.web.dto.AssignationDto;
import org.collectiveone.web.dto.BillDto;
import org.collectiveone.web.dto.ContributorDto;
import org.collectiveone.web.dto.GetResult;
import org.collectiveone.web.dto.InitiativeDto;
import org.collectiveone.web.dto.NewInitiativeDto;
import org.collectiveone.web.dto.PostResult;
import org.collectiveone.web.dto.ReceiverDto;
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
	
	@Autowired
	ContributorRepositoryIf contributorRepository;
	
	@Autowired
	InitiativeTransferRepositoryIf initiativeTransferRepository;
	
	@Autowired
	ContributorTransferRepositoryIf contributorTransferRepository;
	
	
	public PostResult init(UUID c1Id, NewInitiativeDto initiativeDto) {
		/* create the initiative */
		Initiative initiative = create(c1Id, initiativeDto);
		
		if (!initiativeDto.getAsSubinitiative()) {
			/* create a token for this initiative */
			TokenType token = tokenService.create(initiativeDto.getOwnTokens().getAssetName(), "T");
			initiative.setTokenType(token);
			initiative = initiativeRepository.save(initiative);
			tokenService.mintToHolder(token.getId(), initiative.getId(), initiativeDto.getOwnTokens().getOwnedByThisHolder(), TokenHolderType.INITIATIVE);
			return new PostResult("success", "initiative created and tokens created", initiative.getId().toString());
			
		} else {
			Initiative parent = initiativeRepository.findById(UUID.fromString(initiativeDto.getParentInitiativeId()));
			
			/* link to parent initiative */
			InitiativeRelationship relationship = new InitiativeRelationship();
			relationship.setInitiative(initiative);
			relationship.setType(InitiativeRelationshipType.IS_DETACHED_SUB);
			relationship.setOfInitiative(parent);
			
			relationship = initiativeRelationshipRepository.save(relationship);
			
			/* transfer parent assets to child */
			for (TransferDto thisTransfer : initiativeDto.getOtherAssetsTransfers()) {
				TokenType token = tokenService.getTokenType(UUID.fromString(thisTransfer.getAssetId()));
				
				tokenService.transfer(token.getId(), parent.getId(), initiative.getId(), thisTransfer.getValue(), TokenHolderType.INITIATIVE);
				
				/* upper layer keeping track of who transfered what to whom */
				InitiativeTransfer transfer = new InitiativeTransfer();
				transfer.setRelationship(relationship);
				transfer.setTokenType(token);
				transfer.setValue(thisTransfer.getValue());
				
				transfer = initiativeTransferRepository.save(transfer);
				relationship.getTokensTransfers().add(transfer);
			}
			
			initiative.getRelationships().add(relationship);
			initiativeRepository.save(initiative);
			
			return new PostResult("success", "sub initiative created and tokens transferred",  initiative.getId().toString());
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
			
			initiative = initiativeRepository.save(initiative);
			
			/* List of Contributors */
			for (ContributorDto contributorDto : initiativeDto.getContributors()) {
				Contributor contributor = new Contributor();
				contributor.setInitiative(initiative);
				contributor.setUser(appUserRepository.findByC1Id(UUID.fromString(contributorDto.getUser().getC1Id())));
				contributor.setRole(ContributorRole.valueOf(contributorDto.getRole()));
				
				contributor = contributorRepository.save(contributor);
				initiative.getContributors().add(contributor);
			}
			
			return initiative;
			
		} else {		
			return null;
		}
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
	
	@Transactional
	public List<ContributorDto> getContributors(UUID initiativeId) {
		Initiative initiative = initiativeRepository.findById(initiativeId); 
		
		List<ContributorDto> contributorsDtos = new ArrayList<ContributorDto>();
		for (Contributor contributor : initiative.getContributors()) {
			ContributorDto contributorDto = new ContributorDto();
			
			contributorDto.setId(contributor.getId().toString());
			contributorDto.setInitiativeId(initiative.getId().toString());
			contributorDto.setUser(contributor.getUser().toDto());
			contributorDto.setRole(contributor.getRole().toString());
		
			contributorsDtos.add(contributorDto);
		}
		
		return contributorsDtos;
	}
	
	@Transactional
	public PostResult postContributor(UUID initiativeId, UUID c1Id, ContributorRole role) {
		Contributor contributor = addContributor(initiativeId, c1Id, role);
		if (contributor != null) {
			return new PostResult("success", "contributor added",  contributor.getId().toString());
		} 
		
		return new PostResult("error", "contributor not added", "");
	}
	
	@Transactional
	public Contributor addContributor(UUID initiativeId, UUID c1Id, ContributorRole role) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
		
		Contributor contributor = new Contributor();
		contributor.setInitiative(initiative);
		contributor.setUser(appUserRepository.findByC1Id(c1Id));
		contributor.setRole(role);
		
		contributor = contributorRepository.save(contributor);
		initiative.getContributors().add(contributor);
		
		initiativeRepository.save(initiative);
		
		return contributor;
	}
	
	@Transactional
	public Contributor getOrAddContributor(UUID initiativeId, UUID userId) {
		
		Contributor contributor = contributorRepository.findByInitiative_IdAndUser_C1Id(initiativeId, userId);
		
		if(contributor == null) {
			contributor = addContributor(initiativeId, userId, ContributorRole.MEMBER);
		}
		
		return contributor;
	}
	
	@Transactional
	public PostResult deleteContributor(UUID c1Id, UUID contributorId) {
		Contributor contributor = contributorRepository.findById(contributorId);
		contributorRepository.delete(contributor);
		return new PostResult("success", "contributor added", "");
	}
	
	@Transactional
	public PostResult makeDirectAssignation(UUID initiativeId, AssignationDto assignation) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
		
		for(BillDto bill : assignation.getAssets()) {
			for(ReceiverDto receiver : assignation.getReceivers()) {
				TransferDto transfer = new TransferDto();
				
				transfer.setAssetId(bill.getAssetId());
				transfer.setAssetName(bill.getAssetName());
				transfer.setSenderId(initiative.getId().toString());
				transfer.setReceiverId(receiver.getUser().getC1Id());
				
				/* each asset type is distributed among the receviers using
				 * the same percentage*/
				transfer.setValue(bill.getValue() * (receiver.getPercent() / 100.0));
				
				transferToUser(initiative.getId(), transfer);
			}
		}
		return new PostResult("success", "success", "");
	}

	@Transactional
	public PostResult transferToUser(UUID initiativeId, TransferDto transfer) {
		AppUser receiver = appUserRepository.findByC1Id(UUID.fromString(transfer.getReceiverId()));
		TokenType tokenType = tokenService.getTokenType(UUID.fromString(transfer.getAssetId()));
		
		
		tokenService.transfer(
				tokenType.getId(), 
				UUID.fromString(transfer.getSenderId()), 
				receiver.getC1Id(), 
				transfer.getValue(), 
				TokenHolderType.USER);
		
		/* register the transfer to the contributor  */
		Contributor contributor = getOrAddContributor(initiativeId, receiver.getC1Id());
		
		ContributorTransfer thisTransfer = new ContributorTransfer();
		thisTransfer.setContributor(contributor);
		thisTransfer.setTokenType(tokenType);
		thisTransfer.setValue(transfer.getValue());
		
		contributorTransferRepository.save(thisTransfer);
		contributor.getTokensTransfers().add(thisTransfer);
		contributorRepository.save(contributor);
	
		return new PostResult("success", "assets transferred successfully", "");
	}
	
	/** Get the distribution of an asset starting from a given initiative
	 * and including the tokens transferred to its sub-initiatives and members */
	@Transactional
	public AssetsDto getTokenDistribution(UUID tokenId, UUID initiativeId) {

		Initiative initiative = initiativeRepository.findById(initiativeId); 
		AssetsDto assetDto = tokenService.getTokensOfHolderDto(tokenId, initiative.getId());
		assetDto.setHolderName(initiative.getName());
		
		assetDto.setTransferredToSubinitiatives(getTransferredToSubinitiatives(tokenId, initiative.getId()));
		assetDto.setTransferredToUsers(getTransferredToUsers(tokenId, initiative.getId()));
		
		return assetDto;
	}
	
	/** Recursively get the tokens transferred from one initiative into its sub-initiatives */
	@Transactional
	public List<TransferDto> getTransferredToSubinitiatives(UUID tokenId, UUID initiativeId) {
		Initiative initiative = initiativeRepository.findById(initiativeId); 
		TokenType token = tokenService.getTokenType(tokenId);

		/* get of sub-initiatives */
		List<InitiativeRelationship> subinitiativesRelationships = 
				initiativeRelationshipRepository.findByOfInitiativeIdAndType(initiative.getId(), InitiativeRelationshipType.IS_DETACHED_SUB);
		
		List<TransferDto> transferredToSubinitiatives = new ArrayList<TransferDto>();
		
		for (InitiativeRelationship relationship : subinitiativesRelationships) {
			/* get all transfers of a given token made from and to these initiatives */
			Double totalTransferred = initiativeTransferRepository.getTotalTransferred(tokenId, relationship.getId());
			
			TransferDto dto = new TransferDto();
			
			dto.setAssetId(token.getId().toString());
			dto.setAssetName(token.getName());
			dto.setSenderId(relationship.getOfInitiative().getId().toString());
			dto.setSenderName(relationship.getOfInitiative().getName());
			dto.setReceiverId(relationship.getInitiative().getId().toString());
			dto.setReceiverName(relationship.getInitiative().getName());
			dto.setValue(totalTransferred);
			
			transferredToSubinitiatives.add(dto);
		}
		
		return transferredToSubinitiatives;
	}
	
	/** Recursively get the tokens transferred from one initiative into its sub-initiatives */
	@Transactional
	public List<TransferDto> getTransferredToUsers(UUID tokenId, UUID initiativeId) {
		Initiative initiative = initiativeRepository.findById(initiativeId); 
		TokenType token = tokenService.getTokenType(tokenId);

		/* get of sub-initiatives */
		List<Contributor> subinitiativeContributors = 
				contributorRepository.findByInitiativeId(initiative.getId());
		
		List<TransferDto> transferredToUsers = new ArrayList<TransferDto>();
		
		for (Contributor contributor : subinitiativeContributors) {
			/* get all transfers of a given token made from an initiative to a contributor */
			double totalTransferred = contributorTransferRepository.getTotalTransferred(tokenId, contributor.getId());
			
			TransferDto dto = new TransferDto();
			
			dto.setAssetId(token.getId().toString());
			dto.setAssetName(token.getName());
			dto.setSenderId(initiative.getId().toString());
			dto.setSenderName(initiative.getName());
			dto.setReceiverId(contributor.getUser().getC1Id().toString());
			dto.setReceiverName(contributor.getUser().getNickname());
			dto.setValue(totalTransferred);
			
			transferredToUsers.add(dto);
		}
		
		return transferredToUsers;
	}

}
