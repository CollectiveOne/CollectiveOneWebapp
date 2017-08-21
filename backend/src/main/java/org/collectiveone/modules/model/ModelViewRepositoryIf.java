package org.collectiveone.modules.model;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface ModelViewRepositoryIf extends CrudRepository<ModelView, UUID> {
	
}
