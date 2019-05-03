package org.collectiveone.modules.uprcl.repositories;

import org.collectiveone.modules.uprcl.entities.Commit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommitRepositoryIf extends JpaRepository<Commit, String> {
	
}
