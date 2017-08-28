package org.collectiveone.modules.model.repositories;

import java.util.UUID;

import org.collectiveone.modules.model.ModelView;
import org.springframework.data.repository.CrudRepository;

public interface ModelViewRepositoryIf extends CrudRepository<ModelView, UUID> {
	
	public ModelView findById(UUID viewId);
	
}
