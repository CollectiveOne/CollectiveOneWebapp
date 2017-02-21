package org.collectiveone.services;

import org.collectiveone.slack.OnSlackPublishAsked;
import org.springframework.stereotype.Service;

@Service
public class SlackServiceImp extends BaseService {
	
	public void send(String msg) {
		/* send slack message*/
	    if(env.getProperty("collectiveone.webapp.send-slack-enabled").equalsIgnoreCase("true")) {
	    	eventPublisher.publishEvent(new OnSlackPublishAsked(userService.get("collectiveone"), msg));
	    }
	}

}
