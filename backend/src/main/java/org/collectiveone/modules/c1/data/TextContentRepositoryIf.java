package org.collectiveone.modules.c1.data;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface TextContentRepositoryIf extends CrudRepository<TextContent, UUID> {
	
}
