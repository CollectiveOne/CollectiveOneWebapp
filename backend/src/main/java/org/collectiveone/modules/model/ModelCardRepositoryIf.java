package org.collectiveone.modules.model;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface ModelCardRepositoryIf extends CrudRepository<ModelCard, UUID> {
	
}
