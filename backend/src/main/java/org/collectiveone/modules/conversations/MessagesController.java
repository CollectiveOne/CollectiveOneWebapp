package org.collectiveone.modules.conversations;

import java.util.UUID;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/1")
public class MessagesController extends BaseController { 
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private InitiativeService initiativeService;
	
	
	/* creates a new message (context type + element id are used as the identifier of the location of the message) */
	@RequestMapping(path = "/messages/{contextElementType}/{contextElementId}", method = RequestMethod.POST) 
	public PostResult postMessage(
			@PathVariable("contextElementType") String contextElementTypeStr,
			@PathVariable("contextElementId") String contextElementIdStr,
			@RequestBody MessageDto messageDto) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		MessageThreadContextType contextType = MessageThreadContextType.valueOf(contextElementTypeStr);
		UUID elementId = UUID.fromString(contextElementIdStr);
		
		UUID initiativeId = messageService.getInitiativeIdOfMessage(contextType, elementId);
		
		/* Permission to comment is default to ecosystem for now */
		if (!initiativeService.isMemberOfEcosystem(initiativeId, getLoggedUserId())) {
			return new PostResult("error", "not authorized", "");
		}
		
		return messageService.postMessage(messageDto, getLoggedUserId(), contextType, elementId);
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
		
}
