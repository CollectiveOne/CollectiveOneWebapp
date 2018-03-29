package org.collectiveone.modules.conversations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Controller
public class MessagesWebSocketController {

    private final SimpMessagingTemplate template;

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

   
    
    @Autowired
    MessagesWebSocketController(SimpMessagingTemplate template){
        this.template = template;
    }

    @MessageMapping("/messages/send")
    @SendTo("/topic/conversation")
    public String onReceivedMesage(String message){
        return new SimpleDateFormat("HH:mm:ss").format(new Date())+"- "+message;
    }
    
    @Scheduled(fixedRate = 5000)
    public void run() {
        String time = LocalTime.now().format(TIME_FORMAT);
        template.convertAndSend("/topic/conversation", "Current time is " + time);
    }
    
    
    
}