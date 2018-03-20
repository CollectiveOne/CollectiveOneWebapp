package org.collectiveone.common;


import java.util.List;

import javax.transaction.Transactional;

import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.repositories.InitiativeRepositoryIf;
import org.collectiveone.modules.model.ModelSection;
import org.collectiveone.modules.model.repositories.ModelSectionRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupMigrations implements ApplicationListener<ContextRefreshedEvent> {
	
	
	@Autowired
	InitiativeRepositoryIf initiativeRepository;
	
	@Autowired
	private ModelSectionRepositoryIf modelSectionRepository;
	
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
    }
	
	

}
