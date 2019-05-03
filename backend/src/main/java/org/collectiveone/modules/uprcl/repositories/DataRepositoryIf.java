package org.collectiveone.modules.uprcl.repositories;

import org.collectiveone.modules.uprcl.entities.Data;
import org.springframework.data.repository.CrudRepository;

public interface DataRepositoryIf extends CrudRepository<Data, String> {
	
}
