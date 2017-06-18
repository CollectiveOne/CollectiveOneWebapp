package org.collectiveone.modules.assignations.repositories;

import java.util.UUID;

import org.collectiveone.modules.assignations.model.Evaluator;
import org.springframework.data.repository.CrudRepository;

public interface EvaluatorRepositoryIf extends CrudRepository<Evaluator, UUID> {
	
	Evaluator findByAssignationIdAndUser_C1Id(UUID assignationId, UUID appUserC1Id);
}
