package org.collectiveone.modules.contexts;

import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.contexts.cards.CardContent;
import org.collectiveone.modules.contexts.cards.CardService;
import org.collectiveone.modules.contexts.cards.CardWrapper;
import org.collectiveone.modules.contexts.dto.NewCardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerspectiveOuterService {
	
	@Autowired
	private PerspectiveInnerService perspectiveInnerService;
	
	@Autowired
	private CardService cardService; 
	
	@Transactional
	public PostResult stageCardWrapper(
			NewCardDto cardDto, 
			UUID perspectiveId, 
			UUID authorId, 
			UUID onCardWrapperId, 
			Boolean isBefore) {
		
		Commit workingCommit = perspectiveInnerService.getOrCreateWorkingCommit(perspectiveId, authorId);
		CardContent cardContent = cardService.createCardContent(cardDto.getBase());
		
		CardWrapper cardWrapper = new CardWrapper();
		StageCard stage = new StageCard(workingCommit, StageAction.ADD, cardWrapper, cardContent);
		
		return new PostResult("success", "new card staged", "");
	}

}
