package org.collectiveone.modules.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.common.dto.GetResult;
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
	
	@Autowired
	private ModelSectionRepositoryIf modelSectionRepository;
	
	@Autowired
	private ModelCardWrapperRepositoryIf modelCardWrapperRepository;
	
	@Autowired
	private ModelCardRepositoryIf modelCardRepository;
	
	
	@Transactional
	public GetResult<ModelDto> getModel(UUID initiativeId, Integer level, UUID requestById) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
		if (initiative == null) return new GetResult<ModelDto>("error", "initiative not found", null);
		
		List<ModelViewDto> viewsDto = new ArrayList<ModelViewDto>();
		
		List<ModelView> views = initiative.getModelViews(); 
		for (ModelView view : views) {
			viewsDto.add(view.toDto(level)); 
		}
		
		ModelDto modelDto = new ModelDto();
		modelDto.setViews(viewsDto);
		
		return new GetResult<ModelDto> ("success", "model found", modelDto);
	}
	
	@Transactional
	public PostResult createView (UUID initiativeId, ModelViewDto viewDto, UUID creatorId) {
		
		Initiative initiative = initiativeRepository.findById(initiativeId);
		if (initiative == null) return new PostResult("error", "initiative not found", "");
		
		ModelView view = new ModelView();
		
		view.setInitiative(initiative);
		view.setTitle(viewDto.getTitle());
		view.setDescription(viewDto.getDescription());
		view = modelViewRepository.save(view);
		
		return new PostResult("success", "view created", view.getId().toString());
	}
	
	@Transactional
	public PostResult createSection(ModelSectionDto sectionDto, UUID creatorId) {
		
		ModelView view = modelViewRepository.findById(UUID.fromString(sectionDto.getViewId()));
		if (view == null) return new PostResult("error", "view not found", "");
		
		ModelSection section = new ModelSection();
		section.setTitle(sectionDto.getTitle());
		section.setDescription(sectionDto.getDescription());
		section = modelSectionRepository.save(section);
		
		view.getSections().add(section);
		modelViewRepository.save(view);
		
		return new PostResult("success", "section created", section.getId().toString());
	}
	
	@Transactional
	public PostResult createCard(CardDto cardDto, UUID creatorId) {
		
		ModelSection section = modelSectionRepository.findById(UUID.fromString(cardDto.getSectionId()));
		if (section == null) return new PostResult("error", "view not found", "");
		
		ModelCard card = new ModelCard();
		card.setTitle(cardDto.getTitle());
		card.setText(cardDto.getText());
		
		card = modelCardRepository.save(card);
		
		ModelCardWrapper cardWrapper = new ModelCardWrapper();
		cardWrapper.setCard(card);
		
		cardWrapper = modelCardWrapperRepository.save(cardWrapper);
		
		section.getCards().add(cardWrapper);
		modelSectionRepository.save(section);
		
		return new PostResult("success", "card created", card.getId().toString());
	}
}
