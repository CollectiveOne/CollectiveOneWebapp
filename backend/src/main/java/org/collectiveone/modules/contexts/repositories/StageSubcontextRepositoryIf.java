package org.collectiveone.modules.contexts.repositories;

import java.util.UUID;

import org.collectiveone.modules.contexts.entities.StageSubcontext;
import org.springframework.data.repository.CrudRepository;

public interface StageSubcontextRepositoryIf extends CrudRepository<StageSubcontext, UUID> {
	
}
