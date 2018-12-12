package org.collectiveone.modules.contexts.repositories;

import java.util.UUID;

import org.collectiveone.modules.contexts.entities.StageCard;
import org.springframework.data.repository.CrudRepository;

public interface StageCardRepositoryIf extends CrudRepository<StageCard, UUID> {
	
}
