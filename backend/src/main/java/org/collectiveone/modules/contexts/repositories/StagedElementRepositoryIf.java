package org.collectiveone.modules.contexts.repositories;

import java.util.UUID;

import org.collectiveone.modules.contexts.entities.StagedElement;
import org.springframework.data.repository.CrudRepository;

public interface StagedElementRepositoryIf extends CrudRepository<StagedElement, UUID> {
	
}
