package org.collectiveone.modules.uprcl.repositories;

import org.collectiveone.modules.uprcl.entities.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepositoryIf extends JpaRepository<Link, String> {
	
}
