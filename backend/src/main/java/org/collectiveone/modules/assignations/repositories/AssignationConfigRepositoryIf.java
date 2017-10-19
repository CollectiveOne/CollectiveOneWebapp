package org.collectiveone.modules.assignations.repositories;

import java.util.UUID;

import org.collectiveone.modules.assignations.AssignationConfig;
import org.springframework.data.repository.CrudRepository;

public interface AssignationConfigRepositoryIf extends CrudRepository<AssignationConfig, UUID> {
}
