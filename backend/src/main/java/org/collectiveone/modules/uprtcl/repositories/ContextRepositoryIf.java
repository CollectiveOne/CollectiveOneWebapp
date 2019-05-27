package org.collectiveone.modules.uprtcl.repositories;

import org.collectiveone.modules.uprtcl.entities.Context;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContextRepositoryIf extends JpaRepository<Context, byte[]> {
	
}
