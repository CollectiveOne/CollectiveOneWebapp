package org.collectiveone.repositories;

import java.util.UUID;

import org.collectiveone.model.InitiativeRelationship;
import org.springframework.data.repository.CrudRepository;

public interface InitiativeRelationshipRepositoryIf extends CrudRepository<InitiativeRelationship, UUID> {
}
