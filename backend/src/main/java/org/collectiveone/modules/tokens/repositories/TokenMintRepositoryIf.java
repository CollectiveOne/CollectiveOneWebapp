package org.collectiveone.modules.tokens.repositories;

import java.util.UUID;

import org.collectiveone.modules.tokens.TokenMint;
import org.springframework.data.repository.CrudRepository;

public interface TokenMintRepositoryIf extends CrudRepository<TokenMint, UUID> {
	
}
