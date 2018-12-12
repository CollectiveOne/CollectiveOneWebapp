package org.collectiveone.modules.contexts.repositories;

import java.util.UUID;

import org.collectiveone.modules.contexts.entities.Context;
import org.springframework.data.repository.CrudRepository;

public interface ContextRepositoryIf extends CrudRepository<Context, UUID> {

	public Context findById(UUID id);
	
	
}
