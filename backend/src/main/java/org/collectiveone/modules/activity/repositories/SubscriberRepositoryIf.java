package org.collectiveone.modules.activity.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.activity.Subscriber;
import org.collectiveone.modules.activity.enums.SubscriptionElementType;
import org.springframework.data.repository.CrudRepository;

public interface SubscriberRepositoryIf extends CrudRepository<Subscriber, UUID> {
	
	List<Subscriber> findByElementId(UUID elementId);
	
	Subscriber findByElementIdAndTypeAndUser_C1Id(UUID elementId, SubscriptionElementType type, UUID userId);
	
	Subscriber findByUser_C1IdAndType(UUID userId, SubscriptionElementType type);

}
