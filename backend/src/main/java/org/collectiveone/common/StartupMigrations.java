package org.collectiveone.common;

import java.util.List;

import javax.transaction.Transactional;

import org.collectiveone.modules.assignations.Assignation;
import org.collectiveone.modules.assignations.repositories.AssignationRepositoryIf;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.collectiveone.modules.tokens.InitiativeTransfer;
import org.collectiveone.modules.tokens.repositories.InitiativeTransferRepositoryIf;
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
	
	@Autowired
	private InitiativeTransferRepositoryIf initiativeTransferRepository;
	
	@EventListener
	@Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
		
		/* Add alsoInInitiatives property to existing assignations */
		List<Assignation> allAssignations = (List<Assignation>) assignationRepository.findAll();
		
		for (Assignation assignation :  allAssignations) {
			List<Initiative> parents = initiativeService.getParentGenealogyInitiatives(assignation.getInitiative().getId());
			
			assignation.getAlsoInInitiatives().clear();
			assignation.getAlsoInInitiatives().addAll(parents);
			System.out.println("updated also in of assignation");
			
			assignationRepository.save(assignation);
		}
		
		/* Add alsoInInitiatives property to existing assignations */
		List<InitiativeTransfer> allInitiativeTransfers = (List<InitiativeTransfer>) initiativeTransferRepository.findAll();
		
		for (InitiativeTransfer transfer :  allInitiativeTransfers) {
			List<Initiative> parents = initiativeService.getParentGenealogyInitiatives(transfer.getFrom().getId());
			
			transfer.getAlsoInInitiatives().clear();
			transfer.getAlsoInInitiatives().addAll(parents);
			System.out.println("updated also in of transfers");
			
			initiativeTransferRepository.save(transfer);
		}
    }
	
	

}
