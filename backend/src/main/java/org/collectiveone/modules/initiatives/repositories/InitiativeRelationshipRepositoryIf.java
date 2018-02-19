package org.collectiveone.modules.initiatives.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.initiatives.InitiativeRelationship;
import org.collectiveone.modules.initiatives.InitiativeRelationshipType;
import org.collectiveone.modules.initiatives.InitiativeStatus;
import org.springframework.data.repository.CrudRepository;

public interface InitiativeRelationshipRepositoryIf extends CrudRepository<InitiativeRelationship, UUID> {
	
	List<InitiativeRelationship> findByOfInitiativeIdAndInitiative_StatusAndType(UUID ofInitiativeId, InitiativeStatus status, InitiativeRelationshipType type);
		
}
