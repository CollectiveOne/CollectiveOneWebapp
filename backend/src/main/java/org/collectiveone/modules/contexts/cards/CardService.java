package org.collectiveone.modules.contexts.cards;

import org.collectiveone.modules.contexts.repositories.CardRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {
	
	@Autowired
	public CardRepositoryIf cardContentRepository;
	
	public Card createCardContent(String base) {
		
		Card cardContent = new Card();
		cardContent = cardContentRepository.save(cardContent);
		
		return cardContent;
	}
}
