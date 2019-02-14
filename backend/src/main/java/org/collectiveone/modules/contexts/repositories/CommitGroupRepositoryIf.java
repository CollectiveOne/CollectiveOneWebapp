package org.collectiveone.modules.contexts.repositories;

import java.util.UUID;

import org.collectiveone.modules.contexts.entities.CommitGroup;
import org.springframework.data.repository.CrudRepository;

public interface CommitGroupRepositoryIf extends CrudRepository<CommitGroup, UUID> {

}
