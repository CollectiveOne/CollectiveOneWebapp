package org.collectiveone.modules.tokens.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.tokens.ModelSectionTransfer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ModelSectionTransferRepositoryIf extends CrudRepository<ModelSectionTransfer, UUID> {

	@Query("SELECT SUM(tx.value) FROM ModelSectionTransfer tx WHERE tx.tokenType.id = ?1 AND tx.from.id = ?2 AND tx.to.id = ?3")
	Double getTotalTransferredFromToInternal(UUID tokenType, UUID fromInitiativeId, UUID toInitiativeId);
	
	default double getTotalTransferredFromTo(UUID tokenType, UUID fromInitiativeId, UUID toInitiativeId) {
		Double res = getTotalTransferredFromToInternal(tokenType, fromInitiativeId, toInitiativeId);
		return res == null ? 0.0 : res.doubleValue();
	}
	
	ModelSectionTransfer findById(UUID id);
	
	List<ModelSectionTransfer> findByFrom_Id(UUID fromId, Pageable page);
	
	List<ModelSectionTransfer> findByAlsoInModelSections_Id(UUID fromId, Pageable page);

	@Query("SELECT tx FROM ModelSectionTransfer tx WHERE tx.from.id IN ?1")
	List<ModelSectionTransfer> findBySectionsId(List<UUID> sectionIds, Pageable page);
}
