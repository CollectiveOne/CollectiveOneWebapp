package org.collectiveone.modules.conversations;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface MessageThreadRepositoryIf extends CrudRepository<MessageThread, UUID> {
	
	MessageThread findById(UUID threadId);
	
	MessageThread findByModelCardWrapper_Id(UUID elementId);
	
	MessageThread findByModelSection_Id(UUID elementId);
	
		
}
