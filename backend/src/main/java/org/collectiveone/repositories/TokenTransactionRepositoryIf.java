package org.collectiveone.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.model.basic.TokenTransaction;
import org.collectiveone.model.enums.TokenHolderType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TokenTransactionRepositoryIf extends CrudRepository<TokenTransaction, UUID> {
	@Query("SELECT DISTINCT(tx.toHolderId) FROM TokenTransaction tx JOIN TokenHolder holder ON tx.fromHolderId = holder.holderId WHERE tx.tokenType = ?1 AND tx.fromHolderId = ?2 AND holder.type = ?3")
	List<UUID> findReceiverIds(UUID tokenId, UUID senderId, TokenHolderType type);
	
	@Query("SELECT SUM(tx.value) FROM TokenTransaction tx WHERE tx.tokenType = ?1 AND tx.fromHolderId = ?2 AND tx.toHolderId = ?3")
	Double getTotalTransferredTo(UUID tokenId, UUID senderId, UUID receiverId);
}
