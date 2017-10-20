package org.collectiveone.modules.assignations.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.assignations.Assignation;
import org.collectiveone.modules.assignations.enums.AssignationState;
import org.collectiveone.modules.assignations.enums.AssignationType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AssignationRepositoryIf extends CrudRepository<Assignation, UUID> {
	
	Assignation findById(UUID assignationId);
	
	List<Assignation> findByInitiativeId(UUID initiativeId, Pageable page); 
	
	List<Assignation> findByAlsoInInitiatives_Id(UUID initiativeId, Pageable page);
	
	List<Assignation> findByState(AssignationState state);
	
	List<Assignation> findByInitiativeIdAndState(UUID initiativeId, AssignationState state);
	
	List<Assignation> findByTypeAndState(AssignationType type, AssignationState state);
	
	@Query("SELECT init.id FROM Assignation ass JOIN ass.initiative init WHERE ass.id = ?1")
	UUID findInitiativeId(UUID assignationId);
	
}
