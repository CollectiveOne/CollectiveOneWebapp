package org.collectiveone.modules.initiatives.repositories;

import java.util.UUID;

import org.collectiveone.modules.initiatives.InitiativeTag;
import org.springframework.data.repository.CrudRepository;

public interface InitiativeTagRepositoryIf extends CrudRepository<InitiativeTag, UUID> {

	InitiativeTag findById(UUID id);
	
	InitiativeTag findByTagText(String tagText);
	
}
