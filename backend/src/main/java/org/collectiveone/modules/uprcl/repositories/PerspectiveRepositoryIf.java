package org.collectiveone.modules.uprcl.repositories;

import org.collectiveone.modules.uprcl.entities.Perspective;
import org.springframework.data.repository.CrudRepository;

public interface PerspectiveRepositoryIf extends CrudRepository<Perspective, String> {
	
}
