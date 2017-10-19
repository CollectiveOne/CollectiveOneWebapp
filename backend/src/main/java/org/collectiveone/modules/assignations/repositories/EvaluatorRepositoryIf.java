package org.collectiveone.modules.assignations.repositories;

import java.util.UUID;

import org.collectiveone.modules.assignations.Evaluator;
import org.collectiveone.modules.assignations.enums.EvaluatorState;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface EvaluatorRepositoryIf extends CrudRepository<Evaluator, UUID> {
	
	Evaluator findByAssignationIdAndUser_C1Id(UUID assignationId, UUID appUserC1Id);
	
	@Query("SELECT COUNT (evs) FROM Assignation assig JOIN assig.evaluators evs WHERE assig.id = ?1 AND evs.state = ?2")
	Integer countByAssignationIdAndStateInternal(UUID assignationId, EvaluatorState state);
	
	default int countByAssignationIdAndState(UUID assignationId, EvaluatorState state) {
		Integer res = countByAssignationIdAndStateInternal(assignationId, state);
		return res == null ? 0 : res.intValue();
	}
}
