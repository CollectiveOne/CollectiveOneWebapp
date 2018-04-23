package org.collectiveone.modules.model;
import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.activity.dto.ActivityDto;
import org.collectiveone.modules.conversations.MessageDto;
import org.collectiveone.modules.conversations.MessageService;
import org.collectiveone.modules.conversations.MessageThreadContextType;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

@Controller
public class ModelWebSocketController extends BaseController {

    private final SimpMessagingTemplate template;

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    @Autowired
	private MessageService messageService;
    
    @Autowired
	private InitiativeService initiativeService;

    static class Simple{
    	int page;
    	int size;
    	String message;
    	
		 public Simple() {
		 }
    	 
		
		public int getPage() {
			return page;
		}
		public void setPage(int page) {
			this.page = page;
		}
		public int getSize() {
			return size;
		}
		public void setSize(int size) {
			this.size = size;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
    	
    	
    }
    
    @Autowired
    ModelWebSocketController(SimpMessagingTemplate template){
        this.template = template;
    }

    @MessageMapping("/messages/send/a")
    @SendTo("/topic/conversation/a")
    public String onReceivedMesage(String message){
        return "Server reply on "+new SimpleDateFormat("HH:mm:ss").format(new Date())+" for :: "+message;
    }
    
    
    
    
//    @MessageMapping("/activity/model/card/{cardWrapperId}/{contextElementTypeStr}")
//    @SendTo("/topic/activity/model/card/{cardWrapperId}")
//    public GetResult<Page<ActivityDto>> onReceivedMesage(
//    		@DestinationVariable("cardWrapperId") String cardWrapperIdStr,
//    		@DestinationVariable("contextElementTypeStr") String contextElementTypeStr,
//    		MessageDto messageDto
//    		) {
//    	
//    	MessageThreadContextType contextType = MessageThreadContextType.valueOf(contextElementTypeStr);
//		UUID elementId = UUID.fromString(cardWrapperIdStr);
//		
//		UUID initiativeId = messageService.getInitiativeIdOfMessage(contextType, elementId);
//		
//		/* Permission to comment is default to ecosystem for now */
//		if (!initiativeService.isMemberOfEcosystem(initiativeId, getLoggedUserId())) {
//			return new PostResult("error", "not authorized", "");
//		}
//		
//		return messageService.postMessage(messageDto, getLoggedUserId(), contextType, elementId);
//		
//    }
    
//    @Scheduled(fixedRate = 5000)
//    public void run() {
//        String time = LocalTime.now().format(TIME_FORMAT);
//        template.convertAndSend("/topic/conversation/a", "Current time is " + time);
//    }
    
    
    
}