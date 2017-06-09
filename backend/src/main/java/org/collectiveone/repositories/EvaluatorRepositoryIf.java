package org.collectiveone.repositories;

import java.util.UUID;

import org.collectiveone.model.support.Evaluator;
import org.springframework.data.repository.CrudRepository;

public interface EvaluatorRepositoryIf extends CrudRepository<Evaluator, UUID> {

}
