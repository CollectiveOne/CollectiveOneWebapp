package org.collectiveone.modules.conversations;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepositoryIf extends CrudRepository<Message, UUID> {
	
	Message findById(UUID messageId);
	
	@Query("SELECT msg FROM Message msg JOIN msg.thread thr WHERE thr.modelView.id = ?1 OR thr.modelSection.id IN ?2 OR thr.modelCardWrapper.id IN ?3 ORDER BY msg.timestamp DESC")
	Page<Message> findOfViewSectionsAndCards(UUID viewId, List<UUID> sectionIds, List<UUID> cardsWrappersIds, Pageable pageable);
	
	@Query("SELECT msg FROM Message msg JOIN msg.thread thr WHERE thr.modelView.id = ?1 OR thr.modelSection.id IN ?2 ORDER BY msg.timestamp DESC")
	Page<Message> findOfViewAndSections(UUID viewId, List<UUID> sectionIds, Pageable pageable);
	
	@Query("SELECT msg FROM Message msg JOIN msg.thread thr WHERE thr.modelView.id = ?1 ORDER BY msg.timestamp DESC")
	Page<Message> findOfView(UUID viewId, Pageable pageable);
		
}
