package org.collectiveone.modules.users;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface PushSubscriptionRepositoryIf extends CrudRepository<PushSubscription, UUID> {
}
