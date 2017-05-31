package org.collectiveone.repositories;

import java.util.UUID;

import org.collectiveone.model.extensions.InitiativeTransfer;
import org.springframework.data.repository.CrudRepository;

public interface InitiativeTransferRepositoryIf extends CrudRepository<InitiativeTransfer, UUID> {

}
