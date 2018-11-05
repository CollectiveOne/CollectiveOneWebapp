package org.collectiveone.modules.conversations;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface MessageRepositoryIf extends CrudRepository<Message, UUID> {
	
	Optional<Message> findById(UUID messageId);
		
}
