package org.collectiveone.modules.model;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.collectiveone.common.BaseController;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ModelWebSocketController extends BaseController {

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

    @MessageMapping("/messages/send/a")
    @SendTo("/topic/conversation/a")
    public String onReceivedMesage(String message){
        return "Server reply on "+new SimpleDateFormat("HH:mm:ss").format(new Date())+" for :: "+message;
    }
    
}