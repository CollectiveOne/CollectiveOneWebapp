package org.collectiveone.repositories;

import java.util.UUID;

import org.collectiveone.model.TokenType;
import org.springframework.data.repository.CrudRepository;

public interface TokenTypeRepositoryIf extends CrudRepository<TokenType, UUID> {
	
	TokenType findById(UUID id);
}
