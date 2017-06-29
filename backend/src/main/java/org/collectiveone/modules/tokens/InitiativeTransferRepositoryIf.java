package org.collectiveone.modules.tokens;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface InitiativeTransferRepositoryIf extends CrudRepository<InitiativeTransfer, UUID> {

	@Query("SELECT SUM(tx.value) FROM InitiativeTransfer tx WHERE tx.tokenType.id = ?1 AND tx.relationship.id = ?2")
	Double getTotalTransferred(UUID tokenType, UUID relationshipId);
	
	InitiativeTransfer findById(UUID id);
	
}
