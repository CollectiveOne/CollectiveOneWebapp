package org.collectiveone.services;

import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.model.Initiative;
import org.collectiveone.model.TokenHolder;
import org.collectiveone.model.TokenType;
import org.collectiveone.repositories.InitiativeRepositoryIf;
import org.collectiveone.repositories.TokenHolderRepositoryIf;
import org.collectiveone.repositories.TokenTypeRepositoryIf;
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
		/* get or create TokenHolder row in token holders database */
		TokenHolder holder = tokenHolderRepository.findByTokenTypeIdAndHolderId(tokenTypeId, holderId);
		
		if(holder == null) {
			/* add new holder row with this ID */
			TokenType tokenType = tokenTypeRepository.findById(tokenTypeId);
			
			holder = new TokenHolder();
			holder.setHolderId(holderId);
			holder.setTokens(0.0);
			holder.setTokenType(tokenType);
			
			holder = tokenHolderRepository.save(holder);			
		}
		
		return holder;
	}
	
	@Transactional
	public String mintToInitiative(UUID tokenId, UUID initiativeId, double value) {
		/* and assign a pool of its own tokens to this initiative */
		
		TokenType token = tokenTypeRepository.findById(tokenId);
		Initiative initiative = initiativeRepository.findById(initiativeId);
		
		TokenHolder holder = getHolder(token.getId(), initiative.getId());
		holder.setTokens(holder.getTokens() + value);
		
		tokenHolderRepository.save(holder);
		
		return "success";
		
	}
	
}
