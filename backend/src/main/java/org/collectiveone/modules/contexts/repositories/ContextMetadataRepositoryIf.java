package org.collectiveone.modules.contexts.repositories;

import java.util.UUID;

import org.collectiveone.modules.contexts.entities.ContextMetadata;
import org.springframework.data.repository.CrudRepository;

public interface ContextMetadataRepositoryIf extends CrudRepository<ContextMetadata, UUID> {
	
}
