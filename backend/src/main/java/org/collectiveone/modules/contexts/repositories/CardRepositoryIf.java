package org.collectiveone.modules.contexts.repositories;

import java.util.UUID;

import org.collectiveone.modules.contexts.cards.Card;
import org.springframework.data.repository.CrudRepository;

public interface CardRepositoryIf extends CrudRepository<Card, UUID> {

	public Card findById(UUID id);
	
}
