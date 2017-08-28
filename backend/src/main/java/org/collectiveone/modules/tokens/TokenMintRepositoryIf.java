package org.collectiveone.modules.tokens;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface TokenMintRepositoryIf extends CrudRepository<TokenMint, UUID> {
	
}
