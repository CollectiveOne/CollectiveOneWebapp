package org.collectiveone.modules.uprcl.repositories;

import java.util.UUID;

import org.collectiveone.modules.uprcl.entities.Context;
import org.springframework.data.repository.CrudRepository;

public interface ContextRepositoryIf extends CrudRepository<Context, UUID> {
	
}
