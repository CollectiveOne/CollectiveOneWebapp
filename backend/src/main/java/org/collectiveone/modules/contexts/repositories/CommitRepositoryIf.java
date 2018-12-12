package org.collectiveone.modules.contexts.repositories;

import java.util.UUID;

import org.collectiveone.modules.contexts.entities.Commit;
import org.springframework.data.repository.CrudRepository;

public interface CommitRepositoryIf extends CrudRepository<Commit, UUID> {

	public Commit findById(UUID id);
	
}
