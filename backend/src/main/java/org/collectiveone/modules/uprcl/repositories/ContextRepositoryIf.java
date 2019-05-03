package org.collectiveone.modules.uprcl.repositories;

import org.collectiveone.modules.uprcl.entities.Context;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContextRepositoryIf extends JpaRepository<Context, String> {
	
}
