package org.collectiveone.modules.conversations;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface MessageRepositoryIf extends CrudRepository<Message, UUID> {
	
	Message findById(UUID messageId);
		
}
