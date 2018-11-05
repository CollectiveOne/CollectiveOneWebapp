package org.collectiveone.modules.model.repositories;

import java.util.Optional;
import java.util.UUID;

import org.collectiveone.modules.model.ModelCard;
import org.springframework.data.repository.CrudRepository;

public interface ModelCardRepositoryIf extends CrudRepository<ModelCard, UUID> {
	
	public Optional<ModelCard> findById(UUID cardId);
	
}
