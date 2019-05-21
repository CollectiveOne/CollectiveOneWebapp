package org.collectiveone.modules.c1.data.repositories;

import org.collectiveone.modules.c1.data.entities.Data;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRepositoryIf extends JpaRepository<Data, byte[]> {
	
}
