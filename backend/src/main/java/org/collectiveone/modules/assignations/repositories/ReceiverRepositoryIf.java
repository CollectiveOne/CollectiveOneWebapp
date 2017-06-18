package org.collectiveone.modules.assignations.repositories;

import java.util.UUID;

import org.collectiveone.modules.assignations.model.Receiver;
import org.springframework.data.repository.CrudRepository;

public interface ReceiverRepositoryIf extends CrudRepository<Receiver, UUID> {

	Receiver findByAssignation_IdAndUser_C1Id(UUID assignationId, UUID appUserC1Id);
}
