package org.collectiveone.modules.model.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.model.ModelCard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ModelCardRepositoryIf extends CrudRepository<ModelCard, UUID> {
	
	public ModelCard findById(UUID cardId);
	
	@Query("SELECT DISTINCT crd.id FROM ModelSection sec JOIN sec.cardsWrappers crd WHERE sec.id IN ?1")
	public List<UUID> findAllCardsIdsOfSections(List<UUID> sectionId);
	
}
