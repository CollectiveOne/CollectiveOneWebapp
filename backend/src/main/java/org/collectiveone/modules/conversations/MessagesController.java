package org.collectiveone.modules.conversations;

import java.util.UUID;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.governance.DecisionVerdict;
import org.collectiveone.modules.governance.GovernanceService;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.collectiveone.modules.model.ModelController;
import org.collectiveone.modules.model.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/1")
public class MessagesController extends BaseController { 
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ModelService modelService;

	@Autowired
	private InitiativeService initiativeService;
	
	@Autowired
	private GovernanceService governanceService;
	
	@Autowired
	ModelController modelController;
	
	
	/* creates a new message (context type + element id are used as the identifier of the location of the message) */
	@RequestMapping(path = "/messages/{contextElementType}/{contextElementId}", method = RequestMethod.POST) 
	public PostResult postMessage(
			@PathVariable("contextElementType") String contextElementTypeStr,
			@PathVariable("contextElementId") String contextElementIdStr,
			@RequestBody MessageDto messageDto,
			@RequestParam(name = "contextOfContextElementId", defaultValue="") String contextOfContextElementIdStr) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		MessageThreadContextType contextType = MessageThreadContextType.valueOf(contextElementTypeStr);
		UUID elementId = UUID.fromString(contextElementIdStr);
		
		UUID initiativeId = messageService.getInitiativeIdOfMessage(contextType, elementId);
		UUID contextOfContextElementId = contextOfContextElementIdStr.equals("") ? null : UUID.fromString(contextOfContextElementIdStr);
		
		/* Permission to comment is default to ecosystem for now */
		if (!initiativeService.isMemberOfEcosystem(initiativeId, getLoggedUserId())) {
			return new PostResult("error", "not authorized", "");
		}
		
		PostResult result= messageService.postMessage(messageDto, getLoggedUserId(), contextType, elementId, contextOfContextElementId);
		
		return result;
	}
	

	/* creates a new message (context type + element id are used as the identifier of the location of the message) */
	@RequestMapping(path = "/messages/{contextElementType}/{contextElementId}/{messageId}", method = RequestMethod.PUT) 
	public PostResult editMessage(
			@PathVariable("contextElementType") String contextElementTypeStr,
			@PathVariable("contextElementId") String contextElementIdStr,
			@PathVariable("messageId") String messageId,
			@RequestBody MessageDto messageDto) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		return messageService.editMessage(messageDto, getLoggedUserId(), UUID.fromString(messageId));
	}
	
	/* moves an existing message (context type + element id are used as the identifier of the location of the message) */
	@RequestMapping(path = "/message/{contextElementType}/{contextElementId}/{messageId}/move", method = RequestMethod.PUT) 
	public PostResult moveMessage(
			@PathVariable("contextElementType") MessageThreadContextType contextType,
			@PathVariable("contextElementId") UUID elementId,
			@PathVariable("messageId") UUID messageId,
			@RequestParam(name = "toSectionId", defaultValue="") UUID toSectionId,
			@RequestParam(name = "toCardWrapperId", defaultValue="") UUID toCardWrapperId) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID toElementId;
		MessageThreadContextType toContextType;
		UUID toContextOfContextElementId;
		
		if (toSectionId != null) {
			toElementId = toSectionId;
			toContextType = MessageThreadContextType.MODEL_SECTION;
			toContextOfContextElementId = modelService.getIdOfOneModelSectionContainingSection(toSectionId);
		} else {
			toElementId = toCardWrapperId;
			toContextType = MessageThreadContextType.MODEL_CARD;
			toContextOfContextElementId = modelService.getIdOfOneModelSectionContainingCardWrapper(toSectionId);
		}
		
		UUID fromInitiativeId = messageService.getInitiativeIdOfMessage(contextType, elementId);
		UUID toInitiativeId = messageService.getInitiativeIdOfMessage(toContextType, toElementId);
		
		/* check user can remove the comment from its original position */
		if (governanceService.canEditModel(fromInitiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		/* check user can add a comment to the target position */
		if (governanceService.canEditModel(toInitiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return messageService.moveMessage(
				messageId,
				toElementId, 
				toContextType,
				toContextOfContextElementId);
	}
		
		
}
