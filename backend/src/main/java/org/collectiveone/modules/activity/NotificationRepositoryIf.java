package org.collectiveone.modules.activity;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface NotificationRepositoryIf extends CrudRepository<Notification, UUID> {

	List<Notification> findBySubscriber_User_C1Id(UUID userId);
}
