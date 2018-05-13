package org.collectiveone.common;


import javax.transaction.Transactional;

import org.collectiveone.modules.activity.ActivityService;
import org.collectiveone.modules.activity.repositories.ActivityRepositoryIf;
import org.collectiveone.modules.activity.repositories.SubscriberRepositoryIf;
import org.collectiveone.modules.initiatives.repositories.InitiativeRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelCardWrapperRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelSectionRepositoryIf;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupMigrations implements ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	ActivityService activityService;
		
	@Autowired
	ModelCardWrapperRepositoryIf modelCardWrapperRepository;
	
	@Autowired
	ActivityRepositoryIf activityRepository;
	
	@Autowired
	ModelSectionRepositoryIf modelSectionRepository;
	
	@Autowired
	InitiativeRepositoryIf initiativeRepository;
	
	@Autowired
	AppUserRepositoryIf appUserRepository;
	
	@Autowired
	SubscriberRepositoryIf subscriberRepository;
	
	
	@EventListener
	@Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
    }
}
