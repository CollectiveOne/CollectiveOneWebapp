package org.collectiveone.modules.model;

import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.InitiativeRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelService {
	
	@Autowired
	private InitiativeRepositoryIf initiativeRepository;
	
	@Autowired
	private ModelViewRepositoryIf modelViewRepository;
	
	@Transactional
	public PostResult createView (UUID initiativeId, ViewDto viewDto, UUID creatorId) {
		
		Initiative initiative = initiativeRepository.findById(initiativeId);
		if (initiative == null) return new PostResult("error", "initiative not found", "");
		
		ModelView view = new ModelView();
		
		view.setInitiative(initiative);
		view.setTitle(viewDto.getTitle());
		view.setDescription(viewDto.getDescription());
		view = modelViewRepository.save(view);
		
		return new PostResult("success", "view created", view.getId().toString());
	}
}
