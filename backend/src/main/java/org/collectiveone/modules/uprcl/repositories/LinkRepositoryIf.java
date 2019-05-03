package org.collectiveone.modules.uprcl.repositories;

import org.collectiveone.modules.uprcl.entities.Link;
import org.springframework.data.repository.CrudRepository;

public interface LinkRepositoryIf extends CrudRepository<Link, String> {
	
}
