package org.collectiveone.modules.contexts.repositories;

import java.util.UUID;

import org.collectiveone.modules.contexts.entities.Subcontext;
import org.springframework.data.repository.CrudRepository;

public interface SubcontextRepositoryIf extends CrudRepository<Subcontext, UUID> {

	public Subcontext findById(UUID id);
	
}
