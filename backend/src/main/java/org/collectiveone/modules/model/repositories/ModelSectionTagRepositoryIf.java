package org.collectiveone.modules.model.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.initiatives.InitiativeTag;
import org.collectiveone.modules.model.ModelSectionTag;
import org.springframework.data.repository.CrudRepository;

public interface ModelSectionTagRepositoryIf extends CrudRepository<InitiativeTag, UUID> {

	ModelSectionTag findById(UUID id);
	
	ModelSectionTag findByTagText(String tagText);
	
	List<ModelSectionTag> findTop10ByTagTextLikeIgnoreCase(String q);
	
}
