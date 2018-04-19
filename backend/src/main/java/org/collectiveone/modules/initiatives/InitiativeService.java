package org.collectiveone.modules.initiatives;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.activity.Activity;
import org.collectiveone.modules.activity.ActivityService;
import org.collectiveone.modules.activity.WantToContributeNotification;
import org.collectiveone.modules.activity.dto.ActivityDto;
import org.collectiveone.modules.activity.enums.ActivityType;
import org.collectiveone.modules.activity.enums.NotificationState;
import org.collectiveone.modules.activity.enums.SubscriptionElementType;
import org.collectiveone.modules.activity.repositories.ActivityRepositoryIf;
import org.collectiveone.modules.activity.repositories.WantToContributeRepositoryIf;
import org.collectiveone.modules.files.FileStored;
import org.collectiveone.modules.files.FileStoredRepositoryIf;
import org.collectiveone.modules.governance.DecisionMaker;
import org.collectiveone.modules.governance.DecisionMakerRepositoryIf;
import org.collectiveone.modules.governance.DecisionMakerRole;
import org.collectiveone.modules.governance.Governance;
import org.collectiveone.modules.governance.GovernanceService;
import org.collectiveone.modules.initiatives.dto.InitiativeDto;
import org.collectiveone.modules.initiatives.dto.InitiativeMembersDto;
import org.collectiveone.modules.initiatives.dto.InitiativeTagDto;
import org.collectiveone.modules.initiatives.dto.MemberDto;
import org.collectiveone.modules.initiatives.dto.NewInitiativeDto;
import org.collectiveone.modules.initiatives.dto.SearchFiltersDto;
import org.collectiveone.modules.initiatives.repositories.InitiativeMetaRepositoryIf;
import org.collectiveone.modules.initiatives.repositories.InitiativeRelationshipRepositoryIf;
import org.collectiveone.modules.initiatives.repositories.InitiativeRepositoryIf;
import org.collectiveone.modules.initiatives.repositories.InitiativeTagRepositoryIf;
import org.collectiveone.modules.initiatives.repositories.MemberRepositoryIf;
import org.collectiveone.modules.model.ModelSection;
import org.collectiveone.modules.model.repositories.ModelSectionRepositoryIf;
import org.collectiveone.modules.tokens.InitiativeTransfer;
import org.collectiveone.modules.tokens.TokenMint;
import org.collectiveone.modules.tokens.TokenService;
import org.collectiveone.modules.tokens.TokenTransferService;
import org.collectiveone.modules.tokens.TokenType;
import org.collectiveone.modules.tokens.dto.AssetsDto;
import org.collectiveone.modules.tokens.dto.TransferDto;
import org.collectiveone.modules.tokens.enums.TokenHolderType;
import org.collectiveone.modules.tokens.repositories.InitiativeTransferRepositoryIf;
import org.collectiveone.modules.tokens.repositories.TokenMintRepositoryIf;
import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class InitiativeService {
	
	// TODO: remove this dependency and go through the tokenTransferService
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private GovernanceService governanceService;
	
	@Autowired
	private TokenTransferService tokenTransferService;
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private AppUserRepositoryIf appUserRepository;
	
	@Autowired
	private InitiativeRepositoryIf initiativeRepository;
	
	@Autowired
	private FileStoredRepositoryIf fileStoredRepository;
	
	@Autowired
	private InitiativeRelationshipRepositoryIf initiativeRelationshipRepository;
	
	@Autowired
	private InitiativeTransferRepositoryIf initiativeTransferRepository;
	
	@Autowired
	private MemberRepositoryIf memberRepository;
	
	@Autowired
	private ModelSectionRepositoryIf modelSectionRepository;
	
	@Autowired
	private DecisionMakerRepositoryIf decisionMakerRepository;
	
	@Autowired
	private InitiativeMetaRepositoryIf initiativeMetaRepository;
	
	@Autowired
	private InitiativeTagRepositoryIf initiativeTagRepository;
	
	@Autowired
	private ActivityRepositoryIf activityRepository;
	
	@Autowired
	private TokenMintRepositoryIf tokenMintRespository;
	
	@Autowired
	private WantToContributeRepositoryIf wantToContributeRepository;
	  
	
	@Transactional
	public Boolean canAccess(UUID initiativeId, UUID userId) {
		InitiativeVisibility visibility = initiativeRepository.getVisiblity(initiativeId);
		
		if (visibility != null) {
			switch (visibility) {
				case PRIVATE:
					/* if private, only members can access initiative data */
					return isMember(initiativeId, userId);
				
				case PARENTS:
					if (isMember(initiativeId, userId)) {
						return true;
					} else {
						return isMemberOfParent(initiativeId, userId);
					}
					
				case ECOSYSTEM:
					if (isMember(initiativeId, userId)) {
						return true;
					} else {
						return isMemberOfEcosystem(initiativeId, userId);
					}
					
				case PUBLIC:
					return true;
					
				case INHERITED:
				default:
					return canAccessInherited(initiativeId, userId);
			}
		} else {
			return canAccessInherited(initiativeId, userId);
		}
	}
	
	@Transactional
	public Boolean isMember(UUID initiativeId, UUID userId) {
		return memberRepository.findMemberId(initiativeId, userId) != null;
	}
	
	@Transactional
	public Boolean isMemberOfParent(UUID initiativeId, UUID userId) {
		List<Initiative> parents = getParentGenealogyInitiatives(initiativeId);
		
		for (Initiative parent : parents) {
			if (isMember(parent.getId(), userId)) {
				return true;
			}
		}
		return false;
	}
	
	@Transactional
	public Boolean isMemberOfEcosystem(UUID initiativeId, UUID userId) {
		
		List<UUID> ecosystemIds = findAllInitiativeEcosystemIds(initiativeId);
		
		for (UUID thisInitiativeId : ecosystemIds) {
			if (isMember(thisInitiativeId, userId)) {
				return true;
			}
		}
		return false;
	}
	
	private Boolean canAccessInherited(UUID initiativeId, UUID userId) {
		Initiative parent = initiativeRepository.findOfInitiativesWithRelationship(initiativeId, InitiativeRelationshipType.IS_ATTACHED_SUB);
		if (parent != null) {
			return canAccess(parent.getId(), userId);
		} else {
			/* default permission for inherited when no parent exist is like ecosystem */
			if (isMember(initiativeId, userId)) {
				return true;
			} else {
				return isMemberOfEcosystem(initiativeId, userId);
			}
		}
	}

	/** Non-transactional method to create an initiative in multiple transactions */
	public PostResult init(UUID userId, NewInitiativeDto initiativeDto) {
	
		GetResult<Initiative> result = create(userId, initiativeDto);
		
		if(result.getResult().equals("success")) {
			GetResult<Initiative> result2 = addMembers(result.getData().getId(), initiativeDto.getMembers());
			
			if(result2.getResult().equals("success")) {
				PostResult result3 = transferAssets(result.getData().getId(), initiativeDto, userId);
				
				if (result3.getResult().equals("success")) {
					
					PostResult result4 = initModel(result.getData().getId());
					
					if (result4.getResult().equals("success")) {
						return new PostResult("success", "initiative created and initalized",  result.getData().getId().toString());
					} else {
						return new PostResult("error", "error initializing model",  "");
					}
					
				} else {
					
					return new PostResult("error", "error transferring assets",  "");
				}
			} else {
				return new PostResult("error", "error adding members",  "");
			}
		} else {
			return new PostResult("error", "error creating",  "");
		}
		
	}
	
	@Transactional
	private GetResult<Initiative> create(UUID c1Id, NewInitiativeDto initiativeDto) {
			
		AppUser creator = appUserRepository.findByC1Id(c1Id);
		
		if (creator == null) {
			return new GetResult<Initiative>("error", "creator not found",  null);
		}
			
		Initiative initiative = new Initiative();
		
		/* Basic properties*/
		initiative.setCreator(creator);
		initiative.setStatus(InitiativeStatus.ENABLED);
		
		InitiativeMeta meta = new InitiativeMeta();
		
		meta.setName(initiativeDto.getName());
		meta.setDriver(initiativeDto.getDriver());
		meta.setCreationDate(new Timestamp(System.currentTimeMillis()));
		meta.setModelEnabled(true);
		
		if (!initiativeDto.getAsSubinitiative()) {
			meta.setColor("#009ee3");
		} else {
			Initiative parent = initiativeRepository.findById(UUID.fromString(initiativeDto.getParentInitiativeId()));
			meta.setColor(parent.getMeta().getColor());
		}		
		
		meta = initiativeMetaRepository.save(meta);
		initiative.setMeta(meta);
		
		initiative = initiativeRepository.save(initiative);
		
		/* Create the governace object of this initiative */
		Governance governance = governanceService.create(initiative);
		initiative.setGovernance(governance);
		
		initiative = initiativeRepository.save(initiative);
		
		activityService.newInitiativeCreated(initiative, initiative.getCreator());
		
		return new GetResult<Initiative>("success", "initiative created", initiative);
	}
	
	@Transactional
	public PostResult wantToContribute(UUID initiativeId, UUID userId) {
		
		Initiative initiative = initiativeRepository.findById(initiativeId);
				
		AppUser user = appUserRepository.findByC1Id(userId);
		
		List<DecisionMaker> admins = decisionMakerRepository.findByGovernance_IdAndRole(initiative.getGovernance().getId(), DecisionMakerRole.ADMIN);

		for (DecisionMaker admin : admins) {
			WantToContributeNotification notification = new WantToContributeNotification();
			
			notification.setInitiative(initiative);
			notification.setAdmin(admin.getUser());
			notification.setUser(user);
			notification.setEmailState(NotificationState.PENDING);
			
			wantToContributeRepository.save(notification);
		}
		
		return new PostResult("success", "notifications recorded for sending", null);
	}
	
	@Transactional
	public PostResult wantToContributeAccept(UUID initiativeId, UUID userId) {
		
		Member member = addMemberOrGet(initiativeId, userId, DecisionMakerRole.MEMBER);
		
		return new PostResult("success", "member added sending", member.getId().toString());
	}
	
	@Transactional
	private GetResult<Initiative> addMembers(UUID initiativeId, List<MemberDto> initiativeMembers) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
		
		/* List of Members */
		for (MemberDto memberDto : initiativeMembers) {
			addMemberOrGet(initiativeId, UUID.fromString(memberDto.getUser().getC1Id()), DecisionMakerRole.valueOf(memberDto.getRole()));
		}
		
		return new GetResult<Initiative>("success", "initiative created", initiative);
	}
	
	@Transactional
	public Member addMemberOrGet(UUID initiativeId, UUID c1Id, DecisionMakerRole role) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
		AppUser memberUser = appUserRepository.findByC1Id(c1Id);
		
		Member existingMember = memberRepository.findByInitiative_IdAndUser_C1Id(initiativeId, c1Id);
		
		if (existingMember != null) {
			return existingMember;
		}
		
		Member member = new Member();
		member.setInitiative(initiative);
		member.setUser(memberUser);
		member = memberRepository.save(member);
		
		initiative.getMembers().add(member);
		initiativeRepository.save(initiative);
		
		if (role.equals(DecisionMakerRole.ADMIN) || role.equals(DecisionMakerRole.EDITOR)) {
			governanceService.addDecisionMaker(initiative.getGovernance().getId(), memberUser.getC1Id(), role);
		}
		
		/* members are subscribed to initiatives by default */
		activityService.addSubscriber(initiativeId, memberUser.getC1Id(), SubscriptionElementType.INITIATIVE);
		
		return member;
	}
	
	@Transactional 
	public PostResult editMember(UUID initiativeId, UUID userId, DecisionMakerRole role) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
		Member member = memberRepository.findByInitiative_IdAndUser_C1Id(initiativeId, userId);
		
		if (member != null) {
			governanceService.editOrCreateDecisionMaker(initiative.getGovernance().getId(), member.getUser().getC1Id(), role);
		}
		
		return new PostResult("success", "member edited", member.getId().toString());
		
	}
		
	@Transactional
	private PostResult transferAssets(UUID initiativeId, NewInitiativeDto initiativeDto, UUID creatorId) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
		
		if (!initiativeDto.getAsSubinitiative()) {
			/* if is not a sub-initiative, then create a token for this initiative */
			if (initiativeDto.getCreateToken()) {
				TokenType token = tokenService.create(initiativeDto.getOwnTokens().getAssetName(), "T");
				initiative.getTokenTypes().add(token);
				initiative = initiativeRepository.save(initiative);
				tokenService.mintToHolder(token.getId(), initiative.getId(), initiativeDto.getOwnTokens().getOwnedByThisHolder(), TokenHolderType.INITIATIVE);
				
				TokenMint mint = new TokenMint();
				mint.setToken(token);
				mint.setOrderedBy(appUserRepository.findByC1Id(creatorId));
				mint.setToHolder(initiativeId);
				mint.setMotive("Initiative creation.");
				mint.setNotes("");
				mint.setValue(initiativeDto.getOwnTokens().getOwnedByThisHolder());
				
				mint = tokenMintRespository.save(mint);
			}
			
			return new PostResult("success", "initiative tokens created", initiative.getId().toString());
			
		} else {
			Initiative parent = initiativeRepository.findById(UUID.fromString(initiativeDto.getParentInitiativeId()));
			
			/* if it is a sub-initiative, then link to parent initiative */
			InitiativeRelationship relationship = new InitiativeRelationship();
			relationship.setInitiative(initiative);
			relationship.setType(InitiativeRelationshipType.IS_ATTACHED_SUB);
			relationship.setOfInitiative(parent);
			
			relationship = initiativeRelationshipRepository.save(relationship);
			
			List<InitiativeTransfer> transfers = new ArrayList<InitiativeTransfer>();
			/* and transfer parent assets to child */
			for (TransferDto thisTransfer : initiativeDto.getAssetsTransfers()) {
				/* ignore zero token transfers */
				if (thisTransfer.getValue() > 0) {
					TokenType token = tokenService.getTokenType(UUID.fromString(thisTransfer.getAssetId()));
					
					tokenService.transfer(token.getId(), parent.getId(), initiative.getId(), thisTransfer.getValue(), TokenHolderType.INITIATIVE);
					
					/* upper layer keeping track of who transfered what to whom */
					InitiativeTransfer transfer = new InitiativeTransfer();
					transfer.setFrom(parent);
					transfer.setTo(initiative);
					transfer.setTokenType(token);
					transfer.setValue(thisTransfer.getValue());
					transfer.setMotive("sub-initiative creation");
					transfer.setNotes("");
					transfer.setOrderDate(new Timestamp(System.currentTimeMillis()));
									
					transfer = initiativeTransferRepository.save(transfer);
					transfers.add(transfer);
				}
			}
			
			initiative.getRelationships().add(relationship);
			initiativeRepository.save(initiative);
			
			activityService.newSubinitiativeCreated(parent, initiative.getCreator(), initiative, transfers);
		}
			
		return new PostResult("success", "sub initiative created and tokens transferred",  initiative.getId().toString());
	}
	
	@Transactional
	private PostResult initModel(UUID initiativeId) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
		
		ModelSection section = new ModelSection();
		section.setTitle(initiative.getMeta().getName());
		section.setInitiative(initiative);
		section.setIsTopModelSection(true);
		
		section = modelSectionRepository.save(section);
		
		initiative.setTopModelSection(section);
		initiativeRepository.save(initiative);
		
		return new PostResult("success", "initiative top section created",  initiative.getId().toString());
	}
	
	@Transactional
	public PostResult createNewToken(UUID initiativeId, AssetsDto tokenDto, UUID userId) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
		
		TokenType token = tokenService.create(tokenDto.getAssetName(), "T");
		initiative.getTokenTypes().add(token);
		initiative = initiativeRepository.save(initiative);
		tokenService.mintToHolder(token.getId(), initiative.getId(), tokenDto.getOwnedByThisHolder(), TokenHolderType.INITIATIVE);
		
		AppUser orderedBy = appUserRepository.findByC1Id(userId);
		
		TokenMint mint = new TokenMint();
		mint.setToken(token);
		mint.setOrderedBy(orderedBy);
		mint.setToHolder(initiativeId);
		mint.setMotive("Token creation.");
		mint.setNotes("");
		mint.setValue(tokenDto.getOwnedByThisHolder());
		
		mint = tokenMintRespository.save(mint);
		
		activityService.newTokenCreated(initiative, orderedBy, token, mint);
		
		return new PostResult("success", "initiative created and tokens created", initiative.getId().toString());
	} 
	
	
	@Transactional
	public PostResult edit(UUID initiativeId, UUID userId, NewInitiativeDto initiativeDto) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
		InitiativeMeta initiativeMeta = initiative.getMeta();
		
		String oldName = initiativeMeta.getName();
		String oldDriver = initiativeMeta.getDriver();
		
		initiativeMeta.setName(initiativeDto.getName());
		initiativeMeta.setDriver(initiativeDto.getDriver());
		initiativeMeta.setColor(initiativeDto.getColor());
		initiativeMeta.setModelEnabled(initiativeDto.getModelEnabled());
		if (initiativeDto.getVisibility() != null) {
			initiativeMeta.setVisibility(InitiativeVisibility.valueOf(initiativeDto.getVisibility()));
		}
		
		/* remove and add all tags */
		initiativeMeta.getTags().removeAll(initiativeMeta.getTags());
		for (InitiativeTagDto tagDto : initiativeDto.getTags()) {
			InitiativeTag tag = initiativeTagRepository.findById(UUID.fromString(tagDto.getId()));
			initiativeMeta.getTags().add(tag);
		}
		
		/* update or remove image */
		if (initiativeDto.getNewImageFileId() != null) {
			if(!initiativeDto.getNewImageFileId().equals("REMOVE")) {
				UUID imageFileId = initiativeDto.getNewImageFileId().equals("") ? null : UUID.fromString(initiativeDto.getNewImageFileId());
				FileStored imageFile = fileStoredRepository.findById(imageFileId);
				initiativeMeta.setImageFile(imageFile);
			} else {
				initiativeMeta.setImageFile(null);
			}
		} 
		
		initiativeMetaRepository.save(initiativeMeta);
		
		if (!oldName.equals(initiativeDto.getName()) || !oldDriver.equals(initiativeDto.getDriver())) {
			/* update topModelSection name */
			ModelSection section = initiative.getTopModelSection();
			section.setTitle(initiativeMeta.getName());
			modelSectionRepository.save(section);
			
			/* notify only if actually different */
			activityService.initiativeEdited(initiative, appUserRepository.findByC1Id(userId), oldName, oldDriver);
		}
		
		return new PostResult("success", "initaitive updated", initiative.getId().toString());  
	}
	
	@Transactional
	public PostResult delete(UUID initiativeId, UUID userId) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
		
		List<Initiative> subiniatiatives = initiativeRepository.findInitiativesWithRelationship(initiative.getId(), InitiativeRelationshipType.IS_ATTACHED_SUB);
		
		for (Initiative subinitiative : subiniatiatives) {
			/* first delete all subinitiatives (recursively starting from the lower level )*/
			delete(subinitiative.getId(), userId);
		}
		
		Initiative parent = initiativeRepository.findOfInitiativesWithRelationship(initiativeId, InitiativeRelationshipType.IS_ATTACHED_SUB);
		if (parent != null) {
			/* transfer all assets back to parent */
			tokenTransferService.transferAllFromInitiativeToInitiative(initiative.getId(), parent.getId(), userId, "initiative deleted", "");
		}
		
		initiative.setStatus(InitiativeStatus.DELETED);
		initiativeRepository.save(initiative);
		
		activityService.initiativeDeleted(initiative, appUserRepository.findByC1Id(userId));
		
		return new PostResult("success", "initaitive updated", initiative.getId().toString());  
	}
	
	
	/** Get key data from an initiative: id, name and driver but no
	 * data from its assets */
	@Transactional
	public InitiativeDto getLight(UUID id) {
		return  initiativeRepository.findById(id).toDto();
	}
	
	/** */
	@Transactional
	public List<AssetsDto> getInitiativeAssetsDtoLight(UUID id) {
		
		Initiative initiative = initiativeRepository.findById(id);
		List<TokenType> ownTokens = initiative.getTokenTypes();
		List<TokenType> tokenTypes = tokenService.getTokenTypesHeldBy(initiative.getId());
		
		/* add own tokens even if the initiative does not have them */
		for (TokenType own : ownTokens) {
			if (!tokenTypes.contains(own)) {
				tokenTypes.add(own);
			}
		}
		
		List<AssetsDto> assets = new ArrayList<AssetsDto>();
		
		for (TokenType token : tokenTypes) {
			AssetsDto asset = new AssetsDto();
			asset.setAssetId(token.getId().toString());
			asset.setAssetName(token.getName());
			assets.add(asset);
		}
		
		return assets;
	}
	
	public Initiative findByTokenType_Id(UUID tokenTypeId) {
		return initiativeRepository.findByTokenTypes_Id(tokenTypeId);
	}
	
	@Transactional
	public GetResult<List<InitiativeDto>> getOfUser(UUID userC1Id) {
		/* return all super-initiatives (initiatives without parent initiatives) 
		 * and include all sub-initiativespf each of them */
		
		AppUser user = appUserRepository.findByC1Id(userC1Id);
		
		/* get all initiatives in which the user is a contributor */
		List<Initiative> superInitiatives = getSuperInitiativesOfMember(user.getC1Id());
		
		List<InitiativeDto> initiativesDtos = new ArrayList<InitiativeDto>();
		for (Initiative initiative : superInitiatives) {
			if (initiative.getStatus() == InitiativeStatus.ENABLED) {
				InitiativeDto dto = initiative.toDto();
				
				dto.setLoggedMembership(getMemberAndInParent(initiative.getId(),  user.getC1Id()));
				
				/* look for the full sub-initiative tree of each super initiative */
				List<InitiativeDto> subInitiatives = getSubinitiativesTree(initiative.getId(), user.getC1Id());
				dto.setSubInitiatives(subInitiatives);
				
				initiativesDtos.add(dto);
			}
		}
		
		return new GetResult<List<InitiativeDto>>("success", "initiatives retrieved", initiativesDtos);
	}
	
	@Transactional
	public List<Initiative> getSuperInitiativesOfMember(UUID userC1Id) {
		List<Initiative> allInitiatives = initiativeRepository.findOfMember(userC1Id);
		return onlySuperInitiatives(allInitiatives);
	}
	
	@Transactional
	public List<Initiative> onlySuperInitiatives(List<Initiative> allInitiatives) {
		/* filters an initiative list to keep only those that are superinitiaitves (with no parent)*/
		List<Initiative> superInitiatives = new ArrayList<Initiative>();
		
		for (Initiative thisInitiative : allInitiatives) {
			Initiative superInitiative = getSuperInitiative(thisInitiative.getId());
			
			boolean exist = false;
			for (Initiative existingSuperInitiative : superInitiatives) {
				if (existingSuperInitiative.getId() == superInitiative.getId()) {
					exist = true;
				}
			}
			
			if(!exist) {
				superInitiatives.add(superInitiative);
			}
		}
		
		return superInitiatives;
	}
	
	@Transactional
	public Initiative getSuperInitiative(UUID initiativeId) {
		/* get the farthest parent initiative */
		List<Initiative> parents = getParentGenealogyInitiatives(initiativeId);
		if(parents.size() > 0) {
			/* last element in the parents array is the farthest relative */
			return parents.get(parents.size() - 1);
		} else {
			return initiativeRepository.findById(initiativeId);
		}
	}
	
	@Transactional
	public List<Initiative> getParentGenealogyInitiatives(UUID initiativeId) {
		List<Initiative> parents = new ArrayList<Initiative>();
		Initiative parent = initiativeRepository.findOfInitiativesWithRelationship(initiativeId, InitiativeRelationshipType.IS_ATTACHED_SUB);
		
		while(parent != null) {
			/* look upwards until an initiative with no parent is found */
			parents.add(parent);
			parent = initiativeRepository.findOfInitiativesWithRelationship(parent.getId(), InitiativeRelationshipType.IS_ATTACHED_SUB);
		}
		
		return parents;
	}
	
	@Transactional List<InitiativeDto> getParentInitiativesDtos(UUID initiativeId) {
		List<InitiativeDto> parentsDtos = new ArrayList<InitiativeDto>();
		
		for (Initiative parent : getParentGenealogyInitiatives(initiativeId)) {
			parentsDtos.add(parent.toDto());
		}
		
		return parentsDtos;
	}
	
	@Transactional 
	public List<UUID> findAllInitiativeEcosystemIds(UUID initiativeId) {
		Initiative superInitiative = getSuperInitiative(initiativeId);
		List<InitiativeDto> subinitiativesTree = getSubinitiativesTree(superInitiative.getId(), null);
		
		List<UUID> ecosystemIds = new ArrayList<UUID>();
		
		ecosystemIds.add(superInitiative.getId());
		ecosystemIds.addAll(extractAllIdsFromInitiativesTree(subinitiativesTree, new ArrayList<UUID>()));
		
		return ecosystemIds;
	}
	
	private List<UUID> extractAllIdsFromInitiativesTree(List<InitiativeDto> initiativeTree, List<UUID> list) {
		
		for (InitiativeDto initiativeDto : initiativeTree) {
			list.add(UUID.fromString(initiativeDto.getId()));
		}
		
		for (InitiativeDto initiativeDto : initiativeTree) {
			extractAllIdsFromInitiativesTree(initiativeDto.getSubInitiatives(), list);
		}
		
		return list;
	}
	
	@Transactional
	public List<InitiativeDto> getSubinitiativesTree(UUID initiativeId, UUID userId) {
		Initiative initiative = initiativeRepository.findById(initiativeId); 
		List<Initiative> subIniatiatives = initiativeRepository.findInitiativesWithRelationship(initiative.getId(), InitiativeRelationshipType.IS_ATTACHED_SUB);
		
		List<InitiativeDto> subinitiativeDtos = new ArrayList<InitiativeDto>();
		
		for (Initiative subinitiative : subIniatiatives) {
			if (subinitiative.getStatus() == InitiativeStatus.ENABLED) {
				InitiativeDto subinitiativeDto = subinitiative.toDto();
				
				if (userId != null) {
					subinitiativeDto.setLoggedMembership(getMemberAndInParent(subinitiative.getId(),  userId));	
				}
				
				/* recursively call this for its own sub-initiatives */
				List<InitiativeDto> subsubIniatiativesDto = getSubinitiativesTree(subinitiative.getId(), userId);
				subinitiativeDto.setSubInitiatives(subsubIniatiativesDto);
				
				subinitiativeDtos.add(subinitiativeDto);
			}
		}
		
		return subinitiativeDtos;
	}
	
	public List<MemberDto> getMemberAndInParent(UUID initiativeId, UUID userId) {
		List<MemberDto> members = new ArrayList<MemberDto>();
		members.add(getMember(initiativeId, userId));
		members.add(getMemberInParent(initiativeId, userId));
		
		return members;
	}
	
	@Transactional
	public MemberDto getMemberInParent(UUID initiativeId, UUID userId) {
		Initiative parent = initiativeRepository.findOfInitiativesWithRelationship(initiativeId, InitiativeRelationshipType.IS_ATTACHED_SUB);
		if (parent == null) return null;
		else return getMember(parent.getId(), userId);
	}
	
	@Transactional
	public MemberDto getMember(UUID initiativeId, UUID userId) {
		
		if (userId == null) {
			return null;
		}
		
		/* check in this initiative */
		Initiative initiative = initiativeRepository.findById(initiativeId);
		
		Member member = memberRepository.findByInitiative_IdAndUser_C1Id(initiativeId, userId);
		
		MemberDto memberDto = new MemberDto();
		
		if (member != null) {
			memberDto.setId(member.getId().toString());
			memberDto.setUser(member.getUser().toDtoLight());
			
			/* governance related data */
			DecisionMaker decisionMaker = governanceService.getDecisionMaker(initiative.getGovernance().getId(), member.getUser().getC1Id());
			if(decisionMaker != null) {
				memberDto.setRole(decisionMaker.getRole().toString());
			} else {
				memberDto.setRole(DecisionMakerRole.MEMBER.toString());
			}
		} else {
			memberDto.setId("");
			memberDto.setUser(appUserRepository.findByC1Id(userId).toDtoLight());
			memberDto.setRole(DecisionMakerRole.ALIEN.toString());
		}
		
		return memberDto;
	}
	
	public InitiativeMembersDto getMembersAndSubmembers(UUID initiativeId) {
		
		Initiative initiative = initiativeRepository.findById(initiativeId);
		
		InitiativeMembersDto initiativeMembers = new InitiativeMembersDto();
		initiativeMembers.setInitiativeId(initiative.getId().toString());
		initiativeMembers.setInitiativeName(initiative.getMeta().getName());
		
		List<TokenType> tokenTypes = tokenService.getTokenTypesHeldBy(initiative.getId());
		
		
		/* add members of this initiative */
		for (Member member : initiative.getMembers()) {
			MemberDto memberDto = new MemberDto();
			
			memberDto.setId(member.getId().toString());
			memberDto.setUser(member.getUser().toDtoLight());
			
			/* governance related data */
			DecisionMaker decisionMaker = governanceService.getDecisionMaker(initiative.getGovernance().getId(), member.getUser().getC1Id());
			if(decisionMaker != null) {
				memberDto.setRole(decisionMaker.getRole().toString());
			} else {
				memberDto.setRole(DecisionMakerRole.MEMBER.toString());
			}
			
			/* assets related data */
			for (TokenType token : tokenTypes) {
				memberDto.getReceivedAssets().add(tokenService.getTokensOfHolderDtoLight(token.getId(), member.getUser().getC1Id()));
			}
			
			initiativeMembers.getMembers().add(memberDto);
		}
		
		/* add the members of all sub-initiatives too */
		for (InitiativeDto subInitiative : getSubinitiativesTree(initiative.getId(), null)) {
			/* recursively call with subinitiatives */
			initiativeMembers.getSubinitiativesMembers().add(getMembersAndSubmembers(UUID.fromString(subInitiative.getId())));
		}
		
		return initiativeMembers;
	}
	
	
	@Transactional
	public PostResult postMember(UUID initiativeId, UUID c1Id, DecisionMakerRole role) {
		Member member = addMemberOrGet(initiativeId, c1Id, role);
		if (member != null) {
			return new PostResult("success", "member added",  member.getId().toString());
		} 
		
		return new PostResult("error", "member not added", "");
	}
	
	public PostResult deleteMember(UUID initiativeId, UUID memberUserId) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
		Member member = memberRepository.findByInitiative_IdAndUser_C1Id(initiativeId, memberUserId);
		
		List<DecisionMaker> admins = governanceService.getDecisionMakerWithRole(initiative.getGovernance().getId(), DecisionMakerRole.ADMIN);
		
		boolean otherAdmin = false;
		for (DecisionMaker admin :admins) {
			if (!admin.getUser().getC1Id().equals(memberUserId)) {
				otherAdmin = true;
			}
		}
		
		if (otherAdmin) {
			/* delete only if another admin remains */
			
			/* members were subscribed to initiatives by default, so, delete them when deleting the member */
			activityService.removeSubscriber(initiativeId,  SubscriptionElementType.INITIATIVE, member.getUser().getC1Id());
			
			memberRepository.delete(member);
			governanceService.deleteDecisionMaker(initiative.getGovernance().getId(), member.getUser().getC1Id());
			
			return new PostResult("success", "contributor deleted", "");
		}
		
		return new PostResult("error", "user is the only admin, it cannot be deleted", "");
		
	}
	
	@Transactional
	public GetResult<List<InitiativeDto>> searchBy(SearchFiltersDto searchFilters) {
		
		List<UUID> tagIds = new ArrayList<UUID>();
		for (InitiativeTagDto tag : searchFilters.getTags()) {
			tagIds.add(UUID.fromString(tag.getId()));
		}
		
		List<Initiative> initiatives = null;
		
		if (tagIds.size() > 0) {
			initiatives = initiativeRepository.searchByTagIdAndVisibility(tagIds, InitiativeVisibility.PUBLIC);	
		} else {
			initiatives = initiativeRepository.findByVisibility(InitiativeVisibility.PUBLIC);
		}
		
		List<Initiative> superInitiatives = onlySuperInitiatives(initiatives);
		
		List<InitiativeDto> initiativesDtos = new ArrayList<InitiativeDto>();
		
		for(Initiative initiative : superInitiatives) {
			initiativesDtos.add(initiative.toDto());
		}
		
		return new GetResult<List<InitiativeDto>>("succes", "initiatives returned", initiativesDtos);
		
	}
	
	@Transactional
	public Member getOrAddMember(UUID initiativeId, UUID userId) {
		Member member = memberRepository.findByInitiative_IdAndUser_C1Id(initiativeId, userId);
		
		if(member == null) {
			Initiative initiative = initiativeRepository.findById(initiativeId);
			AppUser user = appUserRepository.findByC1Id(userId);
			
			member = new Member();
			member.setInitiative(initiative);
			member.setUser(user);
			
			member = memberRepository.save(member);
		}
		
		return member;
	}


	@Transactional
	public InitiativeTag getOrCreateTag(InitiativeTagDto tagDto) {
		InitiativeTag tag = initiativeTagRepository.findByTagText(tagDto.getTagText());
		
		if (tag == null) {
			tag = new InitiativeTag();
			
			tag.setTagText(tagDto.getTagText());
			tag.setDescription(tagDto.getDescription());
			
			tag = initiativeTagRepository.save(tag);
		}
		
		return tag;
	}
	
	@Transactional
	public PostResult addTagToInitiative(UUID initiativeId, InitiativeTagDto tagDto) {
		
		Initiative initiative = initiativeRepository.findById(initiativeId);
		if (initiative == null) return new PostResult("error", "initiative not found", "");
		
		InitiativeTag tag = getOrCreateTag(tagDto);
		initiative.getMeta().getTags().add(tag);
		
		return new PostResult("success", "tag added to initiative", tag.getId().toString());
	}
	
	@Transactional
	public PostResult deleteTagFromInitiative(UUID initiativeId, UUID tagId) {
		
		Initiative initiative = initiativeRepository.findById(initiativeId);
		if (initiative == null) return new PostResult("error", "initiative not found", "");
		
		InitiativeTag tag = initiativeTagRepository.findById(tagId);
		if (tag == null) return new PostResult("error", "tag not found", "");
		
		initiative.getMeta().getTags().remove(tag);
		
		return new PostResult("success", "tag added to initiative", initiative.getId().toString());
	}
	
	
	@Transactional
	public GetResult<List<InitiativeTagDto>> searchTagsBy(String q) {
		List<InitiativeTag> tags = initiativeTagRepository.findTop10ByTagTextLikeIgnoreCase("%"+q+"%");
		
		List<InitiativeTagDto> tagsDtos = new ArrayList<InitiativeTagDto>();
		
		for(InitiativeTag tag : tags) {
			tagsDtos.add(tag.toDto());
		}
		
		return new GetResult<List<InitiativeTagDto>>("succes", "initiatives returned", tagsDtos);
	}
	
	@Transactional
	public GetResult<InitiativeTagDto> getTag(UUID tagId) {
		InitiativeTag tag = initiativeTagRepository.findById(tagId);
		
		if (tag == null) {
			return new GetResult<InitiativeTagDto>("error", "initiative tag not found", null); 
		}
		
		return new GetResult<InitiativeTagDto>("success", "initiative tag returned", tag.toDto());
	}
	
	@Transactional
	public GetResult<Page<ActivityDto>>  getActivityUnderInitiative(UUID initiativeId, PageRequest page, Boolean onlyMessages) {

		List<InitiativeDto> subinitiativesTree = getSubinitiativesTree(initiativeId, null);
		
		List<UUID> allInitiativesIds = new ArrayList<UUID>();
		
		allInitiativesIds.add(initiativeId);
		allInitiativesIds.addAll(extractAllIdsFromInitiativesTree(subinitiativesTree, new ArrayList<UUID>()));
		
		Page<Activity> activities = null;
		
		if(!onlyMessages) {
			activities = activityRepository.findOfInitiatives(allInitiativesIds, page);
		} else {
			activities = activityRepository.findOfInitiativesAndType(allInitiativesIds, ActivityType.MESSAGE_POSTED, page);
		}
		
		List<ActivityDto> activityDtos = new ArrayList<ActivityDto>();
		
		for(Activity activity : activities.getContent()) {
			activityDtos.add(activity.toDto());
		}
		
		Page<ActivityDto> dtosPage = new PageImpl<ActivityDto>(activityDtos, page, activities.getNumberOfElements());
		
		return new GetResult<Page<ActivityDto>>("succes", "actvity returned", dtosPage);
	}
	
}