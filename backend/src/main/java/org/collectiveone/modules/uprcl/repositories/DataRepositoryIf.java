package org.collectiveone.modules.uprcl.repositories;

import org.collectiveone.modules.uprcl.entities.Data;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRepositoryIf extends JpaRepository<Data, String> {
	
}
