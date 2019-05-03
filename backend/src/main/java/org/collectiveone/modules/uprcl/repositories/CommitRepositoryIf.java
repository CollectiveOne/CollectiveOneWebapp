package org.collectiveone.modules.uprcl.repositories;

import org.collectiveone.modules.uprcl.entities.Commit;
import org.springframework.data.repository.CrudRepository;

public interface CommitRepositoryIf extends CrudRepository<Commit, String> {
	
}
