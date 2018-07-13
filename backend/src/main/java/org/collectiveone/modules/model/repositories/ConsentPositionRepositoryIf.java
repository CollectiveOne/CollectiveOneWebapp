package org.collectiveone.modules.model.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.model.ElementConsentPosition;
import org.springframework.data.repository.CrudRepository;

public interface ConsentPositionRepositoryIf extends CrudRepository<ElementConsentPosition, UUID> {
	
	List<ElementConsentPosition> findByElementId(UUID elementId);
	
	ElementConsentPosition findByElementIdAndAuthor_c1Id(UUID elementId, UUID authorC1Id);
	
}
