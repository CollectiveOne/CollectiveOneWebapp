package org.collectiveone.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;

@Configuration
public class SlackConfig {

	@Value("${slack.channel}")
    String channel;
	
	@Autowired
	protected Environment env;

    SlackSession slackSession;

    @Bean
    SlackSession slackSession() throws IOException {
        if (null == slackSession) {
        	if(env.getProperty("collectiveone.webapp.connect-slack-enabled").equalsIgnoreCase("true")) {
        		slackSession = SlackSessionFactory.createWebSocketSlackSession(System.getenv("SLACK_API_KEY"));
                slackSession.connect();
        	}
        }
        return slackSession;
    }

    @Bean
    SlackChannel slackChannel() throws IOException {
    	if (null != slackSession) {
    		return slackSession.findChannelByName(channel);
    	} else {
    		return null;
    	}
    }

}