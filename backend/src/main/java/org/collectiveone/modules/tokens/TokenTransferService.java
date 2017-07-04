package org.collectiveone.modules.tokens;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.InitiativeRelationship;
import org.collectiveone.modules.initiatives.InitiativeRelationshipRepositoryIf;
import org.collectiveone.modules.initiatives.InitiativeRelationshipType;
import org.collectiveone.modules.initiatives.InitiativeRepositoryIf;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.collectiveone.modules.initiatives.Member;
import org.collectiveone.modules.initiatives.MemberRepositoryIf;
import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenTransferService {
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private InitiativeService initiativeService;
	
	@Autowired
	private InitiativeRepositoryIf initiativeRepository;
	
	@Autowired
	private AppUserRepositoryIf appUserRepository;
	
	@Autowired
	private InitiativeTransferRepositoryIf initiativeTransferRepository;
	
	@Autowired
	private MemberTransferRepositoryIf memberTransferRepository;
	
	@Autowired
	private MemberRepositoryIf memberRepository;
	
	@Autowired
	private InitiativeRelationshipRepositoryIf initiativeRelationshipRepository;
	
	
	
	/** Get the distribution of an asset starting from a given initiative
	 * and including the tokens transferred to its sub-initiatives and members */
	@Transactional
	public AssetsDto getTokenDistribution(UUID tokenId, UUID initiativeId) {

		Initiative initiative = initiativeRepository.findById(initiativeId); 
		AssetsDto assetDto = tokenService.getTokensOfHolderDto(tokenId, initiative.getId());
		assetDto.setHolderName(initiative.getName());
		
		assetDto.setTransferredToSubinitiatives(getTransferredToSubinitiatives(tokenId, initiative.getId()));
		assetDto.setTransferredToUsers(getTransferredToUsers(tokenId, initiative.getId()));
		
		/* sum all tranfers as additional data */
		assetDto.setTotalTransferredToSubinitiatives(0.0);
		for (TransferDto transfer : assetDto.getTransferredToSubinitiatives()) {
			assetDto.setTotalTransferredToSubinitiatives(assetDto.getTotalTransferredToSubinitiatives() + transfer.getValue());
		}
		
		assetDto.setTotalTransferredToUsers(0.0);
		for (TransferDto transfer : assetDto.getTransferredToUsers()) {
			assetDto.setTotalTransferredToUsers(assetDto.getTotalTransferredToUsers() + transfer.getValue());
		}
		
		assetDto.setTotalUnderThisHolder(
				assetDto.getOwnedByThisHolder() + 
				assetDto.getTotalTransferredToSubinitiatives() + 
				assetDto.getTotalTransferredToUsers());
		
		
		return assetDto;
	}
	
	@Transactional
	public PostResult transferFromInitiativeToUser(UUID initiativeId, TransferDto transfer) {
		return transferFromInitiativeToUser(initiativeId, UUID.fromString(transfer.getReceiverId()), UUID.fromString(transfer.getAssetId()), transfer.getValue());
	}
	
	@Transactional
	public PostResult transferFromInitiativeToUser(UUID initiativeId, UUID receiverId, UUID assetId, double value) {
		AppUser receiver = appUserRepository.findByC1Id(receiverId);
		TokenType tokenType = tokenService.getTokenType(assetId);
		
		tokenService.transfer(
				tokenType.getId(), 
				initiativeId, 
				receiver.getC1Id(), 
				value, 
				TokenHolderType.USER);
		
		/* register the transfer to the contributor  */
		Member member = initiativeService.getOrAddMember(initiativeId, receiver.getC1Id());
		
		MemberTransfer thisTransfer = new MemberTransfer();
		thisTransfer.setMember(member);
		thisTransfer.setTokenType(tokenType);
		thisTransfer.setValue(value);
		
		memberTransferRepository.save(thisTransfer);
		member.getTokensTransfers().add(thisTransfer);
		
		memberRepository.save(member);
	
		return new PostResult("success", "assets transferred successfully", "");
	}
	
	@Transactional
	public PostResult transferFromInitiativeToInitiative(UUID fromInitiativeId, TransferDto transferDto) {
		return transferFromInitiativeToInitiative(
				fromInitiativeId, 
				UUID.fromString(transferDto.getReceiverId()), 
				UUID.fromString(transferDto.getAssetId()), 
				transferDto.getValue(), 
				transferDto.getMotive(), 
				transferDto.getNotes());
	}
	
	@Transactional
	public PostResult transferFromInitiativeToInitiative(UUID fromInitiativeId, UUID toInitiativeId, UUID assetId, double value, String motive, String notes) {
		
		TokenType tokenType = tokenService.getTokenType(assetId);
		Initiative from = initiativeRepository.findById(fromInitiativeId);
		Initiative to = initiativeRepository.findById(toInitiativeId);
		
		tokenService.transfer(
				tokenType.getId(), 
				from.getId(), 
				to.getId(), 
				value, 
				TokenHolderType.INITIATIVE);
		
		/* register the transfer to the initiative  */
		InitiativeTransfer transfer = new InitiativeTransfer();
		transfer.setTokenType(tokenType);
		transfer.setFrom(from);
		transfer.setTo(to);
		transfer.setMotive(motive);
		transfer.setNotes(notes);
		transfer.setValue(value);
		
		transfer = initiativeTransferRepository.save(transfer);
		
		return new PostResult("success", "transfer saved", transfer.getId().toString());
		
	}
	
	
	/** Get the tokens transfers from one initiative to any other initiatives */
	@Transactional
	public InitiativeTransfersDto getTransfersToSubInitiatives(UUID initiativeId) {
		
		Initiative initiative = initiativeRepository.findById(initiativeId);
		
		InitiativeTransfersDto initiativeTransfers = new InitiativeTransfersDto();
		
		initiativeTransfers.setInitiativeId(initiative.getId().toString());
		initiativeTransfers.setInitiativeName(initiative.getName());
		
		for (InitiativeTransfer transfer : initiativeTransferRepository.findByFrom_Id(initiativeId)) {
			initiativeTransfers.getTransfers().add(transfer.toDto());
		}
		
		for (Initiative subInitiative : initiativeRepository.findInitiativesWithRelationship(initiative.getId(), InitiativeRelationshipType.IS_DETACHED_SUB)) {
			/** Recursive */
			initiativeTransfers.getSubinitiativesTransfers().add(getTransfersToSubInitiatives(subInitiative.getId()));
		}
		
		return initiativeTransfers;
	}
	
	/** Get the tokens transferred from one initiative into its sub-initiatives */
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
			Double totalTransferred = initiativeTransferRepository.getTotalTransferredFromTo(tokenId, relationship.getOfInitiative().getId(), relationship.getInitiative().getId());
			
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
	
	/** Get the tokens transferred from one initiative to its members */
	@Transactional
	public List<TransferDto> getTransferredToUsers(UUID tokenId, UUID initiativeId) {
		Initiative initiative = initiativeRepository.findById(initiativeId); 
		TokenType token = tokenService.getTokenType(tokenId);

		List<TransferDto> transferredToUsers = new ArrayList<TransferDto>();
		for (Member member : initiative.getMembers()) {
			/* get all transfers of a given token made from an initiative to a contributor */
			double totalTransferred = memberTransferRepository.getTotalTransferred(tokenId, member.getId());
			
			TransferDto dto = new TransferDto();
			
			dto.setAssetId(token.getId().toString());
			dto.setAssetName(token.getName());
			dto.setSenderId(initiative.getId().toString());
			dto.setSenderName(initiative.getName());
			dto.setReceiverId(member.getUser().getC1Id().toString());
			dto.setReceiverName(member.getUser().getNickname());
			dto.setValue(totalTransferred);
			
			transferredToUsers.add(dto);
		}
		
		return transferredToUsers;
	}
}
