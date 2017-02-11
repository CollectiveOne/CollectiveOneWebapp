package org.collectiveone.slack;

import java.io.IOException;
import java.util.UUID;

import org.collectiveone.model.User;
import org.collectiveone.services.AppMailServiceHeroku;
import org.collectiveone.services.UserServiceIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;

@Component
public class SlackPublishListener implements ApplicationListener<OnSlackPublishAsked> {
	
	@Autowired
	private SlackSession slackSession;
	
	@Autowired
	private SlackChannel slackChannel;
    
    // API

    @Override
    public void onApplicationEvent(final OnSlackPublishAsked event) {
        try {
			this.sendMessage(event);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private void sendMessage(final OnSlackPublishAsked event) throws IOException {
        final String message = event.getMessage();
        slackSession.sendMessage(slackChannel, message);
    }
  
}
