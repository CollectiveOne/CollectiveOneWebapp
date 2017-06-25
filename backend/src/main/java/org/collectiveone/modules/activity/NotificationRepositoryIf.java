package org.collectiveone.modules.activity;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface NotificationRepositoryIf extends CrudRepository<Notification, UUID> {

}
