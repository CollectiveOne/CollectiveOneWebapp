package org.collectiveone.modules.uprcl.repositories;

import org.collectiveone.modules.uprcl.entities.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepositoryIf extends JpaRepository<Content, String> {
	
}
