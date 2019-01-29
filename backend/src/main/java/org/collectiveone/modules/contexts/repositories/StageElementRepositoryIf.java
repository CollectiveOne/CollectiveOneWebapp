package org.collectiveone.modules.contexts.repositories;

import java.util.UUID;

import org.collectiveone.modules.contexts.entities.StageElement;
import org.springframework.data.repository.CrudRepository;

public interface StageElementRepositoryIf extends CrudRepository<StageElement, UUID> {
	
}
