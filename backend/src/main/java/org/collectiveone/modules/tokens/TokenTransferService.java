package org.collectiveone.modules.tokens;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.activity.ActivityService;
import org.collectiveone.modules.assignations.Assignation;
import org.collectiveone.modules.assignations.AssignationService;
import org.collectiveone.modules.assignations.Bill;
import org.collectiveone.modules.model.Member;
import org.collectiveone.modules.model.ModelSection;
import org.collectiveone.modules.model.ModelService;
import org.collectiveone.modules.model.repositories.MemberRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelSectionRepositoryIf;
import org.collectiveone.modules.tokens.dto.AssetsDto;
import org.collectiveone.modules.tokens.dto.TokenMintDto;
import org.collectiveone.modules.tokens.dto.TransferDto;
import org.collectiveone.modules.tokens.enums.MemberTransferStatus;
import org.collectiveone.modules.tokens.enums.TokenHolderType;
import org.collectiveone.modules.tokens.repositories.ModelSectionTransferRepositoryIf;
import org.collectiveone.modules.tokens.repositories.MemberTransferRepositoryIf;
import org.collectiveone.modules.tokens.repositories.TokenMintRepositoryIf;
import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class TokenTransferService {

	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private ModelService modelService;
	
	@Autowired
	private AssignationService assignationService;
	
	@Autowired
	private ModelSectionRepositoryIf modelSectionRepository;
	
	@Autowired
	private AppUserRepositoryIf appUserRepository;
	
	@Autowired
	private ModelSectionTransferRepositoryIf modelSectionTransferRepository;
	
	@Autowired
	private MemberTransferRepositoryIf memberTransferRepository;
	
	@Autowired
	private MemberRepositoryIf memberRepository;
	
	@Autowired
	private InitiativeRelationshipRepositoryIf initiativeRelationshipRepository;
	
	@Autowired
	private TokenMintRepositoryIf tokenMintRespository;
	
	
	
	/** Get the distribution of an asset starting from a given initiative
	 * and including the tokens transferred to its sub-initiatives and members */
	@Transactional
	public AssetsDto getTokenDistribution(UUID tokenId, UUID modelSectionId) {

		ModelSection modelSection = modelSectionRepository.findById(modelSectionId); 
		AssetsDto assetDto = tokenService.getTokensOfHolderDto(tokenId, modelSection.getId());
		assetDto.setHolderName(modelSection.getTitle());
		
		//####assetDto.setTotalTransferredToSubModelSections(getTransferredToSubModelSections(tokenId, modelSection.getId()));
		assetDto.setTransferredToUsers(getTransferredToUsers(tokenId, modelSection.getId()));
		assetDto.setTransfersPending(getTransfersPending(modelSection.getId()));
		
		/* sum all tranfers as additional data */
		assetDto.setTotalTransferredToSubModelSections(0.0);
		for (TransferDto transfer : assetDto.getTransferredToModelSections()) {
			assetDto.setTotalTransferredToSubModelSections(assetDto.getTotalTransferredToSubModelSections() + transfer.getValue());
		}
		
		assetDto.setTotalTransferredToUsers(0.0);
		for (TransferDto transfer : assetDto.getTransferredToUsers()) {
			assetDto.setTotalTransferredToUsers(assetDto.getTotalTransferredToUsers() + transfer.getValue());
		}
		
		assetDto.setTotalPending(0.0);
		for (TransferDto transfer : assetDto.getTransfersPending()) {
			assetDto.setTotalPending(assetDto.getTotalPending() + transfer.getValue());
		}
		
		assetDto.setTotalUnderThisHolder(
				assetDto.getOwnedByThisHolder() + 
				assetDto.getTotalTransferredToSubModelSections() + 
				assetDto.getTotalTransferredToUsers());
		
		
		return assetDto;
	}
	
	@Transactional
	public String mintToInitiative(UUID tokenId, UUID initiativeId, UUID orderByUserId, TokenMintDto mintDto) {
		
		String result = tokenService.mintToHolder(tokenId, initiativeId, mintDto.getValue(), TokenHolderType.MODEL_SECTION);
		
		if (result.equals("success")) {
			AppUser orderedBy = appUserRepository.findByC1Id(orderByUserId);
			
			TokenMint mint = new TokenMint();
			mint.setToken(tokenService.getTokenType(tokenId));
			mint.setOrderedBy(orderedBy);
			mint.setToHolder(initiativeId);
			mint.setMotive(mintDto.getMotive());
			mint.setNotes(mintDto.getNotes());
			mint.setValue(mintDto.getValue());
			
			mint = tokenMintRespository.save(mint);
			
			activityService.tokensMinted(modelSectionRepository.findById(initiativeId), mint);
		}
		
		return result;
		
	}
	
	@Transactional
	public PostResult transferFromModelSectionToUser(UUID modelSectionId, TransferDto transfer) {
		return transferFromModelSectionToUser(modelSectionId, UUID.fromString(transfer.getReceiverId()), UUID.fromString(transfer.getAssetId()), transfer.getValue());
	}
	
	@Transactional
	public PostResult transferFromModelSectionToUser(UUID modelSectionId, UUID receiverId, UUID assetId, double value) {
		AppUser receiver = appUserRepository.findByC1Id(receiverId);
		TokenType tokenType = tokenService.getTokenType(assetId);
		
		String result = tokenService.transfer(
				tokenType.getId(), 
				modelSectionId, 
				receiver.getC1Id(), 
				value, 
				TokenHolderType.USER);
		
		if (result.equals("success")) {
			/* register the transfer to the contributor  */
			Member member = modelService.getOrAddMember(modelSectionId, receiver.getC1Id());
			
			MemberTransfer thisTransfer = new MemberTransfer();
			thisTransfer.setMember(member);
			thisTransfer.setTokenType(tokenType);
			thisTransfer.setValue(value);
			thisTransfer.setStatus(MemberTransferStatus.DONE);
			
			thisTransfer = memberTransferRepository.save(thisTransfer);
			member.getTokensTransfers().add(thisTransfer);
			
			memberRepository.save(member);
			
			return new PostResult("success", "assets transferred successfully", thisTransfer.getId().toString());
		} else {
			return new PostResult("error", "error transferring assets: " + result, "");
		}
	}
	
	@Transactional
	public void revertTransferFromModelSectionToUser(UUID transferId) {
		MemberTransfer transfer = memberTransferRepository.findById(transferId);
		
		String result = tokenService.transfer(
				transfer.getTokenType().getId(), 
				transfer.getMember().getUser().getC1Id(), 
				transfer.getMember().getModelSection().getId(), 
				transfer.getValue(), 
				TokenHolderType.MODEL_SECTION);
		
		if (result.equals("success")) {
			transfer.setStatus(MemberTransferStatus.REVERTED);
		}
		
	}
	
	@Transactional
	public PostResult transferFromModelSectionToModelSection(UUID fromModelSectionId, TransferDto transferDto, UUID orderByUserId) {
		return transferFromModelSectionToModelSection(
			fromModelSectionId, 
				UUID.fromString(transferDto.getReceiverId()),
				orderByUserId,
				UUID.fromString(transferDto.getAssetId()), 
				transferDto.getValue(), 
				transferDto.getMotive(), 
				transferDto.getNotes());
	}
	
	@Transactional
	public PostResult transferFromModelSectionToModelSection(UUID fromModelSectionId, UUID toModelSectionId, UUID orderByUserId, UUID assetId, double value, String motive, String notes) {
		
		TokenType tokenType = tokenService.getTokenType(assetId);
		ModelSection from = modelSectionRepository.findById(fromModelSectionId);
		ModelSection to = modelSectionRepository.findById(toModelSectionId);
		
		String result = tokenService.transfer(
				tokenType.getId(), 
				from.getId(), 
				to.getId(), 
				value, 
				TokenHolderType.MODEL_SECTION);
		
		if (result.equals("success")) {
			/* register the transfer to the initiative  */
			ModelSectionTransfer transfer = new ModelSectionTransfer();
			transfer.setTokenType(tokenType);
			transfer.setFrom(from);
			transfer.setTo(to);
			transfer.setMotive(motive);
			transfer.setNotes(notes);
			transfer.setValue(value);
			transfer.setOrderDate(new Timestamp(System.currentTimeMillis()));
			transfer.setOrderedBy(appUserRepository.findByC1Id(orderByUserId));
			
			transfer = modelSectionTransferRepository.save(transfer);
			
			activityService.transferToSubModelSection(transfer);
			
			return new PostResult("success", "transfer done", transfer.getId().toString());
		} else {
			return new PostResult("success", "error making the transfer:" + result, "");
		}
		
	}
	
	
	@Transactional
	public PostResult transferAllFromInitiativeToInitiative(UUID fromInitiativeId, UUID toInitiativeId, UUID orderByUserId, String motive, String notes) {
		
		List<TokenType> tokenTypes = tokenService.getTokenTypesHeldBy(fromInitiativeId);
		
		for (TokenType tokenType : tokenTypes) {
			TokenHolder holder = tokenService.getHolder(tokenType.getId(), fromInitiativeId);
			transferFromModelSectionToModelSection(fromInitiativeId, toInitiativeId, orderByUserId, tokenType.getId(), holder.getTokens(), motive, notes);
		}
		
		return new PostResult("success", "all assets sent", "");
	}
	
	
	/** Get the tokens transfers from one initiative to any other initiatives */
	@Transactional
	public GetResult<List<TransferDto>> getTransfersFromModelSections(UUID modelSectionId, PageRequest page) {
		
		List<TransferDto> ModelSectionTransfers = new ArrayList<TransferDto>();
		
		for (ModelSectionTransfer transfer : modelSectionTransferRepository.findByFrom_Id(modelSectionId, page)) {
			ModelSectionTransfers.add(transfer.toDto());
		}
		
		return new GetResult<List<TransferDto>>("success", "transfers retrieved", ModelSectionTransfers);
	}
	
	/** Get the tokens transfers from one initiative to any other initiatives */
	@Transactional
	public GetResult<List<TransferDto>> getTransfersFromSubModelSections(UUID modelSectionId, PageRequest page) {
		
		List<TransferDto> modelSectionTransfers = new ArrayList<TransferDto>();
		
		for (ModelSectionTransfer transfer : modelSectionTransferRepository.findByAlsoInModelSections_Id(modelSectionId, page)) {
			modelSectionTransfers.add(transfer.toDto());
		}

		//#### sagar work this for cardwrapper case here need change logic to get all sub initiatives from getModelSection
		// List<UUID> allSectionIds = getAllSubsectionsIds(sectionId, level);
		
		return new GetResult<List<TransferDto>>("success", "transfers retrieved", modelSectionTransfers);
	}
	
	/** Get the tokens transferred from one initiative into its sub-initiatives */
	@Transactional
	public List<TransferDto> getTransferredToSubModelSections(UUID tokenId, UUID initiativeId) {
		ModelSection modelSection = modelSectionRepository.findById(initiativeId); 
		TokenType token = tokenService.getTokenType(tokenId);

		/* get of sub-initiatives */
		// #### here findByOfInitiativeIdAndInitiative_StatusAndType ?
		List<InitiativeRelationship> subinitiativesRelationships = 
				initiativeRelationshipRepository.findByOfInitiativeIdAndInitiative_StatusAndType(modelSection.getId(), InitiativeStatus.ENABLED, InitiativeRelationshipType.IS_ATTACHED_SUB);
		
		List<TransferDto> transferredToSubinitiatives = new ArrayList<TransferDto>();
		
		for (InitiativeRelationship relationship : subinitiativesRelationships) {
			/* get all transfers of a given token made from and to these initiatives */
			Double totalTransferred = modelSectionTransferRepository.getTotalTransferredFromTo(tokenId, relationship.getOfInitiative().getId(), relationship.getInitiative().getId());
			Double totalReturned = modelSectionTransferRepository.getTotalTransferredFromTo(tokenId, relationship.getInitiative().getId(), relationship.getOfInitiative().getId());
			
			TransferDto dto = new TransferDto();
			
			dto.setAssetId(token.getId().toString());
			dto.setAssetName(token.getName());
			dto.setSenderId(relationship.getOfInitiative().getId().toString());
			dto.setSenderName(relationship.getOfInitiative().getMeta().getName());
			dto.setReceiverId(relationship.getInitiative().getId().toString());
			dto.setReceiverName(relationship.getInitiative().getMeta().getName());
			dto.setValue(totalTransferred - totalReturned);
			
			transferredToSubinitiatives.add(dto);
		}
		
		return transferredToSubinitiatives;
	}
	
	/** Get the tokens transferred from one initiative to its members */
	@Transactional
	public List<TransferDto> getTransferredToUsers(UUID tokenId, UUID initiativeId) {
		ModelSection modelSection = modelSectionRepository.findById(initiativeId); 
		TokenType token = tokenService.getTokenType(tokenId);

		List<TransferDto> transferredToUsers = new ArrayList<TransferDto>();
		for (Member member : modelSection.getMembers()) {
			/* get all transfers of a given token made from an initiative to a contributor */
			double totalTransferred = memberTransferRepository.getTotalTransferred(tokenId, member.getId());
			
			TransferDto dto = new TransferDto();
			
			dto.setAssetId(token.getId().toString());
			dto.setAssetName(token.getName());
			dto.setSenderId(modelSection.getId().toString());
			dto.setSenderName(modelSection.getTitle());
			dto.setReceiverId(member.getUser().getC1Id().toString());
			dto.setReceiverName(member.getUser().getProfile().getNickname());
			dto.setValue(totalTransferred);
			
			transferredToUsers.add(dto);
		}
		
		return transferredToUsers;
	}
	
	@Transactional
	public List<TransferDto> getTransfersPending(UUID initiativeId) {
		ModelSection modelSection = modelSectionRepository.findById(initiativeId); 
		
		List<TransferDto> transfersPending = new ArrayList<TransferDto>();
		List<Assignation> assignations = assignationService.getOpenAssignations(initiativeId);
		
		for (Assignation assignation : assignations) {
			for (Bill bill : assignation.getBills()) {
				TransferDto dto = new TransferDto();
				
				dto.setAssetId(bill.getTokenType().getId().toString());
				dto.setAssetName(bill.getTokenType().getName());
				dto.setSenderId(modelSection.getId().toString());
				dto.setSenderName(modelSection.getTitle());
				dto.setValue(bill.getValue());
				
				transfersPending.add(dto);
			}
		}
		
		return transfersPending;
	}
	
}
