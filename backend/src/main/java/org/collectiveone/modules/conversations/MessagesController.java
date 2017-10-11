package org.collectiveone.modules.conversations;

import java.util.UUID;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.springframework.beans.factory.annotation.Autowired;
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
	private InitiativeService initiativeService;
	
	
	/* creates a new thread and post a new message on it */
	@RequestMapping(path = "/messages", method = RequestMethod.POST) 
	public PostResult postThreadAndMessage(
			@RequestBody MessageDto messageDto) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = null;
		initiativeId = messageService.getInitiativeIdOfMessageDto(messageDto);
		
		if (!initiativeService.isMemberOfEcosystem(initiativeId, getLoggedUserId())) {
			return new PostResult("error", "not authorized", "");
		}
		
		return messageService.postThreadAndMessage(messageDto, getLoggedUserId());
	}
	
	/* creates a message on an existing message thread */
	@RequestMapping(path = "/messages/{threadId}", method = RequestMethod.POST) 
	public PostResult postMessage(
			@RequestParam("threadId") String threadIdStr,
			@RequestBody MessageDto messageDto) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = null;
		UUID threadId = UUID.fromString(threadIdStr);
		initiativeId = messageService.getInitiativeIdOfMessageThread(threadId);
		
		if (initiativeService.isMemberOfEcosystem(initiativeId, getLoggedUserId())) {
			return new PostResult("error", "not authorized", "");
		}
		
		return messageService.postMessage(threadId, messageDto, getLoggedUserId());
	}
	
}
