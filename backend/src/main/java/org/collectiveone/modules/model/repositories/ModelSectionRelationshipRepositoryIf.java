package org.collectiveone.modules.model.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.initiatives.InitiativeRelationship;
import org.collectiveone.modules.initiatives.InitiativeRelationshipType;
import org.collectiveone.modules.initiatives.InitiativeStatus;
import org.collectiveone.modules.model.ModelSectionRelationship;
import org.collectiveone.modules.model.ModelSectionRelationshipType;
import org.collectiveone.modules.model.ModelSectionStatus;
import org.springframework.data.repository.CrudRepository;

public interface ModelSectionRelationshipRepositoryIf extends CrudRepository<InitiativeRelationship, UUID> {
	
	List<ModelSectionRelationship> findByOfInitiativeIdAndInitiative_StatusAndType(UUID ofInitiativeId, ModelSectionStatus status, ModelSectionRelationshipType type);
		
}
