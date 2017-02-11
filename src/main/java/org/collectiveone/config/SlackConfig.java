package org.collectiveone.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;

@Configuration
public class SlackConfig {

    @Value("${slack.channel}")
    String channel;

    @Value("${slack.api.key}")
    String key;

    SlackSession slackSession;

    @Bean
    SlackSession slackSession() throws IOException {
        if (null == slackSession) {
            slackSession = SlackSessionFactory.createWebSocketSlackSession(key);
            slackSession.connect();
        }
        return slackSession;
    }

    @Bean
    SlackChannel slackChannel() throws IOException {
        return slackSession.findChannelByName(channel);
    }

}