package org.collectiveone.common;


import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.modules.activity.Activity;
import org.collectiveone.modules.activity.enums.ActivityType;
import org.collectiveone.modules.activity.repositories.ActivityRepositoryIf;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.repositories.InitiativeRepositoryIf;
import org.collectiveone.modules.model.ModelCardWrapper;
import org.collectiveone.modules.model.ModelSection;
import org.collectiveone.modules.model.repositories.ModelCardWrapperRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelSectionRepositoryIf;
import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserRepositoryIf;
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
	
	@Autowired
	ModelSectionRepositoryIf modelSectionRepository;
	
	@Autowired
	InitiativeRepositoryIf initiativeRepository;
	
	@Autowired
	AppUserRepositoryIf appUserRepository;
	
	
	@EventListener
	@Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
		
		/* Create top section to all initiatives */
		List<Initiative> initiatives = (List<Initiative>) initiativeRepository.findAll();
		
		System.out.println("creating initiatives top sections");
		
		for (Initiative initiative : initiatives) {
			if (initiative.getTopModelSection() == null) {
				
				System.out.println("creating top section for " + initiative.getMeta().getName());
				
				ModelSection section = new ModelSection();
				section.setTitle(initiative.getMeta().getName());
				section.setInitiative(initiative);
				section.setIsTopModelSection(true);
				
				section = modelSectionRepository.save(section);
				
				initiative.setTopModelSection(section);
				initiativeRepository.save(initiative);
				
			}
		}
		
		
		/* Fill card wrappers creation date */
		List<ModelCardWrapper> cardWrappersNotCreated = modelCardWrapperRepository.findWithNullCreation();
		
		System.out.println("filling card wrappers creation date");
		
		for (ModelCardWrapper cardWrapper : cardWrappersNotCreated) {
			
			List<Activity> creationEvents = activityRepository.findOfCard(cardWrapper.getId(), ActivityType.MODEL_CARDWRAPPER_CREATED);
			
			Timestamp creationDate = null;
			AppUser creator = null;
			
			if (creationEvents.size() > 0) {
				creationDate = creationEvents.get(0).getTimestamp();
				creator = creationEvents.get(0).getTriggerUser();
				
			} else {
				creationDate = new Timestamp(1451606400000L);
				creator = appUserRepository.findByC1Id(UUID.fromString("ac12edce-5f45-124a-815f-486a854d000d")); // Tom
			}
			
			System.out.println("filling card wrapper " + cardWrapper.getId().toString() +" creation date and creator");
			cardWrapper.setCreationDate(creationDate);
			cardWrapper.setCreator(creator);
			
			modelCardWrapperRepository.save(cardWrapper);
		}
		
		/* Fill card wrappers last edited date */
		List<ModelCardWrapper> cardWrappersNotEdited = modelCardWrapperRepository.findWithNullLastEdited();
		
		System.out.println("filling card wrappers last edited date");
		
		for (ModelCardWrapper cardWrapper : cardWrappersNotEdited) {
			
			cardWrapper.getEditors().clear();
			List<Activity> editionEvents = activityRepository.findOfCard(cardWrapper.getId(), ActivityType.MODEL_CARDWRAPPER_CREATED);
			
			Timestamp lastEdited = null;
			
			if (editionEvents.size() > 0) {
				Activity lastEdition = editionEvents.get(0);
				lastEdited = lastEdition.getTimestamp();
				/* get the max */
				for (Activity thisEvent : editionEvents) {
					if (thisEvent.getTimestamp().after(lastEdited)) {
						lastEdited = thisEvent.getTimestamp();
					}
					cardWrapper.getEditors().add(lastEdition.getTriggerUser());
				}
			} else {
				lastEdited = new Timestamp(1451606400000L);
				cardWrapper.getEditors().add(appUserRepository.findByC1Id(UUID.fromString("ac12edce-5f45-124a-815f-486a854d000d")));
			}
			
			System.out.println("filling card wrapper " + cardWrapper.getId().toString() +" last edited date");
			cardWrapper.setLastEdited(lastEdited);
			
			modelCardWrapperRepository.save(cardWrapper);
		}
    }
}
