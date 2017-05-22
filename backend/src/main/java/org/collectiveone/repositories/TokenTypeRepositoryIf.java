package org.collectiveone.repositories;

import java.util.UUID;

import org.collectiveone.model.basic.TokenType;
import org.collectiveone.model.enums.TokenHolderType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TokenTypeRepositoryIf extends CrudRepository<TokenType, UUID> {
	
	TokenType findById(UUID id);
	
	@Query("SELECT SUM(holder.tokens) FROM TokenHolder holder WHERE holder.tokenType.id = ?1")
	Double totalExisting(UUID id);
	
	@Query("SELECT SUM(holder.tokens) FROM TokenHolder holder WHERE (holder.tokenType.id = ?1 AND holder.holderId != ?2 AND holder.holderType = ?3)" )
	Double getTotalAssignedToOther(UUID tokenId, UUID initiativeId, TokenHolderType type);
	
	@Query("SELECT SUM(holder.tokens) FROM TokenHolder holder WHERE (holder.tokenType.id = ?1 AND holder.holderType = ?2)" )
	Double getTotalAssignedToHolderType(UUID tokenId, TokenHolderType type);

}
