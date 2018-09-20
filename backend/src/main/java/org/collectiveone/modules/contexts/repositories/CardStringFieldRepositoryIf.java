package org.collectiveone.modules.contexts.repositories;

import java.util.UUID;

import org.collectiveone.modules.contexts.cards.CardStringField;
import org.springframework.data.repository.CrudRepository;

public interface CardStringFieldRepositoryIf extends CrudRepository<CardStringField, UUID> {

	public CardStringField findById(UUID id);
	
}
