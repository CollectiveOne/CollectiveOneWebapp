package org.collectiveone.modules.assignations;

import java.util.List;

import javax.transaction.Transactional;

import org.collectiveone.modules.assignations.repositories.AssignationRepositoryIf;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupMigrations implements ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	private InitiativeService initiativeService;
	
	@Autowired
	private AssignationRepositoryIf assignationRepository;
	
	@EventListener
	@Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
		/*Temporary method to add alsoInInitiatives property to existing assignations */
		
		List<Assignation> allAssignations = (List<Assignation>) assignationRepository.findAll();
		
		for (Assignation assignation :  allAssignations) {
			List<Initiative> parents = initiativeService.getParentGenealogyInitiatives(assignation.getInitiative().getId());
			
			assignation.getAlsoInInitiatives().clear();
			assignation.getAlsoInInitiatives().addAll(parents);
			System.out.println("updated also in");
			
			assignationRepository.save(assignation);
		}
    }

}
