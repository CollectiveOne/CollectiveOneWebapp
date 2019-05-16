package org.collectiveone.modules.c1.data.repositories;

import java.util.UUID;

import org.collectiveone.modules.c1.data.entities.TextData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextDataRepositoryIf extends JpaRepository<TextData, UUID> {
	
}
