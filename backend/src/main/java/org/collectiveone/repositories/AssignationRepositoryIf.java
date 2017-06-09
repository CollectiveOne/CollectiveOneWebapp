package org.collectiveone.repositories;

import java.util.UUID;

import org.collectiveone.model.basic.Assignation;
import org.springframework.data.repository.CrudRepository;

public interface AssignationRepositoryIf extends CrudRepository<Assignation, UUID> {

}
