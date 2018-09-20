package org.collectiveone.modules.contexts.repositories;

import java.util.UUID;

import org.collectiveone.modules.contexts.cards.CardNumberField;
import org.springframework.data.repository.CrudRepository;

public interface CardNumberFieldRepositoryIf extends CrudRepository<CardNumberField, UUID> {

	public CardNumberField findById(UUID id);
	
}
