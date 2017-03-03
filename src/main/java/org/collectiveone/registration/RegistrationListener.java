package org.collectiveone.registration;

import java.io.IOException;
import java.util.UUID;

import org.collectiveone.model.User;
import org.collectiveone.services.AppMailServiceHeroku;
import org.collectiveone.services.UserAuthServiceIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    @Autowired
    private UserAuthServiceIf userService;

    @Autowired
    private MessageSource messages;
    
    @Autowired
    private AppMailServiceHeroku mailService;

    
    // API

    @Override
    public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
        try {
			this.confirmRegistration(event);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private void confirmRegistration(final OnRegistrationCompleteEvent event) throws IOException {
        final User user = event.getUser();
        final String token = UUID.randomUUID().toString();
        userService.createVerificationTokenForUser(user, token);

        String activationUrl =  event.getAppUrl() + "/user/signupConfirm?token=" + token;
        String subject = messages.getMessage("message.confMailSubject", null, event.getLocale());
        String body = messages.getMessage("message.confMailBody", null, event.getLocale()) + 
        		" \r\n<a href='"+activationUrl+"'>"+activationUrl+"</a>";
        
        mailService.sendMail(
        		user.getEmail(), 
        		subject, 
        		body);
        
    }
  
}
