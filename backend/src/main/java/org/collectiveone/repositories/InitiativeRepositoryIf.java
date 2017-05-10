package org.collectiveone.repositories;

import java.util.UUID;

import org.collectiveone.model.Initiative;
import org.springframework.data.repository.CrudRepository;

public interface InitiativeRepositoryIf extends CrudRepository<Initiative, UUID> {

}
