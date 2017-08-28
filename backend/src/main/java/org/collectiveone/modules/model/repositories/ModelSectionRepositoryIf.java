package org.collectiveone.modules.model.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.model.ModelSection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ModelSectionRepositoryIf extends CrudRepository<ModelSection, UUID> {

	public ModelSection findById(UUID sectionId);
	
	@Query("SELECT section from ModelSection section JOIN section.subsections subsec WHERE subsec.id = ?1")
	public List<ModelSection> findParentSections(UUID sectionId);
}
