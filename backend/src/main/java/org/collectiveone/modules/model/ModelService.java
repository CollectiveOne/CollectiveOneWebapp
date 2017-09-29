package org.collectiveone.modules.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.files.FileStored;
import org.collectiveone.modules.files.FileStoredRepositoryIf;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.collectiveone.modules.initiatives.repositories.InitiativeRepositoryIf;
import org.collectiveone.modules.model.dto.ModelCardDto;
import org.collectiveone.modules.model.dto.ModelCardWrapperDto;
import org.collectiveone.modules.model.dto.ModelDto;
import org.collectiveone.modules.model.dto.ModelSectionDto;
import org.collectiveone.modules.model.dto.ModelViewDto;
import org.collectiveone.modules.model.repositories.ModelCardRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelCardWrapperRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelSectionRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelViewRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ModelService {
	
	@Autowired 
	private InitiativeService initiativeService;
	
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
	
	@Autowired
	private FileStoredRepositoryIf fileStoredRepository;
	
	
	@Transactional
	public GetResult<ModelDto> getModel(UUID initiativeId, Integer level, UUID requestById) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
		if (initiative == null) return new GetResult<ModelDto>("error", "initiative not found", null);
		
		List<ModelViewDto> viewsDto = new ArrayList<ModelViewDto>();
		
		List<ModelView> views = initiative.getModelViews(); 
		for (ModelView view : views) {
			ModelViewDto viewDto = view.toDto();
			viewDto = addViewSubElements(viewDto, view.getId(), level);
			viewsDto.add(viewDto); 
		}
		
		ModelDto modelDto = new ModelDto();
		modelDto.setViews(viewsDto);
		
		return new GetResult<ModelDto> ("success", "model found", modelDto);
	}
	
	@Transactional
	public ModelViewDto addViewSubElements(ModelViewDto viewDto, UUID viewId, Integer level) {
		
		ModelView view = modelViewRepository.findById(viewId);
		
		if (level >= 1) {
			viewDto.setSectionsLoaded(true);
			
			for (ModelSection section : view.getSections()) {
				viewDto.getSections().add(getSectionDto(section.getId(), level - 1));
			}
		} else {
			viewDto.setSectionsLoaded(false);
		}
		
		return viewDto; 
	}
	
	@Transactional
	public PostResult createView (UUID initiativeId, ModelViewDto viewDto, UUID creatorId) {
		
		Initiative initiative = initiativeRepository.findById(initiativeId);
		if (initiative == null) return new PostResult("error", "initiative not found", "");
		
		ModelView view = viewDto.toEntity(null, viewDto, initiative);
		view = modelViewRepository.save(view);
		
		return new PostResult("success", "view created", view.getId().toString());
	}
	
	@Transactional
	public PostResult editView (UUID initiativeId, UUID viewId, ModelViewDto viewDto, UUID creatorId) {
		
		Initiative initiative = initiativeRepository.findById(initiativeId);
		if (initiative == null) return new PostResult("error", "initiative not found", "");
		
		ModelView view = modelViewRepository.findById(viewId);
		
		view = viewDto.toEntity(view, viewDto, initiative);
		view = modelViewRepository.save(view);
		
		return new PostResult("success", "view edited", view.getId().toString());
	}
	
	@Transactional
	public GetResult<ModelViewDto> getView (UUID viewId, UUID requestById, Integer level) {
		
		ModelView view = modelViewRepository.findById(viewId);
		
		ModelViewDto viewDto = view.toDto();
		viewDto = addViewSubElements(viewDto, view.getId(), level);
		
		return new GetResult<ModelViewDto>("success", "view retrieved", viewDto);
	}
	
	
	@Transactional
	public PostResult deleteView (UUID viewId, UUID creatorId) {
		
		ModelView view = modelViewRepository.findById(viewId);
		modelViewRepository.delete(view);
		
		return new PostResult("success", "view deleted", "");
	}
	
	@Transactional
	public PostResult createSection(ModelSectionDto sectionDto, UUID parentSectionId, UUID parentViewId , UUID creatorId) {
		
		ModelSection section = sectionDto.toEntity(null, sectionDto);
		section = modelSectionRepository.save(section);
		
		if(parentSectionId != null) {
			ModelSection parent = modelSectionRepository.findById(parentSectionId);
			if (parent == null) return new PostResult("error", "parent section not found", "");
			
			parent.getSubsections().add(section);
			section.setInitiative(parent.getInitiative());
			
			modelSectionRepository.save(parent);
			
		} else {
			ModelView view = modelViewRepository.findById(parentViewId);
			if (view == null) return new PostResult("error", "view not found", "");
			
			view.getSections().add(section);
			section.setInitiative(view.getInitiative());
			
			modelViewRepository.save(view);
		}
		
		
		return new PostResult("success", "section created", section.getId().toString());
	}
	
	@Transactional
	public GetResult<ModelSectionDto> getSection(UUID sectionId, UUID requestById, Integer level) {
		return new GetResult<ModelSectionDto>("success", "section retrieved",  getSectionDto(sectionId, level));
	}
	
	@Transactional
	public ModelSectionDto getSectionDto(UUID sectionId, Integer level) {
		ModelSection section = modelSectionRepository.findById(sectionId);
		ModelSectionDto sectionDto = section.toDto();
		
		/* set parent sections or views */
		List<ModelSection> inSections = modelSectionRepository.findParentSections(section.getId());
		List<ModelView> inViews = modelSectionRepository.findParentViews(section.getId());
		
		for (ModelSection inSection : inSections) {
			sectionDto.getInSections().add(inSection.toDto());
		}
		
		for (ModelView inView : inViews) {
			sectionDto.getInViews().add(inView.toDto());
		}
		
		sectionDto = addSectionSubElements(sectionDto, section.getId(), level);
		
		return sectionDto;
	}
	
	@Transactional
	public GetResult<Page<ModelSectionDto>> searchSection(String query, PageRequest page, UUID initiativeId) {
		
		List<UUID> initiativeEcosystemIds = initiativeService.findAllInitiativeEcosystemIds(initiativeId);
		Page<ModelSection> enititiesPage = modelSectionRepository.searchBy("%"+query.toLowerCase()+"%", initiativeEcosystemIds, page);
		
		List<ModelSectionDto> sectionDtos = new ArrayList<ModelSectionDto>();
		
		for(ModelSection section : enititiesPage.getContent()) {
			sectionDtos.add(section.toDto());
		}
		
		Page<ModelSectionDto> dtosPage = new PageImpl<ModelSectionDto>(sectionDtos, page, enititiesPage.getNumberOfElements());
		
		return new GetResult<Page<ModelSectionDto>>("succes", "sections returned", dtosPage);
	}
	
	@Transactional
	public PostResult editSection (UUID sectionId, ModelSectionDto sectionDto, UUID creatorId) {
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		
		section = sectionDto.toEntity(section, sectionDto);
		section = modelSectionRepository.save(section);
		
		return new PostResult("success", "section edited", section.getId().toString());
	}
	
	@Transactional
	public PostResult removeSubsectionFromSection(UUID sectionId, UUID subsectionId) {
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		ModelSection subsection = modelSectionRepository.findById(subsectionId);
		
		section.getSubsections().remove(subsection);
		section = modelSectionRepository.save(section);
		
		return new PostResult("success", "subsection removed to section", section.getId().toString());
	}
	
	@Transactional
	public PostResult removeSubsectionFromView(UUID viewId, UUID sectionId) {
		
		ModelView view = modelViewRepository.findById(viewId);
		ModelSection section = modelSectionRepository.findById(sectionId);
		
		view.getSections().remove(section);
		view = modelViewRepository.save(view);
		
		return new PostResult("success", "section removed to view", view.getId().toString());
	}
	
	@Transactional
	public PostResult moveSection(UUID fromViewId, UUID sectionId, UUID beforeViewSectionId, UUID toSectionId, UUID beforeSubsectionId) {
		/* move a section within a view (it must be one of the top level sections of the view) */
		
		if (sectionId.equals(beforeViewSectionId)) {
			return new PostResult("warning", "cannot move on itself", null);
		}
		
		if (sectionId.equals(beforeSubsectionId)) {
			return new PostResult("warning", "cannot move on itself", null);
		}
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		ModelView view = modelViewRepository.findById(fromViewId);
		
		view.getSections().remove(section);
		
		if (toSectionId == null) {
			/* if toSectionId is null, then moving section within the same view */
			ModelSection beforeSection = modelSectionRepository.findById(beforeViewSectionId);
			int index = view.getSections().indexOf(beforeSection);
			view.getSections().add(index, section);
			section.setInitiative(view.getInitiative());
			
			modelViewRepository.save(view);
			modelSectionRepository.save(section);
			
		} else {
			/* if toSectionId is not null, then moving section from view to section */
			ModelSection toSection = modelSectionRepository.findById(toSectionId);
			
			if (beforeSubsectionId != null) {
				ModelSection beforeSubsection = modelSectionRepository.findById(beforeSubsectionId);
				int index = toSection.getSubsections().indexOf(beforeSubsection);
				toSection.getSubsections().add(index, section);
			} else {
				toSection.getSubsections().add(section);
			}
			section.setInitiative(toSection.getInitiative());
			
			modelSectionRepository.save(toSection);
			modelSectionRepository.save(section);
		}
		
		return new PostResult("success", "section moved", section.getId().toString());
	}
	
	@Transactional
	public PostResult moveSubsection(UUID fromSectionId, UUID subSectionId, UUID toViewId, UUID toSectionId, UUID beforeSubsectionId) {
		/* move a subsection to another section or subsection or, as top section, to a view */
		
		if (subSectionId.equals(toSectionId)) {
			return new PostResult("warning", "cannot move on itself", null);
		}
				
		ModelSection subSection = modelSectionRepository.findById(subSectionId);
		ModelSection fromSection = modelSectionRepository.findById(fromSectionId);
		
		/* remove subsection from section */
		fromSection.getSubsections().remove(subSection);
		fromSection = modelSectionRepository.save(fromSection);
	
		if(toViewId == null) {
			/* moving to another section add to section as subsection */
			ModelSection toSection = modelSectionRepository.findById(fromSectionId);
			
			if (beforeSubsectionId != null) {
				ModelSection beforeSubsection = modelSectionRepository.findById(beforeSubsectionId);
				int index = toSection.getSubsections().indexOf(beforeSubsection);
				toSection.getSubsections().add(index, subSection);
			} else {
				toSection.getSubsections().add(subSection);
			}
			subSection.setInitiative(toSection.getInitiative());
			
			modelSectionRepository.save(toSection);
			modelSectionRepository.save(subSection);
			
		} else {
			/* moving as section of a view  */
			ModelView toView = modelViewRepository.findById(toViewId);
			
			if (beforeSubsectionId != null) {
				ModelSection beforeSubsection = modelSectionRepository.findById(beforeSubsectionId);
				int index = toView.getSections().indexOf(beforeSubsection);
				toView.getSections().add(index, subSection);
			} else {
				toView.getSections().add(subSection);
			}
			subSection.setInitiative(toView.getInitiative());
			
			modelViewRepository.save(toView);
			modelSectionRepository.save(subSection);
		}
		
		return new PostResult("success", "subsection moved", subSection.getId().toString());
	}
	
	@Transactional
	public PostResult addSection (UUID sectionId, UUID onSectionId, UUID onViewId, UUID userId) {
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		
		if (onSectionId != null) {
			/* add it as subsection */
			ModelSection onSection = modelSectionRepository.findById(onSectionId);
			
			onSection.getSubsections().add(section);
			onSection = modelSectionRepository.save(onSection);
			
			return new PostResult("success", "section added to section", section.getId().toString());
			
		} else {
			/* add on view */
			ModelView onView = modelViewRepository.findById(onViewId);
			
			onView.getSections().add(section);
			onView = modelViewRepository.save(onView);
			
			return new PostResult("success", "section added to view", section.getId().toString());
		}
	}
		
	@Transactional
	public PostResult addCardToSection (UUID sectionId, UUID cardWrapperId, UUID beforeCardWrapperId) {
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		ModelCardWrapper cardWrapper = modelCardWrapperRepository.findById(cardWrapperId);
		
		if (beforeCardWrapperId == null) {
			section.getCardsWrappers().add(cardWrapper);
		} else {
			ModelCardWrapper beforeCardWrapper = modelCardWrapperRepository.findById(beforeCardWrapperId);
			int index = section.getCardsWrappers().indexOf(beforeCardWrapper);
			section.getCardsWrappers().add(index, cardWrapper);
		}
		
		section = modelSectionRepository.save(section);
		
		return new PostResult("success", "card added to section", section.getId().toString());
	}
	
	@Transactional
	public PostResult removeCardFromSection (UUID sectionId, UUID cardWrapperId) {
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		ModelCardWrapper cardWrapper = modelCardWrapperRepository.findById(cardWrapperId);
		
		section.getCardsWrappers().remove(cardWrapper);
		section = modelSectionRepository.save(section);
		
		return new PostResult("success", "card added to section", section.getId().toString());
	}
	
	@Transactional
	public ModelSectionDto addSectionSubElements(ModelSectionDto sectionDto, UUID sectionId, Integer level) {
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		
		if (level >= 1) {
			sectionDto.setSubElementsLoaded(true);
			
			for (ModelCardWrapper cardWrapper : section.getCardsWrappers()) {
				ModelCardWrapperDto cardWrapperDto = cardWrapper.toDto();
				List<ModelSection> inSections = modelCardWrapperRepository.findParentSections(cardWrapper.getId());
				
				for (ModelSection inSection : inSections) {
					cardWrapperDto.getInSections().add(inSection.toDto());
				}
				
				sectionDto.getCardsWrappers().add(cardWrapperDto);
			}
			
			for (ModelSection subsection : section.getSubsections()) {
				sectionDto.getSubsections().add(addSectionSubElements(subsection.toDto(), subsection.getId(), level - 1));
			}
		} else {
			sectionDto.setSubElementsLoaded(false);
			for (ModelSection subsection : section.getSubsections()) {
				sectionDto.getSubsections().add(subsection.toDto());
			}
		}
		
		return sectionDto; 
	}
	
	@Transactional
	public PostResult deleteSection (UUID sectionId, UUID creatorId) {
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		
		/* remove references to views */
		List<ModelView> views = modelSectionRepository.findParentViews(sectionId);
		if (views.size() > 0) {
			for (ModelView view : views) {
				view.getSections().remove(section);
			}
		}
		
		/* remove references to parent sections */
		List<ModelSection> parents = modelSectionRepository.findParentSections(sectionId);
		if (parents.size() > 0) {
			for (ModelSection parent : parents) {
				parent.getSubsections().remove(section);
			}
		}
		
		modelSectionRepository.delete(section);
		
		return new PostResult("success", "section deleted", "");
	}
	
	@Transactional
	public PostResult createCardWrapper(ModelCardDto cardDto, UUID sectionId, UUID creatorId) {
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		if (section == null) return new PostResult("error", "view not found", "");
		
		ModelCard card = cardDto.toEntity(null, cardDto, null);
		
		if (cardDto.getNewImageFileId() != null) {
			UUID imageFileId = cardDto.getNewImageFileId().equals("") ? null : UUID.fromString(cardDto.getNewImageFileId());
			FileStored imageFile = fileStoredRepository.findById(imageFileId);
			card.setImageFile(imageFile);
		}
		
		card = modelCardRepository.save(card);
		
		ModelCardWrapper cardWrapper = new ModelCardWrapper();
		cardWrapper.setCard(card);
		cardWrapper.setOtherProperties(cardDto);
		cardWrapper.setInitiative(section.getInitiative());
		
		cardWrapper = modelCardWrapperRepository.save(cardWrapper);
		
		section.getCardsWrappers().add(cardWrapper);
		modelSectionRepository.save(section);
		
		return new PostResult("success", "card created", card.getId().toString());
	}
	
	@Transactional
	public PostResult editCardWrapper(UUID initiativeId, UUID cardWrapperId, ModelCardDto cardDto, UUID creatorId) {
		
		Initiative initiative = initiativeRepository.findById(initiativeId);
		if (initiative == null) return new PostResult("error", "initiative not found", "");
		
		ModelCardWrapper cardWrapper = modelCardWrapperRepository.findById(cardWrapperId);
		if (cardWrapper == null) return new PostResult("error", "card wrapper not found", "");
		
		ModelCard originalCard = cardWrapper.getCard();
		
		cardWrapper.getOldVersions().add(originalCard);
		
		ModelCard card = cardDto.toEntity(null, cardDto, null);
		
		/* update or remove image */
		if (cardDto.getNewImageFileId() != null) {
			if(!cardDto.getNewImageFileId().equals("REMOVE")) {
				UUID imageFileId = cardDto.getNewImageFileId().equals("") ? null : UUID.fromString(cardDto.getNewImageFileId());
				FileStored imageFile = fileStoredRepository.findById(imageFileId);
				card.setImageFile(imageFile);
			} else {
				card.setImageFile(null);
			}
		} else {
			/* use the same image */
			card.setImageFile(originalCard.getImageFile());
		} 
		
		card = modelCardRepository.save(card);
		
		cardWrapper.setCard(card);
		cardWrapper.setOtherProperties(cardDto);
		
		return new PostResult("success", "card edited", cardWrapper.getId().toString());
	}
	
	
	@Transactional
	public PostResult moveCardWrapper(UUID fromSectionId, UUID cardWrapperId, UUID toSectionId, UUID beforeCardWrapperId) {
		
		if (cardWrapperId.equals(beforeCardWrapperId)) {
			return new PostResult("error", "cannot move on itself", null);
		}
		
		ModelCardWrapper cardWrapper = modelCardWrapperRepository.findById(cardWrapperId);
		if (cardWrapper == null) return new PostResult("error", "card wrapper not found", "");
		
		/* remove from section */
		ModelSection fromSection = modelSectionRepository.findById(fromSectionId);
		fromSection.getCardsWrappers().remove(cardWrapper);
		modelSectionRepository.save(fromSection);
		
		/* add to section */
		ModelSection toSection = modelSectionRepository.findById(toSectionId);
		
		if (beforeCardWrapperId != null) {
			ModelCardWrapper beforeCardWrapper = modelCardWrapperRepository.findById(beforeCardWrapperId);
			int index = toSection.getCardsWrappers().indexOf(beforeCardWrapper);
			toSection.getCardsWrappers().add(index, cardWrapper);
			
		} else {
			toSection.getCardsWrappers().add(cardWrapper);
		}
		
		cardWrapper.setInitiative(toSection.getInitiative());
		
		modelSectionRepository.save(toSection);
		modelCardWrapperRepository.save(cardWrapper);
		
		return new PostResult("success", "card moved", cardWrapper.getId().toString());
	}
	
	@Transactional
	public GetResult<ModelCardWrapperDto> getCardWrapper(UUID cardWrapperId, UUID requestById) {
		ModelCardWrapper cardWrapper = modelCardWrapperRepository.findById(cardWrapperId);
		List<ModelSection> inSections = modelCardWrapperRepository.findParentSections(cardWrapper.getId());
		
		ModelCardWrapperDto cardWrapperDto = cardWrapper.toDto();
		
		for (ModelSection section : inSections) {
			cardWrapperDto.getInSections().add(section.toDto());
		}
		
		return new GetResult<ModelCardWrapperDto>("success", "card retrieved", cardWrapperDto);
	}
	
	@Transactional
	public Initiative getCardWrapperInitiative(UUID cardWrapperId) {
		return modelCardWrapperRepository.findById(cardWrapperId).getInitiative();
	}
	
	@Transactional
	public GetResult<Page<ModelCardWrapperDto>> searchCardWrapper(String query, PageRequest page, UUID initiativeId) {
		List<UUID> initiativeEcosystemIds = initiativeService.findAllInitiativeEcosystemIds(initiativeId);
		Page<ModelCardWrapper> enititiesPage = modelCardWrapperRepository.searchBy("%"+query.toLowerCase()+"%", initiativeEcosystemIds, page);
		
		List<ModelCardWrapperDto> cardsDtos = new ArrayList<ModelCardWrapperDto>();
		
		for(ModelCardWrapper cardWrapper : enititiesPage.getContent()) {
			List<ModelSection> inSections = modelCardWrapperRepository.findParentSections(cardWrapper.getId());
			
			ModelCardWrapperDto cardWrapperDto = cardWrapper.toDto();
			
			for (ModelSection section : inSections) {
				cardWrapperDto.getInSections().add(section.toDto());
			}
			
			cardsDtos.add(cardWrapperDto);
		}
		
		Page<ModelCardWrapperDto> dtosPage = new PageImpl<ModelCardWrapperDto>(cardsDtos, page, enititiesPage.getNumberOfElements());
		
		return new GetResult<Page<ModelCardWrapperDto>>("succes", "cards returned", dtosPage);
	}
		
	@Transactional
	public PostResult deleteCardWrapper (UUID cardWrapperId, UUID creatorId) {
		
		List<ModelSection> parents = modelCardWrapperRepository.findParentSections(cardWrapperId);
		ModelCardWrapper card = modelCardWrapperRepository.findById(cardWrapperId);
		
		/* remove references */
		if (parents.size() > 0) {
			for (ModelSection parent : parents) {
				parent.getCardsWrappers().remove(card);
				modelSectionRepository.save(parent);
			}
		}
		
		modelCardWrapperRepository.delete(card);
		
		return new PostResult("success", "section deleted", "");
	}
	
	
}
