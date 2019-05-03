package org.collectiveone.modules.uprcl.repositories;

import org.collectiveone.modules.uprcl.entities.Content;
import org.springframework.data.repository.CrudRepository;

public interface ContentRepositoryIf extends CrudRepository<Content, String> {
	
}
