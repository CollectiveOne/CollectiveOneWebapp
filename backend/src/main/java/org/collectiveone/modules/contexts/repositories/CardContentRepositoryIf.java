package org.collectiveone.modules.contexts.repositories;

import java.util.UUID;

import org.collectiveone.modules.contexts.cards.CardContent;
import org.springframework.data.repository.CrudRepository;

public interface CardContentRepositoryIf extends CrudRepository<CardContent, UUID> {

	public CardContent findById(UUID id);
	
}
