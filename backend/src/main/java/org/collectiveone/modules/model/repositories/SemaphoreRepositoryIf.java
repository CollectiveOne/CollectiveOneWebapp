package org.collectiveone.modules.model.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.model.ElementSemaphore;
import org.springframework.data.repository.CrudRepository;

public interface SemaphoreRepositoryIf extends CrudRepository<ElementSemaphore, UUID> {
	
	List<ElementSemaphore> findByElementId(UUID elementId);
	
	ElementSemaphore findByElementIdAndAuthor_c1Id(UUID elementId, UUID authorC1Id);
	
}
