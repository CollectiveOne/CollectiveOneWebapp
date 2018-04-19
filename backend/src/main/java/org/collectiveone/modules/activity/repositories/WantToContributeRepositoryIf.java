package org.collectiveone.modules.activity.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.activity.WantToContributeNotification;
import org.collectiveone.modules.activity.enums.NotificationState;
import org.springframework.data.repository.CrudRepository;

public interface WantToContributeRepositoryIf extends CrudRepository<WantToContributeNotification, UUID> {

	List<WantToContributeNotification> findByEmailState(NotificationState notificationEmailState);
}
