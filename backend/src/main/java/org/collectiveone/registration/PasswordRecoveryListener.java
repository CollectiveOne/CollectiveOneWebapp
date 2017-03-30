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
public class PasswordRecoveryListener implements ApplicationListener<OnPasswordRecoveryAsked> { // NO_UCD (unused code)
    @Autowired
    private UserAuthServiceIf userService;

    @Autowired
    private MessageSource messages;
    
    @Autowired
    private AppMailServiceHeroku mailService;

    
    // API

    @Override
    public void onApplicationEvent(final OnPasswordRecoveryAsked event) {
        try {
			this.confirmRegistration(event);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private void confirmRegistration(final OnPasswordRecoveryAsked event) throws IOException {
        final User user = event.getUser();
        
        final String token = UUID.randomUUID().toString();
        userService.createPasswordRecoveryTokenForUser(user, token);
        
        String subject = messages.getMessage("message.pswdRecoverySubject", null, event.getLocale());
        String body = messages.getMessage("message.pswdRecoveryBody", null, event.getLocale()) + 
        		" \r\n" + event.getAppUrl() + "/user/passwordRecovery?id="+user.getId()+"&token=" + token;
        
        mailService.sendMail(
        		user.getEmail(), 
        		subject, 
        		body);
        
    }
  
}
