package org.collectiveone.modules.tokens.repositories;

import java.util.Optional;
import java.util.UUID;

import org.collectiveone.modules.tokens.MemberTransfer;
import org.collectiveone.modules.tokens.enums.MemberTransferStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MemberTransferRepositoryIf extends CrudRepository<MemberTransfer, UUID> {

	Optional<MemberTransfer> findById(UUID id);
	
	@Query("SELECT SUM(tx.value) FROM MemberTransfer tx WHERE tx.tokenType.id = ?1 AND tx.member.id = ?2 AND tx.status = ?3")
	Double getTotalTransferredInternal(UUID tokenTypeId, UUID contributorId, MemberTransferStatus status);
	
	default double getTotalTransferred(UUID tokenTypeId, UUID contributorId) {
		Double res = getTotalTransferredInternal(tokenTypeId, contributorId, MemberTransferStatus.DONE);
		return res == null ? 0.0 : res.doubleValue();
	}
	
}
