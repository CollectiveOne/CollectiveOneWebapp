package org.collectiveone.common;


import java.util.List;

import javax.transaction.Transactional;

import org.collectiveone.modules.activity.Activity;
import org.collectiveone.modules.activity.enums.ActivityType;
import org.collectiveone.modules.activity.repositories.ActivityRepositoryIf;
import org.collectiveone.modules.model.ModelCardWrapper;
import org.collectiveone.modules.model.repositories.ModelCardWrapperRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelSectionRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupMigrations implements ApplicationListener<ContextRefreshedEvent> {
	
	
	@Autowired
	ModelCardWrapperRepositoryIf modelCardWrapperRepository;
	
	@Autowired
	ActivityRepositoryIf activityRepository;
	
	@EventListener
	@Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
		
		/* Create top section to all initiatives */
		List<ModelCardWrapper> cardWrappers = modelCardWrapperRepository.findWithNullCreationDate();
		
		System.out.println("filling card wrappers creation date");
		
		for (ModelCardWrapper cardWrapper : cardWrappers) {
			
			List<Activity> creationEvents = activityRepository.findOfCard(cardWrapper.getId(), ActivityType.MODEL_CARDWRAPPER_CREATED);
			
			if (creationEvents.size() > 0) {
				System.out.println("filling card wrapper " + cardWrapper.getId().toString() +" creation date");
				cardWrapper.setCreationDate(creationEvents.get(0).getTimestamp());
			}
			
			modelCardWrapperRepository.save(cardWrapper);
		}
    }
}
