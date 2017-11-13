package org.collectiveone.common;


import javax.transaction.Transactional;

import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.collectiveone.modules.users.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupMigrations implements ApplicationListener<ContextRefreshedEvent> {
	
	
	@Autowired
	AppUserService appUserService	;
	
	@Autowired
	AppUserRepositoryIf appUserRepository;
	
	
	@EventListener
	@Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
		
		/* Issue with Auth0 API API rate limit */
//		List<AppUser> users = (List<AppUser>) appUserRepository.findAll();
//		
//		System.out.println("updating users profiles");
//		
//		for (AppUser user : users) {
//			appUserService.updateUserDataInLocalDB(user.getC1Id());
//		}
    }
	
	

}
