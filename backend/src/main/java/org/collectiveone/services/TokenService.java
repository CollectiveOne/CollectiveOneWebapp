package org.collectiveone.services;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.model.TokenHolder;
import org.collectiveone.model.TokenType;
import org.collectiveone.model.enums.TokenHolderType;
import org.collectiveone.repositories.InitiativeRepositoryIf;
import org.collectiveone.repositories.TokenHolderRepositoryIf;
import org.collectiveone.repositories.TokenTypeRepositoryIf;
import org.collectiveone.web.dto.AssetsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

	@Autowired
	InitiativeRepositoryIf initiativeRepository;
	
	@Autowired
	TokenTypeRepositoryIf tokenTypeRepository;
	
	@Autowired
	TokenHolderRepositoryIf tokenHolderRepository;
	
	@Transactional
	public TokenType create(String name, String symbol) {
		/* if no parent initiative is provided, create a new token type */
		TokenType token = new TokenType();
		token.setName(name);
		token.setSymbol(symbol);
		token = tokenTypeRepository.save(token);
		
		return token;
	}
	
	@Transactional
	public TokenHolder getHolder(UUID tokenTypeId, UUID holderId) {
		/* get TokenHolder row in token holders database */
		return tokenHolderRepository.findByTokenTypeIdAndHolderId(tokenTypeId, holderId);
	}
	
	@Transactional
	public TokenHolder getOrCreateHolder(UUID tokenTypeId, UUID holderId, TokenHolderType holderType) {
		/* get or create TokenHolder row in token holders database */
		TokenHolder holder = tokenHolderRepository.findByTokenTypeIdAndHolderId(tokenTypeId, holderId);
		
		if(holder == null) {
			/* add new holder row with this ID */
			TokenType tokenType = tokenTypeRepository.findById(tokenTypeId);
			
			holder = new TokenHolder();
			holder.setHolderId(holderId);
			holder.setHolderType(holderType);
			holder.setTokens(0.0);
			holder.setTokenType(tokenType);
			
			holder = tokenHolderRepository.save(holder);			
		}
		
		return holder;
	}
	
	@Transactional
	public String mintToHolder(UUID tokenId, UUID holderId, double value, TokenHolderType holderType) {
		/* and assign a pool of its own tokens to this initiative */
		
		TokenType token = tokenTypeRepository.findById(tokenId);
		TokenHolder holder = getOrCreateHolder(token.getId(), holderId, holderType);
		holder.setTokens(holder.getTokens() + value);
		
		tokenHolderRepository.save(holder);
		
		return "success";
		
	}
	
	@Transactional
	public String transfer(UUID tokenId, UUID fromHolderId, UUID toHolderId, double value, TokenHolderType holderType) {
		/* and assign a pool of its own tokens to this initiative */
		
		TokenType token = tokenTypeRepository.findById(tokenId);
		
		TokenHolder fromHolder = getHolder(token.getId(), fromHolderId);
		TokenHolder toHolder = getOrCreateHolder(token.getId(), toHolderId, holderType);
		
		if(fromHolder.getTokens() >= value) {
			fromHolder.setTokens(fromHolder.getTokens() - value);
			toHolder.setTokens(toHolder.getTokens() + value);
			
			tokenHolderRepository.save(fromHolder);
			tokenHolderRepository.save(toHolder);
			
			return "success";
			
		} else {
			return "not enough tokens";
		}
		
	}

	@Transactional
	public double getTotalExisting(UUID tokenId) {
		Double res = tokenTypeRepository.totalExisting(tokenId);
		if (res != null) {
			return res;
		} else {
			return 0.0;
		}
	}
	
	@Transactional
	public double getTotalAssignedToOtherInitiatives(UUID tokenId, UUID initiativeId) {
		Double res = tokenTypeRepository.getTotalAssignedToOther(tokenId, initiativeId, TokenHolderType.INITIATIVE);
		if (res != null) {
			return res;
		} else {
			return 0.0;
		}
	}
	
	@Transactional
	public double getTotalAssignedToUsers(UUID tokenId) {
		Double res = tokenTypeRepository.getTotalAssignedToHolderType(tokenId, TokenHolderType.USER);
		if (res != null) {
			return res;
		} else {
			return 0.0;
		}
	}
	
	public List<TokenType> getTokenTypesHeldBy(UUID holderId) {
		return tokenHolderRepository.getTokenTypesHeldBy(holderId);
	}
	
	public List<TokenType> getTokenTypesHeldByOtherThan(UUID holderId, UUID tokenId) {
		return tokenHolderRepository.getTokenTypesHeldByOtherThan(holderId, tokenId);
	}
	
	@Transactional
	public AssetsDto getTokensOfHolderDto(UUID tokenTypeId, UUID holderId) {
		
		TokenType token = tokenTypeRepository.findById(tokenTypeId);
		AssetsDto assetsDto = new AssetsDto();
		
		/* token info */
		assetsDto.setAssetId(token.getId().toString());
		assetsDto.setAssetName(token.getName());
		
		/* Total amount of tokens in circulation */
		double existingTokens = getTotalExisting(token.getId());
		assetsDto.setTotalExistingTokens(existingTokens);
		
		/* Amount of tokens held by the input initiative */
		assetsDto.setHolderId(holderId.toString());
		assetsDto.setOwnedByThisHolder(getHolder(token.getId(), holderId).getTokens());
		
		return assetsDto;
	}

	
}
