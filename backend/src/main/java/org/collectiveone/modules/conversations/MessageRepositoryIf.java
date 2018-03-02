package org.collectiveone.modules.conversations;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepositoryIf extends CrudRepository<Message, UUID> {
	
	Message findById(UUID messageId);
	
	@Query("SELECT msg FROM Message msg JOIN msg.thread thr WHERE thr.modelSection.id IN ?1 OR thr.modelCardWrapper.id IN ?2 ORDER BY msg.timestamp DESC")
	Page<Message> findOfSectionsAndCards(List<UUID> sectionIds, List<UUID> cardsIds, Pageable pageable);
		
	@Query("SELECT msg FROM Message msg JOIN msg.thread thr WHERE thr.modelSection.id IN ?1 ORDER BY msg.timestamp DESC")
	Page<Message> findOfSections(List<UUID> sectionIds, Pageable pageable);
		
	@Query("SELECT msg FROM Message msg JOIN msg.thread thr WHERE thr.modelCardWrapper.id = ?1 ORDER BY msg.timestamp DESC")
	Page<Message> findOfCard(UUID cardId, Pageable pageable);
		
}
