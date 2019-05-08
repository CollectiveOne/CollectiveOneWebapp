package org.collectiveone.modules.c1.data;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TextDataRepositoryIf extends JpaRepository<TextData, UUID> {
	
}
