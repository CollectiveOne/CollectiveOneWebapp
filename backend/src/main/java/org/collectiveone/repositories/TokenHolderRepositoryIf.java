package org.collectiveone.repositories;

import java.util.UUID;

import org.collectiveone.model.TokenHolder;
import org.springframework.data.repository.CrudRepository;

public interface TokenHolderRepositoryIf extends CrudRepository<TokenHolder, UUID> {
	
	TokenHolder findByTokenTypeIdAndHolderId(UUID tokenTypeId, UUID holderId);
}
