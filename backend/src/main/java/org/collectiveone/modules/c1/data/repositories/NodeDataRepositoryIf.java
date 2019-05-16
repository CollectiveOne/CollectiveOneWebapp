package org.collectiveone.modules.c1.data.repositories;

import java.util.UUID;

import org.collectiveone.modules.c1.data.entities.NodeData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NodeDataRepositoryIf extends JpaRepository<NodeData, UUID> {
	
}
