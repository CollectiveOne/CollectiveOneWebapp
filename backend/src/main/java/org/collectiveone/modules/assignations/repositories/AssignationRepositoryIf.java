package org.collectiveone.modules.assignations.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.assignations.model.Assignation;
import org.springframework.data.repository.CrudRepository;

public interface AssignationRepositoryIf extends CrudRepository<Assignation, UUID> {
	
	Assignation findById(UUID assignationId);
	
	List<Assignation> findByInitiativeId(UUID initiativeId); 
	
}
