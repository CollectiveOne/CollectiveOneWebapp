package org.collectiveone.modules.tokens.repositories;

import java.util.UUID;

import org.collectiveone.modules.tokens.TokenBurn;
import org.springframework.data.repository.CrudRepository;

public interface TokenBurnRepositoryIf extends CrudRepository<TokenBurn, UUID> {
	
}
