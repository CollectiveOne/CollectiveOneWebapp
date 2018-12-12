package org.collectiveone.modules.contexts.repositories;

import java.util.UUID;

import org.collectiveone.modules.contexts.entities.StageMetadata;
import org.springframework.data.repository.CrudRepository;

public interface StageMetadataRepositoryIf extends CrudRepository<StageMetadata, UUID> {
	
}
