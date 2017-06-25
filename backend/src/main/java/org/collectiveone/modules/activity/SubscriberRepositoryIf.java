package org.collectiveone.modules.activity;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface SubscriberRepositoryIf extends CrudRepository<Subscriber, UUID> {
	
	List<Subscriber> findByElementId(UUID elementId);

}
