package org.collectiveone.repositories;

import java.util.UUID;

import org.collectiveone.model.support.Receiver;
import org.springframework.data.repository.CrudRepository;

public interface ReceiverRepositoryIf extends CrudRepository<Receiver, UUID> {

}
