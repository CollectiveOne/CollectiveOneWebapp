package org.collectiveone.modules.assignations;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface EvaluationGradeRepositoryIf extends CrudRepository<EvaluationGrade, UUID> {
	
	EvaluationGrade findByAssignation_IdAndReceiver_User_C1IdAndEvaluator_User_C1Id(UUID assignationId, UUID receiverUserC1Id, UUID evaluatorUserC1Id);
}
