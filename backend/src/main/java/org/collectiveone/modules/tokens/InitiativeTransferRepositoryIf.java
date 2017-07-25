package org.collectiveone.modules.tokens;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface InitiativeTransferRepositoryIf extends CrudRepository<InitiativeTransfer, UUID> {

	@Query("SELECT SUM(tx.value) FROM InitiativeTransfer tx WHERE tx.tokenType.id = ?1 AND tx.from.id = ?2 AND tx.to.id = ?3")
	Double getTotalTransferredFromToInternal(UUID tokenType, UUID fromInitiativeId, UUID toInitiativeId);
	
	default double getTotalTransferredFromTo(UUID tokenType, UUID fromInitiativeId, UUID toInitiativeId) {
		Double res = getTotalTransferredFromToInternal(tokenType, fromInitiativeId, toInitiativeId);
		return res == null ? 0.0 : res.doubleValue();
	}
	
	InitiativeTransfer findById(UUID id);
	
	List<InitiativeTransfer> findByFrom_Id(UUID fromId);
	
}
