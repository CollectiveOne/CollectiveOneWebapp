package org.collectiveone.repositories;

import java.util.UUID;

import org.collectiveone.model.TokenType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TokenTypeRepositoryIf extends CrudRepository<TokenType, UUID> {
	
	TokenType findById(UUID id);
	
	@Query("SELECT SUM(holder.tokens) FROM TokenHolder holder WHERE holder.tokenType.id = ?1")
	double totalExisting(UUID id);
}
