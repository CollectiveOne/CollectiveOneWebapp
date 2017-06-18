package org.collectiveone.modules.decisions.repositories;

import java.util.UUID;

import org.collectiveone.modules.assignations.model.Bill;
import org.springframework.data.repository.CrudRepository;

public interface DecisionRepositoryIf extends CrudRepository<Bill, UUID> {

}
