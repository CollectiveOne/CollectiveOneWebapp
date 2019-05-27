package org.collectiveone.modules.uprtcl.repositories;

import org.collectiveone.modules.uprtcl.entities.Commit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommitRepositoryIf extends JpaRepository<Commit, byte[]> {
	
}
