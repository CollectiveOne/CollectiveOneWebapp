package org.collectiveone.services;

import org.collectiveone.slack.OnSlackPublishAsked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;

public class SlackServiceImp {
	
	@Autowired
    ApplicationEventPublisher eventPublisher;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private UserServiceImp userService;
	
	public void sendToSlack(String msg) {
		/* send slack message*/
	    if(env.getProperty("collectiveone.webapp.send-slack-enabled").equalsIgnoreCase("true")) {
	    	eventPublisher.publishEvent(new OnSlackPublishAsked(userService.userGet("collectiveone"), msg));
	    }
	}

}
