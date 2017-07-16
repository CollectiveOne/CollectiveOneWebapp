package org.collectiveone.modules.initiatives;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.activity.ActivityService;
import org.collectiveone.modules.activity.SubscriptionElementType;
import org.collectiveone.modules.governance.DecisionMaker;
import org.collectiveone.modules.governance.DecisionMakerRole;
import org.collectiveone.modules.governance.Governance;
import org.collectiveone.modules.governance.GovernanceService;
import org.collectiveone.modules.tokens.AssetsDto;
import org.collectiveone.modules.tokens.InitiativeTransfer;
import org.collectiveone.modules.tokens.InitiativeTransferRepositoryIf;
import org.collectiveone.modules.tokens.TokenHolderType;
import org.collectiveone.modules.tokens.TokenService;
import org.collectiveone.modules.tokens.TokenType;
import org.collectiveone.modules.tokens.TransferDto;
import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitiativeService {
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private GovernanceService governanceService;
	
	@Autowired
	private ActivityService activityService;
	
	
	@Autowired
	private AppUserRepositoryIf appUserRepository;
	
	@Autowired
	private InitiativeRepositoryIf initiativeRepository;
	
	@Autowired
	private InitiativeRelationshipRepositoryIf initiativeRelationshipRepository;
	
	@Autowired
	private InitiativeTransferRepositoryIf initiativeTransferRepository;
	
	@Autowired
	private MemberRepositoryIf memberRepository;
	
	@Autowired
	private InitiativeMetaRepositoryIf initiativeMetaRepository;
	
	
	
	/** Non-transactional method to create an initiative in multiple transactions */
	public PostResult init(UUID userId, NewInitiativeDto initiativeDto) {
	
		GetResult<Initiative> result = create(userId, initiativeDto);
		
		if(result.getResult().equals("success")) {
			GetResult<Initiative> result2 = addMembers(result.getData().getId(), initiativeDto.getMembers());
			
			if(result2.getResult().equals("success")) {
				PostResult result3 = transferAssets(result2.getData().getId(), initiativeDto);
				
				if (result3.getResult().equals("success")) {
					return result3;
				
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
		initiative.setEnabled(true);
		
		InitiativeMeta meta = new InitiativeMeta();
		
		meta.setName(initiativeDto.getName());
		meta.setDriver(initiativeDto.getDriver());
		meta.setCreationDate(new Timestamp(System.currentTimeMillis()));
		meta.setColor("#009ee3");
		
		meta = initiativeMetaRepository.save(meta);
		initiative.setMeta(meta);
		
		initiative = initiativeRepository.save(initiative);
		
		/* Create the governace object of this initiative */
		Governance governance = governanceService.create(initiative);
		initiative.setGovernance(governance);
		
		initiative = initiativeRepository.save(initiative);
		
		return new GetResult<Initiative>("success", "initiative created", initiative);
	}
	
	@Transactional
	private GetResult<Initiative> addMembers(UUID initiativeId, List<MemberDto> initiativeMemebers) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
		
		/* List of Members */
		for (MemberDto memberDto : initiativeMemebers) {
			addMember(initiativeId, UUID.fromString(memberDto.getUser().getC1Id()), DecisionMakerRole.valueOf(memberDto.getRole()));
		}
		
		return new GetResult<Initiative>("success", "initiative created", initiative);
	}
	
	@Transactional
	public Member addMember(UUID initiativeId, UUID c1Id, DecisionMakerRole role) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
		
		AppUser memberUser = appUserRepository.findByC1Id(c1Id); 
		
		Member member = new Member();
		member.setInitiative(initiative);
		member.setUser(memberUser);
		member = memberRepository.save(member);
		
		initiative.getMembers().add(member);
		initiativeRepository.save(initiative);
		
		if (role.equals(DecisionMakerRole.ADMIN)) {
			governanceService.addDecisionMaker(initiative.getGovernance().getId(), memberUser.getC1Id(), DecisionMakerRole.ADMIN);
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
	private PostResult transferAssets(UUID initiativeId, NewInitiativeDto initiativeDto) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
		
		if (!initiativeDto.getAsSubinitiative()) {
			/* if is not a sub-initiative, then create a token for this initiative */
			TokenType token = tokenService.create(initiativeDto.getOwnTokens().getAssetName(), "T");
			initiative.setTokenType(token);
			initiative = initiativeRepository.save(initiative);
			tokenService.mintToHolder(token.getId(), initiative.getId(), initiativeDto.getOwnTokens().getOwnedByThisHolder(), TokenHolderType.INITIATIVE);
			
			activityService.newInitiativeCreated(initiative, initiative.getCreator(), token);
			
			return new PostResult("success", "initiative created and tokens created", initiative.getId().toString());
			
		} else {
			Initiative parent = initiativeRepository.findById(UUID.fromString(initiativeDto.getParentInitiativeId()));
			
			/* if it is a sub-initiative, then link to parent initiative */
			InitiativeRelationship relationship = new InitiativeRelationship();
			relationship.setInitiative(initiative);
			relationship.setType(InitiativeRelationshipType.IS_DETACHED_SUB);
			relationship.setOfInitiative(parent);
			
			relationship = initiativeRelationshipRepository.save(relationship);
			
			List<InitiativeTransfer> transfers = new ArrayList<InitiativeTransfer>();
			/* and transfer parent assets to child */
			for (TransferDto thisTransfer : initiativeDto.getAssetsTransfers()) {
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
			
			initiative.getRelationships().add(relationship);
			initiativeRepository.save(initiative);
			
			activityService.newSubinitiativeCreated(parent, initiative.getCreator(), initiative, transfers);
		}
			
		return new PostResult("success", "sub initiative created and tokens transferred",  initiative.getId().toString());
	}
	
	
	@Transactional
	public PostResult edit(UUID initiativeId, UUID userId, NewInitiativeDto initiativeDto) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
		InitiativeMeta initiativeMeta = initiative.getMeta();
		
		String oldName = initiativeMeta.getName();
		String oldDriver = initiativeMeta.getDriver();
		
		initiativeMeta.setName(initiativeDto.getName());
		initiativeMeta.setDriver(initiativeDto.getDriver());
		
		initiativeMetaRepository.save(initiativeMeta);
		
		activityService.initiativeEdited(initiative, appUserRepository.findByC1Id(userId), oldName, oldDriver);
		
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
	public List<AssetsDto> getInitiativeAssets(UUID id) {
		
		Initiative initiative = initiativeRepository.findById(id);
		List<TokenType> tokenTypes = tokenService.getTokenTypesHeldBy(initiative.getId());
		
		List<AssetsDto> assets = new ArrayList<AssetsDto>();
		
		for (TokenType token : tokenTypes) {
			AssetsDto asset = new AssetsDto();
			asset.setAssetId(token.getId().toString());
			asset.setAssetName(token.getName());
			asset.setOwnedByThisHolder(tokenService.getHolder(token.getId(), initiative.getId()).getTokens());
			asset.setTotalExistingTokens(tokenService.getTotalExisting(token.getId()));
			
			assets.add(asset);
		}
		
		return assets;
	}
	
	public Initiative findByTokenType_Id(UUID tokenTypeId) {
		return initiativeRepository.findByTokenType_Id(tokenTypeId);
	}
	
	@Transactional
	public GetResult<List<InitiativeDto>> getOfUser(UUID userC1Id) {
		/* return the all super-initiatives (initiatives without parent initiatives) 
		 * and include all sub-initiativespf each of them */
		
		AppUser user = appUserRepository.findByC1Id(userC1Id);
		List<Initiative> superInitiatives = getSuperInitiativesOfMember(user.getC1Id());
		
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
	public List<Initiative> getSuperInitiativesOfMember(UUID userC1Id) {
		List<Initiative> allInitiatives = initiativeRepository.findOfMember(userC1Id);
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
		List<Initiative> parents = getParentInitiatives(initiativeId);
		if(parents.size() > 0) {
			/* last element in the parents array is the farther relative */
			return parents.get(parents.size() - 1);
		} else {
			return initiativeRepository.findById(initiativeId);
		}
	}
	
	@Transactional
	public List<Initiative> getParentInitiatives(UUID initiativeId) {
		List<Initiative> parents = new ArrayList<Initiative>();
		Initiative parent = initiativeRepository.findOfInitiativesWithRelationship(initiativeId, InitiativeRelationshipType.IS_DETACHED_SUB);
		
		while(parent != null) {
			/* look upwards until an initiative with no parent is found */
			parents.add(parent);
			parent = initiativeRepository.findOfInitiativesWithRelationship(parent.getId(), InitiativeRelationshipType.IS_DETACHED_SUB);
		}
		
		return parents;
	}
	
	@Transactional List<InitiativeDto> getParentInitiativesDtos(UUID initiativeId) {
		List<InitiativeDto> parentsDtos = new ArrayList<InitiativeDto>();
		
		for (Initiative parent : getParentInitiatives(initiativeId)) {
			parentsDtos.add(parent.toDto());
		}
		
		return parentsDtos;
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
	public MemberDto getMember(UUID initiativeId, UUID userId) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
		Member member = memberRepository.findByInitiative_IdAndUser_C1Id(initiativeId, userId);
		
		MemberDto memberDto = new MemberDto();

		if (member != null) {
			memberDto.setId(member.getId().toString());
			memberDto.setUser(member.getUser().toDto());
			
			/* governance related data */
			DecisionMaker decisionMaker = governanceService.getDecisionMaker(initiative.getGovernance().getId(), member.getUser().getC1Id());
			if(decisionMaker != null) {
				if(decisionMaker.getRole() == DecisionMakerRole.ADMIN) {
					memberDto.setRole(DecisionMakerRole.ADMIN.toString());
				}
			}
		} else {
			memberDto.setId("");
			memberDto.setUser(appUserRepository.findByC1Id(userId).toDto());
			memberDto.setRole(DecisionMakerRole.ALIEN.toString());
		}
		
		return memberDto;
	}
	
	public InitiativeMembersDto getMembersAndSubmembers(UUID initiativeId) {
		
		Initiative initiative = initiativeRepository.findById(initiativeId);
		
		InitiativeMembersDto initiativeMembers = new InitiativeMembersDto();
		initiativeMembers.setInitiativeId(initiative.getId().toString());
		initiativeMembers.setInitiativeName(initiative.getMeta().getName());
		
		
		/* add members of this initiative */
		for (Member member : initiative.getMembers()) {
			MemberDto memberDto = new MemberDto();
			
			memberDto.setId(member.getId().toString());
			memberDto.setUser(member.getUser().toDto());
			
			/* governance related data */
			DecisionMaker decisionMaker = governanceService.getDecisionMaker(initiative.getGovernance().getId(), member.getUser().getC1Id());
			if(decisionMaker != null) {
				memberDto.setRole(decisionMaker.getRole().toString());
			} else {
				memberDto.setRole(DecisionMakerRole.MEMBER.toString());
			}
			
			initiativeMembers.getMembers().add(memberDto);
		}
		
		/* add the members of all sub-initiatives too */
		for (InitiativeDto subInitiative : getSubinitiativesTree(initiative.getId())) {
			/* recursively call with subinitiatives */
			initiativeMembers.getSubinitiativesMembers().add(getMembersAndSubmembers(UUID.fromString(subInitiative.getId())));
		}
		
		return initiativeMembers;
	}
	
	
	@Transactional
	public PostResult postMember(UUID initiativeId, UUID c1Id, DecisionMakerRole role) {
		Member member = addMember(initiativeId, c1Id, role);
		if (member != null) {
			return new PostResult("success", "member added",  member.getId().toString());
		} 
		
		return new PostResult("error", "member not added", "");
	}
	
	public PostResult deleteMember(UUID initiativeId, UUID memberUserId) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
		Member member = memberRepository.findByInitiative_IdAndUser_C1Id(initiativeId, memberUserId);
		memberRepository.delete(member);
		
		governanceService.deleteDecisionMaker(initiative.getGovernance().getId(), member.getUser().getC1Id());
		
		return new PostResult("success", "contributor deleted", "");
	}
	
	@Transactional
	public GetResult<List<InitiativeDto>> searchBy(String q) {
		List<Initiative> initiatives = initiativeRepository.searchBy(q.toLowerCase());
		List<InitiativeDto> initiativesDtos = new ArrayList<InitiativeDto>();
		
		for(Initiative initiative : initiatives) {
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
}
