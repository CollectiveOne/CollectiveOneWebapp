package org.collectiveone.modules.assignations.repositories;

import java.util.UUID;

import org.collectiveone.modules.assignations.Bill;
import org.springframework.data.repository.CrudRepository;

public interface BillRepositoryIf extends CrudRepository<Bill, UUID> {

}
