package org.collectiveone.modules.assignations;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AssignationRepositoryIf extends CrudRepository<Assignation, UUID> {
	
	Assignation findById(UUID assignationId);
	
	List<Assignation> findByInitiativeId(UUID initiativeId); 
	
	List<Assignation> findByState(AssignationState state);
	
	List<Assignation> findByInitiativeIdAndState(UUID initiativeId, AssignationState state);
	
	List<Assignation> findByTypeAndState(AssignationType type, AssignationState state);
	
	@Query("SELECT init.id FROM Assignation ass JOIN ass.initiative init WHERE ass.id = ?1")
	UUID findInitiativeId(UUID assignationId);
	
}
