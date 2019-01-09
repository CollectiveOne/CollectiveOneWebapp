package org.collectiveone.modules.contexts;

import org.collectiveone.modules.contexts.cards.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerspectiveOuterService {
	
	@Autowired
	private PerspectiveInnerService perspectiveInnerService;
	
	@Autowired
	private CardService cardService; 
	

}
