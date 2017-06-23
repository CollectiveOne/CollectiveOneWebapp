package org.collectiveone.modules.initiatives;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface InitiativeRelationshipRepositoryIf extends CrudRepository<InitiativeRelationship, UUID> {
	
	List<InitiativeRelationship> findByOfInitiativeIdAndType(UUID ofInitiativeId, InitiativeRelationshipType type);
		
}
