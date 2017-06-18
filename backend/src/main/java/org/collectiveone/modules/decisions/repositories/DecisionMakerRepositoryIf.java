package org.collectiveone.modules.decisions.repositories;

import java.util.UUID;

import org.collectiveone.modules.decisions.model.DecisionMaker;
import org.springframework.data.repository.CrudRepository;

public interface DecisionMakerRepositoryIf extends CrudRepository<DecisionMaker, UUID> {
	
}
