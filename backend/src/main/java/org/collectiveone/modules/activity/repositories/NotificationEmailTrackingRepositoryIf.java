package org.collectiveone.modules.activity.repositories;

import java.util.UUID;

import org.collectiveone.modules.activity.NotificationEmailTracking;
import org.collectiveone.modules.activity.enums.NotificationTrackingType;
import org.springframework.data.repository.CrudRepository;

public interface NotificationEmailTrackingRepositoryIf extends CrudRepository<NotificationEmailTracking, UUID> {

	NotificationEmailTracking findByType(NotificationTrackingType type);
}
