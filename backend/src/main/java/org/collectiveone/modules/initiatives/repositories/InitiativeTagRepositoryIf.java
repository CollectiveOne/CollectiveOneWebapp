package org.collectiveone.modules.initiatives.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.collectiveone.modules.initiatives.InitiativeTag;
import org.springframework.data.repository.CrudRepository;

public interface InitiativeTagRepositoryIf extends CrudRepository<InitiativeTag, UUID> {

	Optional<InitiativeTag> findById(UUID id);
	
	InitiativeTag findByTagText(String tagText);
	
	List<InitiativeTag> findTop10ByTagTextLikeIgnoreCase(String q);
	
}
