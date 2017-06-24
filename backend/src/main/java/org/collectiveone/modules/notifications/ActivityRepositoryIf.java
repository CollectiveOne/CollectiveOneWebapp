package org.collectiveone.modules.notifications;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface ActivityRepositoryIf extends CrudRepository<Activity, UUID> {

}
