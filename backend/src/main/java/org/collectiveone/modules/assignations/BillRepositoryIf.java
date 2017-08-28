package org.collectiveone.modules.assignations;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface BillRepositoryIf extends CrudRepository<Bill, UUID> {

}
