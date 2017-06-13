package org.collectiveone.repositories;

import java.util.UUID;

import org.collectiveone.model.support.EvaluationGrade;
import org.springframework.data.repository.CrudRepository;

public interface EvaluationGradeRepositoryIf extends CrudRepository<EvaluationGrade, UUID> {
	
	EvaluationGrade findByAssignation_IdAndReceiver_User_C1IdAndEvaluator_User_C1Id(UUID assignationId, UUID receiverUserC1Id, UUID evaluatorUserC1Id);
}
