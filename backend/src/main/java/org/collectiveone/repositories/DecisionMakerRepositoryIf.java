package org.collectiveone.repositories;

import java.util.UUID;

import org.collectiveone.model.support.DecisionMaker;
import org.springframework.data.repository.CrudRepository;

public interface DecisionMakerRepositoryIf extends CrudRepository<DecisionMaker, UUID> {
	
}
