package org.collectiveone.repositories;

import java.util.UUID;

import org.collectiveone.model.support.MemberTransfer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MemberTransferRepositoryIf extends CrudRepository<MemberTransfer, UUID> {

	@Query("SELECT SUM(tx.value) FROM ContributorTransfer tx WHERE tx.tokenType.id = ?1 AND tx.contributor.id = ?2")
	Double getTotalTransferredInternal(UUID tokenTypeId, UUID contributorId);
	
	default double getTotalTransferred(UUID tokenTypeId, UUID contributorId) {
		Double res = getTotalTransferredInternal(tokenTypeId, contributorId);
		return res == null ? 0.0 : res.doubleValue();
	}
	
}
