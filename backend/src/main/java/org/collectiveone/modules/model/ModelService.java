package org.collectiveone.modules.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.activity.Activity;
import org.collectiveone.modules.activity.ActivityService;
import org.collectiveone.modules.activity.dto.ActivityDto;
import org.collectiveone.modules.activity.enums.ActivityType;
import org.collectiveone.modules.activity.repositories.ActivityRepositoryIf;
import org.collectiveone.modules.conversations.Message;
import org.collectiveone.modules.conversations.MessageRepositoryIf;
import org.collectiveone.modules.files.FileService;
import org.collectiveone.modules.files.FileStored;
import org.collectiveone.modules.files.FileStoredRepositoryIf;
import org.collectiveone.modules.governance.CardLike;
import org.collectiveone.modules.governance.CardLikeRepositoryIf;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.collectiveone.modules.initiatives.repositories.InitiativeRepositoryIf;
import org.collectiveone.modules.model.dto.CardWrappersHolderDto;
import org.collectiveone.modules.model.dto.ModelCardDto;
import org.collectiveone.modules.model.dto.ModelCardWrapperDto;
import org.collectiveone.modules.model.dto.ModelSectionDto;
import org.collectiveone.modules.model.dto.ModelSectionLinkedDto;
import org.collectiveone.modules.model.repositories.ModelCardRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelCardWrapperAdditionRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelCardWrapperRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelSectionRepositoryIf;
import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ModelService {
	
	@Autowired 
	private InitiativeService initiativeService;
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private InitiativeRepositoryIf initiativeRepository;
	
	@Autowired
	private AppUserRepositoryIf appUserRepository;
	
	@Autowired
	private ModelSectionRepositoryIf modelSectionRepository;
	
	@Autowired
	private ModelCardWrapperRepositoryIf modelCardWrapperRepository;
	
	@Autowired
	private ModelCardRepositoryIf modelCardRepository;
	
	@Autowired
	private FileStoredRepositoryIf fileStoredRepository;
	
	@Autowired
	private ActivityRepositoryIf activityRepository;
	
	@Autowired
	private MessageRepositoryIf messageRepository;
	
	@Autowired
	private CardLikeRepositoryIf cardLikeRepository;
	
	@Autowired
	private ModelCardWrapperAdditionRepositoryIf modelCardWrapperAdditionRepository;
	
	
	@Transactional
	public GetResult<ModelSectionDto> getModel(UUID initiativeId, Integer level, UUID requestById, Boolean onlySections) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
		if (initiative == null) return new GetResult<ModelSectionDto>("error", "initiative not found", null);
		
		ModelSectionDto sectionDto = initiative.getTopModelSection().toDto();
		if (level > 0) {
			sectionDto = addSectionSubElements(sectionDto, initiative.getTopModelSection().getId(), level - 1, requestById, onlySections);	
		}
			
		return new GetResult<ModelSectionDto> ("success", "model found", sectionDto);
	}
	
	@Transactional
	public Initiative getSectionInitiative (UUID sectionId) {
		return modelSectionRepository.findById(sectionId).getInitiative();
	}
	
	@Transactional
	public PostResult createSection(ModelSectionDto sectionDto, UUID parentSectionId, UUID creatorId, boolean register) {
		
		ModelSection section = sectionDto.toEntity(null, sectionDto);
		section = modelSectionRepository.save(section);
		
		ModelSection parent = modelSectionRepository.findById(parentSectionId);
		if (parent == null) return new PostResult("error", "parent section not found", "");
		
		parent.getSubsections().add(section);
		section.setInitiative(parent.getInitiative());
		
		if (register) activityService.modelSectionCreatedOnSection(section, parent, appUserRepository.findByC1Id(creatorId));
		
		modelSectionRepository.save(parent);
		
		return new PostResult("success", "section created", section.getId().toString());
	}
	
	@Transactional
	public GetResult<ModelSectionDto> getSection(UUID sectionId, Integer level, UUID requestByUserId, Boolean onlySections) {
		return new GetResult<ModelSectionDto>("success", "section retrieved",  getSectionDto(sectionId, level, requestByUserId, onlySections));
	}
	
	@Transactional
	public ModelSectionDto getSectionDto(UUID sectionId, Integer level, UUID requestByUserId, Boolean onlySections) {
		ModelSection section = modelSectionRepository.findById(sectionId);
		ModelSectionDto sectionDto = section.toDto();
		
		List<ModelSection> inSections = modelSectionRepository.findParentSections(section.getId());
		
		for (ModelSection inSection : inSections) {
			sectionDto.getInSections().add(inSection.toDto());
		}
		
		if(level > 0) {
			sectionDto = addSectionSubElements(sectionDto, section.getId(), level - 1, requestByUserId, onlySections);
		}
		
		return sectionDto;
	}
	
	@Transactional
	public GetResult<CardWrappersHolderDto> getSectionCardWrappers(UUID sectionId, UUID requestByUserId) {
		ModelSection section = modelSectionRepository.findById(sectionId);
		
		CardWrappersHolderDto cardWrappersHolder = getSectionCardWrappersDtos(section.getId(), requestByUserId);
		
		return new GetResult<CardWrappersHolderDto>("success", "section retrieved",  cardWrappersHolder);
	}
	
	
	@Transactional
	public GetResult<Page<ModelSectionDto>> searchSection(String query, PageRequest page, UUID initiativeId, UUID requestByUserId) {
		
		List<UUID> initiativeEcosystemIds = initiativeService.findAllInitiativeEcosystemIds(initiativeId);
		Page<ModelSection> enititiesPage = modelSectionRepository.searchBy("%"+query.toLowerCase()+"%", initiativeEcosystemIds, page);
		
		List<ModelSectionDto> sectionDtos = new ArrayList<ModelSectionDto>();
		
		for(ModelSection section : enititiesPage.getContent()) {
			sectionDtos.add(getSectionDto(section.getId(), 0, requestByUserId, true));
		}
		
		Page<ModelSectionDto> dtosPage = new PageImpl<ModelSectionDto>(sectionDtos, page, enititiesPage.getNumberOfElements());
		
		return new GetResult<Page<ModelSectionDto>>("succes", "sections returned", dtosPage);
	}
	
	@Transactional
	public PostResult editSection (UUID sectionId, ModelSectionDto sectionDto, UUID creatorId) {
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		
		section = sectionDto.toEntity(section, sectionDto);
		section = modelSectionRepository.save(section);
		
		activityService.modelSectionEdited(section, appUserRepository.findByC1Id(creatorId));
		
		return new PostResult("success", "section edited", section.getId().toString());
	}
	
	@Transactional
	public PostResult removeSubsectionFromSection(UUID sectionId, UUID subsectionId, UUID creatorId) {
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		ModelSection subsection = modelSectionRepository.findById(subsectionId);
		
		section.getSubsections().remove(subsection);
		section.getSubsectionsTrash().add(subsection);
		
		section = modelSectionRepository.save(section);
		
		activityService.modelSectionRemovedFromSection(subsection, section, appUserRepository.findByC1Id(creatorId));
		
		return new PostResult("success", "subsection removed to section", section.getId().toString());
	}
	
	@Transactional
	public PostResult moveSubsection(
			UUID fromSectionId, 
			UUID subSectionId, 
			UUID toSectionId, 
			UUID beforeSubsectionId,
			UUID creatorId) {
		/* move a subsection to another section or subsection or, as top section, to a view */
		
		if (subSectionId.equals(toSectionId)) {
			return new PostResult("warning", "cannot move on itself", null);
		}
				
		ModelSection subSection = modelSectionRepository.findById(subSectionId);
		ModelSection fromSection = modelSectionRepository.findById(fromSectionId);
		
		/* remove subsection from section */
		fromSection.getSubsections().remove(subSection);
		
		/* moving to another section add to section as subsection */
		ModelSection toSection = modelSectionRepository.findById(toSectionId);
		
		if (beforeSubsectionId != null) {
			ModelSection beforeSubsection = modelSectionRepository.findById(beforeSubsectionId);
			int index = toSection.getSubsections().indexOf(beforeSubsection);
			if (index < 0) {
				return new PostResult("error", "error while moving section", subSection.getId().toString());
			}
			toSection.getSubsections().add(index, subSection);
		} else {
			toSection.getSubsections().add(subSection);
		}
		
		fromSection = modelSectionRepository.save(fromSection);
		subSection.setInitiative(toSection.getInitiative());
				
		activityService.modelSectionMovedFromSectionToSection(subSection, fromSection, toSection, appUserRepository.findByC1Id(creatorId));
		
		modelSectionRepository.save(toSection);
		modelSectionRepository.save(subSection);
		
		return new PostResult("success", "subsection moved", subSection.getId().toString());
	}
	
	@Transactional
	public PostResult addSection (UUID sectionId, UUID onSectionId, UUID beforeSubsectionId, UUID creatorId) {
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		
		/* add it as subsection */
		ModelSection onSection = modelSectionRepository.findById(onSectionId);
		
		if (beforeSubsectionId != null) {
			ModelSection beforeSubsection = modelSectionRepository.findById(beforeSubsectionId);
			int index = onSection.getSubsections().indexOf(beforeSubsection);
			onSection.getSubsections().add(index, section);
		} else {
			onSection.getSubsections().add(section);
		}
		
		onSection = modelSectionRepository.save(onSection);
		
		activityService.modelNewSubsection(section, onSection, appUserRepository.findByC1Id(creatorId));
		
		return new PostResult("success", "section added to section", section.getId().toString());
	}
		
	@Transactional
	public PostResult addCardToSection (
			UUID sectionId, 
			UUID cardWrapperId, 
			UUID beforeCardWrapperId, 
			UUID requestByUserId,
			ModelCardWrapperScope scope) {
		
		ModelCardWrapperAddition existingCard = 
				modelCardWrapperAdditionRepository.findBySectionAndCardWrapperVisibleToUser(sectionId, cardWrapperId, requestByUserId);
		
		if (existingCard != null) {
			return new PostResult("error", "card already in this section", null);
		}
		
		ModelCardWrapper cardWrapper = modelCardWrapperRepository.findById(cardWrapperId);
		
		ModelCardWrapperAddition beforeCardWrapperAddition = 
				modelCardWrapperAdditionRepository.findBySectionAndCardWrapperVisibleToUser(sectionId, beforeCardWrapperId, requestByUserId);
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		ModelCardWrapperAddition cardWrapperAddition = new ModelCardWrapperAddition();
		
		cardWrapperAddition.setAdder(appUserRepository.findByC1Id(requestByUserId));
		cardWrapperAddition.setIsBefore(true);
		cardWrapperAddition.setSection(section);
		cardWrapperAddition.setCardWrapper(cardWrapper);
		cardWrapperAddition.setOnCardWrapperAddition(beforeCardWrapperAddition);
		cardWrapperAddition.setScope(scope);
		
		modelCardWrapperAdditionRepository.save(cardWrapperAddition);
				
		switch (scope) {
			case SHARED:
				break;
				
			case PRIVATE:
				if (scope == ModelCardWrapperScope.SHARED) {
					activityService.modelCardWrapperAdded(cardWrapperAddition, appUserRepository.findByC1Id(requestByUserId));
				}
				
				modelSectionRepository.save(section);
				break;
				
			case COMMON:
				
				/* check index in array of common cards */
				if (beforeCardWrapperId == null) {
					section.getCardsWrappersAdditionsCommon().add(cardWrapperAddition);
				} else {
					int index = section.getCardsWrappersAdditionsCommon().indexOf(beforeCardWrapperAddition);
					if (index != -1) {
						section.getCardsWrappersAdditionsCommon().add(index, cardWrapperAddition);	
					} else {
						section.getCardsWrappersAdditionsCommon().add(cardWrapperAddition);
					}
					
				}
				
				activityService.modelCardWrapperAdded(cardWrapperAddition, appUserRepository.findByC1Id(requestByUserId));
				
				section = modelSectionRepository.save(section);
				break;
		}
		
		return new PostResult("success", "card added to section", section.getId().toString());
	}
	
	@Transactional
	public PostResult removeCardFromSection (UUID sectionId, UUID cardWrapperId, UUID requestedById) {
		
		ModelCardWrapperAddition cardWrapperAddition = 
				modelCardWrapperAdditionRepository.findBySectionAndCardWrapperVisibleToUser(sectionId, cardWrapperId, requestedById);
		
		/* check permisson */
		if (cardWrapperAddition.getScope() == ModelCardWrapperScope.PRIVATE || 
				cardWrapperAddition.getScope() == ModelCardWrapperScope.SHARED) {
			if (cardWrapperAddition.getAdder().getC1Id() != requestedById) {
				return new PostResult("error", "only adder can move shared", cardWrapperAddition.getId().toString());
			}
		}
		
		cardWrapperAddition.setStatus(Status.DELETED);
		
		modelCardWrapperAdditionRepository.save(cardWrapperAddition);
		
		/* remove from section if common */
		if (cardWrapperAddition.getScope() == ModelCardWrapperScope.COMMON) {
			ModelSection section = cardWrapperAddition.getSection();
			if (section.getCardsWrappersAdditionsCommon().contains(cardWrapperAddition)) {
				section.getCardsWrappersAdditionsCommon().remove(cardWrapperAddition);
				modelSectionRepository.save(section);	
			}
		}
		
		if (cardWrapperAddition.getScope() != ModelCardWrapperScope.PRIVATE) {
			activityService.modelCardWrapperRemoved(cardWrapperAddition, appUserRepository.findByC1Id(requestedById));
		}
		
		return new PostResult("success", "card added to section", cardWrapperAddition.getId().toString());
	}
	
	@Transactional
	private CardWrappersHolderDto getSectionCardWrappersDtos(UUID sectionId, UUID requestByUserId) {
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		CardWrappersHolderDto cardWrappersHolder = new CardWrappersHolderDto();
		
		for (ModelCardWrapperAddition cardWrapperAddition : section.getCardsWrappersAdditionsCommon()) {
			cardWrappersHolder.getCardsWrappersCommon().add(getCardWrapperDtoWithMetadata(cardWrapperAddition, requestByUserId));
		}
		
		List<ModelCardWrapperAddition> modelCardWrappersPrivate = 
				modelCardWrapperAdditionRepository.findOfUserInSection(requestByUserId, section.getId(), ModelCardWrapperScope.PRIVATE);
		
		for (ModelCardWrapperAddition cardWrapperAddition : modelCardWrappersPrivate) {
			ModelCardWrapperDto cardWrapperDto = getCardWrapperDtoWithMetadata(cardWrapperAddition, requestByUserId);
			cardWrappersHolder.getCardsWrappersPrivate().add(cardWrapperDto);
		}
		
		/* if request user is in ecosystem add shared cards too */
		if(initiativeService.isMemberOfEcosystem(section.getInitiative().getId(), requestByUserId)) {
			
			List<ModelCardWrapperAddition> modelCardWrappersShared = 
					modelCardWrapperAdditionRepository.findInSectionWithScope(section.getId(), ModelCardWrapperScope.SHARED);
			
			for (ModelCardWrapperAddition cardWrapperAddition : modelCardWrappersShared) {
				ModelCardWrapperDto cardWrapperDto = getCardWrapperDtoWithMetadata(cardWrapperAddition, requestByUserId);
				cardWrappersHolder.getCardsWrappersShared().add(cardWrapperDto);
			}
		}
		
		return cardWrappersHolder;
	}
	
	@Transactional
	public ModelSectionDto addSectionSubElements(ModelSectionDto sectionDto, UUID sectionId, Integer level, UUID requestByUserId, Boolean onlySections) {
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		sectionDto.setSubElementsLoaded(true);
		
		if (!onlySections) {
			CardWrappersHolderDto cardWrappersHolder = getSectionCardWrappersDtos(section.getId(), requestByUserId);
			
			sectionDto.setCardsWrappersCommon(cardWrappersHolder.getCardsWrappersCommon());
			sectionDto.setCardsWrappersPrivate(cardWrappersHolder.getCardsWrappersPrivate());
			sectionDto.setCardsWrappersShared(cardWrappersHolder.getCardsWrappersShared());
		}
				
		if (level > 0) {
			/* add the subsections with their sub-elements too */
			for (ModelSection subsection : section.getSubsections()) {
				sectionDto.getSubsections().add(addSectionSubElements(subsection.toDto(), subsection.getId(), level - 1, requestByUserId, onlySections));
			}
		} 
		
		return sectionDto; 
	}
	
	private ModelCardWrapperDto getCardWrapperDtoWithMetadata(ModelCardWrapperAddition cardWrapperAddition, UUID requestByUserId) {
		
		/* Card wrapper additions are converted into card wrappers dtos for simplicity */
		ModelCardWrapperDto cardWrapperDto = cardWrapperAddition.getCardWrapper().toDto();
		cardWrapperDto.setScope(cardWrapperAddition.getScope());
		
		/* check if this card wrapper is private on that section only adder is able to see it */
		if (cardWrapperAddition.getScope() == ModelCardWrapperScope.PRIVATE) {
			if (!cardWrapperAddition.getAdder().getC1Id().equals(requestByUserId)) {
				return null;
			}
		}
		
		if (cardWrapperAddition.getOnCardWrapperAddition() != null) {
			cardWrapperDto.setOnCardWrapperId(cardWrapperAddition.getOnCardWrapperAddition().getCardWrapper().getId().toString());
			cardWrapperDto.setIsBefore(cardWrapperAddition.getIsBefore());
		}
		
		if (cardWrapperAddition.getSection() != null) {
			/* set initiative based on section */
			cardWrapperDto.setInitiativeId(cardWrapperAddition.getSection().getInitiative().getId().toString());
		} else {
			cardWrapperDto.setInitiativeId(cardWrapperAddition.getCardWrapper().getInitiative().getId().toString());
		}
		
		/* get number of likes */
		cardWrapperDto.setnLikes(cardLikeRepository.countOfCard(cardWrapperAddition.getCardWrapper().getId()));
		
		if (requestByUserId != null) {
			cardWrapperDto.setUserLiked(cardLikeRepository.findByCardWrapperIdAndAuthor_c1Id(cardWrapperAddition.getCardWrapper().getId(), requestByUserId) != null);
		}
		
		List<ModelCardWrapperAddition> allAdditions = modelCardWrapperAdditionRepository.findOfCardWrapper(cardWrapperAddition.getCardWrapper().getId());
		
		for (ModelCardWrapperAddition thisAddition : allAdditions) {
			/* private info only for author */
			boolean skip = false;
			if (thisAddition.getScope() == ModelCardWrapperScope.PRIVATE) {
				if (!thisAddition.getAdder().getC1Id().equals(requestByUserId)) {
					skip = true;
				}
			}
			
			if (!skip) {
				cardWrapperDto.getInModelSections().add(thisAddition.toInModelSectionDto());	
			}
		}
		
		return cardWrapperDto;
	}
	
	@Transactional
	public PostResult deleteSection (UUID sectionId, UUID creatorId) {
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		
		/* remove references to parent sections */
		List<ModelSection> parents = modelSectionRepository.findParentSections(sectionId);
		if (parents.size() > 0) {
			for (ModelSection parent : parents) {
				parent.getSubsections().remove(section);
				parent.getSubsectionsTrash().add(section);
			}
		}
		
		
		section = modelSectionRepository.save(section);
		
		activityService.modelSectionDeleted(section, appUserRepository.findByC1Id(creatorId));
		
		return new PostResult("success", "section deleted", section.getId().toString());
	}
	
	@Transactional
	public PostResult createCardWrapper(ModelCardDto cardDto, UUID sectionId, UUID creatorId, UUID beforeId, UUID afterId) {
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		if (section == null) return new PostResult("error", "section not found", "");
		
		ModelCard card = cardDto.toEntity(null, cardDto, null);
		
		card = modelCardRepository.save(card);
		
		if (cardDto.getNewImageFileId() != null) {
			UUID imageFileId = cardDto.getNewImageFileId().equals("") ? null : UUID.fromString(cardDto.getNewImageFileId());
			if (imageFileId != null) {
				/* copy image from temporary location to card fixed location, needed the card ID for this */
				UUID newFileId = fileService.copyImageAfterCreationToCard(creatorId, imageFileId, card.getId());
				if (newFileId != null) {
					FileStored newImageFile = fileStoredRepository.findById(newFileId);
					card.setImageFile(newImageFile);	
				}
			}
		}
		
		AppUser creator = appUserRepository.findByC1Id(creatorId);
		
		/* create card wrapper */
		ModelCardWrapper cardWrapper = new ModelCardWrapper();
		cardWrapper.setCard(card);
		cardWrapper.setInitiative(section.getInitiative());
		cardWrapper.setCreator(creator);
		cardWrapper.setCreationDate(new Timestamp(System.currentTimeMillis()));
		cardWrapper.getEditors().add(creator);
		cardWrapper.setLastEdited(new Timestamp(System.currentTimeMillis()));
		
		cardWrapper = modelCardWrapperRepository.save(cardWrapper);
		
		/* create cardwrapper addition */
		ModelCardWrapperAddition cardWrapperAddition = new ModelCardWrapperAddition();
		ModelCardWrapperAddition onCardWrapperAddition = null;
				
		cardWrapperAddition.setAdder(creator);
		cardWrapperAddition.setSection(section);
		cardWrapperAddition.setCardWrapper(cardWrapper);
		cardWrapperAddition.setOnCardWrapperAddition(onCardWrapperAddition);
		cardWrapperAddition.setScope(cardDto.getNewScope());
		
		modelCardWrapperAdditionRepository.save(cardWrapperAddition);
		
		/* add to section */
		switch (cardDto.getNewScope()) {
		
			case PRIVATE:
			case SHARED:
				
				/* order is relative to other cards */
				if (beforeId != null) { 
					onCardWrapperAddition = modelCardWrapperAdditionRepository.findById(beforeId);
					cardWrapperAddition.setIsBefore(true);
				} else if (afterId != null) {
					onCardWrapperAddition = modelCardWrapperAdditionRepository.findById(afterId);
					cardWrapperAddition.setIsBefore(false);
				} 

				
				if (cardDto.getNewScope() == ModelCardWrapperScope.SHARED) {
					activityService.modelCardWrapperCreated(cardWrapperAddition, appUserRepository.findByC1Id(creatorId));
				}
				
				break;
			
			case COMMON:
				
				/* order is absolute in the section and uses hibernate @OrderColumn */
				
				Integer ix = null;
				
				if (beforeId != null) {
					
					ModelCardWrapperAddition beforeCardWrapperAddition = modelCardWrapperAdditionRepository.findById(beforeId);
					ix = section.getCardsWrappersAdditionsCommon().indexOf(beforeCardWrapperAddition);
					
				} else if (afterId != null) {
					
					ModelCardWrapperAddition afterCardWrapperAddition = modelCardWrapperAdditionRepository.findById(afterId);
					ix = section.getCardsWrappersAdditionsCommon().indexOf(afterCardWrapperAddition) + 1;
					
				}
			
				/* add in the correct location */
				if (ix == null) {
					/* at the end */
					section.getCardsWrappersAdditionsCommon().add(cardWrapperAddition);
				} else {
					if ((ix == -1) || (ix == section.getCardsWrappersAdditionsCommon().size() - 1)) {
						/* at the end */
						section.getCardsWrappersAdditionsCommon().add(cardWrapperAddition);
					} else {
						/* at a given ix */
						section.getCardsWrappersAdditionsCommon().add(ix, cardWrapperAddition);
					}
				}
				
				modelSectionRepository.save(section);
				activityService.modelCardWrapperCreated(cardWrapperAddition, appUserRepository.findByC1Id(creatorId));
				
				break;
				
		}
		
		return new PostResult("success", "card created", card.getId().toString());
	}
	
	@Transactional
	public PostResult editCardWrapper(
			UUID initiativeId, 
			UUID inSectionId, 
			UUID cardWrapperId, 
			ModelCardDto cardDto, 
			UUID creatorId) {
		
		Initiative initiative = initiativeRepository.findById(initiativeId);
		if (initiative == null) return new PostResult("error", "initiative not found", "");
		
		ModelCardWrapper cardWrapper = modelCardWrapperRepository.findById(cardWrapperId);
		if (cardWrapper == null) return new PostResult("error", "card wrapper not found", "");
		
		ModelCardWrapperAddition cardWrapperAddition = 
				modelCardWrapperAdditionRepository.findBySectionAndCardWrapperVisibleToUser(inSectionId, cardWrapperId, creatorId);
		
		if (cardWrapperAddition != null) {
			cardWrapperAddition.setScope(cardDto.getNewScope());	
			modelCardWrapperAdditionRepository.save(cardWrapperAddition);
		}
		
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
		cardWrapper.setLastEdited(new Timestamp(System.currentTimeMillis()));
		
		/* add an editor only once */
		AppUser prevEditor = modelCardWrapperRepository.findEditor(cardWrapper.getId(), creatorId);
		if (prevEditor == null) {
			cardWrapper.getEditors().add(appUserRepository.findByC1Id(creatorId));
		}
		
		if (cardWrapperAddition.getScope() != ModelCardWrapperScope.PRIVATE) {
			activityService.modelCardWrapperEdited(cardWrapperAddition, appUserRepository.findByC1Id(creatorId));	
		}
		
		return new PostResult("success", "card edited", cardWrapper.getId().toString());
	}
	
	@Transactional
	public PostResult makeCardWrapperShared(UUID cardWrapperId, UUID onSectionId, UUID requestByUserId) {
		
		ModelCardWrapperAddition cardWrapperAddition = 
				modelCardWrapperAdditionRepository.findBySectionAndCardWrapperIdAndScope(onSectionId, cardWrapperId, ModelCardWrapperScope.PRIVATE);
				
		if (cardWrapperAddition == null) return new PostResult("error", "card wrapper not found", "");
		
		cardWrapperAddition.setScope(ModelCardWrapperScope.SHARED);
		modelCardWrapperAdditionRepository.save(cardWrapperAddition);
		
		activityService.modelCardWrapperMadeShared(cardWrapperAddition, appUserRepository.findByC1Id(requestByUserId));
		
		return new PostResult("success", "card edited", cardWrapperAddition.getId().toString());
	}
	
	@Transactional
	public PostResult makeCardWrapperCommon(UUID cardWrapperId, UUID onSectionId, UUID requestByUserId) {
		
		ModelCardWrapperAddition cardWrapperAddition = 
				modelCardWrapperAdditionRepository.findBySectionAndCardWrapperVisibleToUser(onSectionId, cardWrapperId, requestByUserId);
				
		if (cardWrapperAddition == null) return new PostResult("error", "card wrapper not found", "");
		
		cardWrapperAddition.setScope(ModelCardWrapperScope.COMMON);
		
		/* update the order */
		ModelSection section = cardWrapperAddition.getSection();
		
		if (cardWrapperAddition.getOnCardWrapperAddition() != null) {
			int ix = section.getCardsWrappersAdditionsCommon().indexOf(cardWrapperAddition);
			if (ix != -1) {
				if (cardWrapperAddition.getIsBefore()) {
					section.getCardsWrappersAdditionsCommon().add(ix, cardWrapperAddition);
				} else {
					section.getCardsWrappersAdditionsCommon().add(ix + 1, cardWrapperAddition);
				}
			} else {
				section.getCardsWrappersAdditionsCommon().add(cardWrapperAddition);
			}
		} else {
			section.getCardsWrappersAdditionsCommon().add(cardWrapperAddition);
		}
		
		modelSectionRepository.save(section);
		
		activityService.modelCardWrapperMadeCommon(cardWrapperAddition, appUserRepository.findByC1Id(requestByUserId));
		
		return new PostResult("success", "card wrapper converted to common", cardWrapperAddition.getId().toString());
	}
	
	
	@Transactional
	public PostResult moveCardWrapper(
			UUID fromSectionId, 
			UUID cardWrapperId, 
			UUID toSectionId, 
			UUID beforeCardWrapperId, 
			UUID requestedById) {
		
		if (cardWrapperId.equals(beforeCardWrapperId)) {
			return new PostResult("error", "cannot move on itself", null);
		}
		
		/* check there is not a visible card already in this section */
		if (!fromSectionId.equals(toSectionId)) {
			ModelCardWrapperAddition existingCard = 
					modelCardWrapperAdditionRepository.findBySectionAndCardWrapperVisibleToUser(toSectionId, cardWrapperId, requestedById);
					
			if (existingCard != null) {
				return new PostResult("error", "card already in this section", null);
			}	
		}
		
		/* remove card from section (just mark it as deleted)*/
		ModelCardWrapperAddition cardWrapperAdditionFrom = 
				modelCardWrapperAdditionRepository.findBySectionAndCardWrapperVisibleToUser(fromSectionId, cardWrapperId, requestedById);
		
		ModelSection fromSection = modelSectionRepository.findById(fromSectionId);
		
		cardWrapperAdditionFrom.setStatus(Status.DELETED);
		
		if (cardWrapperAdditionFrom.getScope() == ModelCardWrapperScope.COMMON) {
			fromSection.getCardsWrappersAdditionsCommon().remove(cardWrapperAdditionFrom);
			modelSectionRepository.save(fromSection);
		}
		
		modelCardWrapperAdditionRepository.save(cardWrapperAdditionFrom);
		
		
		/* create new addition on the destionation card */
		ModelCardWrapperAddition cardWrapperAdditionTo = new ModelCardWrapperAddition();
		
		
		/* set section */
		ModelSection toSection = modelSectionRepository.findById(toSectionId);
		
		cardWrapperAdditionTo.setAdder(appUserRepository.findByC1Id(requestedById));
		cardWrapperAdditionTo.setSection(toSection);
		cardWrapperAdditionTo.setCardWrapper(cardWrapperAdditionFrom.getCardWrapper());
		cardWrapperAdditionTo.setScope(cardWrapperAdditionFrom.getScope());
		
		modelCardWrapperAdditionRepository.save(cardWrapperAdditionTo);
		
		/* set order */
		ModelCardWrapperAddition beforeCardWrapperAddition = null;
		int onIndex = -1;
		
		if (beforeCardWrapperId != null) {
			beforeCardWrapperAddition = 
					modelCardWrapperAdditionRepository.findBySectionAndCardWrapperVisibleToUser(toSectionId, beforeCardWrapperId, requestedById);
		}
		
		switch (cardWrapperAdditionTo.getScope()) {
			case PRIVATE:
			case SHARED:
				if (beforeCardWrapperAddition != null) {
					cardWrapperAdditionTo.setOnCardWrapperAddition(beforeCardWrapperAddition);
					cardWrapperAdditionTo.setIsBefore(true);
				} else {
					cardWrapperAdditionTo.setOnCardWrapperAddition(null);
				}
				modelCardWrapperAdditionRepository.save(cardWrapperAdditionTo);
				
				break;
				
			case COMMON:
				
				if (beforeCardWrapperAddition != null) {
					onIndex = toSection.getCardsWrappersAdditionsCommon().indexOf(beforeCardWrapperAddition);
					if (onIndex != -1) {
						toSection.getCardsWrappersAdditionsCommon().add(onIndex, cardWrapperAdditionTo);
					} else {
						toSection.getCardsWrappersAdditionsCommon().add(cardWrapperAdditionTo);
					}
				} else {
					toSection.getCardsWrappersAdditionsCommon().add(cardWrapperAdditionTo);
				}
				modelSectionRepository.save(toSection);
						
				break;
		}
		
		if (cardWrapperAdditionTo.getScope() != ModelCardWrapperScope.PRIVATE) {
			activityService.modelCardWrapperMoved(cardWrapperAdditionTo, fromSection, toSection, appUserRepository.findByC1Id(requestedById));
		}
		
		return new PostResult("success", "card wrapper moved", cardWrapperAdditionTo.getId().toString());
		
	}
	
	@Transactional
	public GetResult<ModelCardWrapperDto> getCardWrapperAddition(UUID cardWrapperId, UUID requestByUserId, UUID inSectionId) {
		
		ModelCardWrapperDto cardWrapperDto = null;
		
		if (!modelCardWrapperAdditionRepository.cardWrapperuserHaveAccess(cardWrapperId, requestByUserId)) {
			return new GetResult<ModelCardWrapperDto>("error", "dont have access to this card", null);
		}
		
		/* check if this is a card addition */
		ModelCardWrapperAddition dummy = new ModelCardWrapperAddition();
		dummy.setCardWrapper(modelCardWrapperRepository.findById(cardWrapperId));
		
		cardWrapperDto = getCardWrapperDtoWithMetadata(dummy, requestByUserId);
		
		return new GetResult<ModelCardWrapperDto>("success", "card retrieved", cardWrapperDto);
	}
	
	@Transactional
	public UUID getModelCardWrapperCreatorId(UUID cardWrapperId) {
		return modelCardWrapperRepository.findById(cardWrapperId).getCreator().getC1Id();
	}
	
	@Transactional
	public Initiative getCardWrapperInitiative(UUID cardWrapperId) {
		return modelCardWrapperRepository.findById(cardWrapperId).getInitiative();
	}
	
	@Transactional
	public GetResult<Page<ModelCardWrapperDto>> searchCardWrapper(
			UUID sectionId, 
			String query, 
			Integer page, 
			Integer pageSize, 
			String sortByIn, 
			Integer levels, 
			UUID requestById, 
			Boolean inInitiativeEcosystem) {
		
		PageRequest pageRequest = null;
		Page<ModelCardWrapperAddition> enititiesPage = null;
		
		switch (sortByIn) {
			case "CREATION_DATE_DESC":
				pageRequest = new PageRequest(page, pageSize, new Sort(Sort.Direction.DESC, "crdWrp.creationDate"));
				break;
				
			case "EDITION_DATE_DESC":
				pageRequest = new PageRequest(page, pageSize, new Sort(Sort.Direction.DESC, "crdWrp.lastEdited"));
				break;
				
			case "CREATOR":
				pageRequest = new PageRequest(page, pageSize, new Sort(Sort.Direction.ASC, "crdWrp.creator.profile.nickname"));
				break;
			
			default:
				pageRequest = new PageRequest(page, pageSize);
				break;
		}
		
		if (!inInitiativeEcosystem) {
			List<UUID> allSectionIds = new ArrayList<UUID>();
			
			allSectionIds.addAll(getAllSubsectionsIds(sectionId, levels - 1));

			enititiesPage = modelCardWrapperAdditionRepository.searchInSectionsByQuery(allSectionIds, "%"+query.toLowerCase()+"%", requestById, pageRequest);
			
		} else {
			
			ModelSection section = modelSectionRepository.findById(sectionId);
			List<UUID> initiativeEcosystemIds = initiativeService.findAllInitiativeEcosystemIds(section.getInitiative().getId());
			
			enititiesPage = modelCardWrapperAdditionRepository.searchInInitiativesByQuery(initiativeEcosystemIds, "%"+query.toLowerCase()+"%", requestById, pageRequest);
		}
		
		List<ModelCardWrapperDto> cardsDtos = new ArrayList<ModelCardWrapperDto>();
		
		for(ModelCardWrapperAddition cardWrapperAddition : enititiesPage.getContent()) {
			cardsDtos.add(getCardWrapperDtoWithMetadata(cardWrapperAddition, requestById));
		}
		
		Page<ModelCardWrapperDto> dtosPage = new PageImpl<ModelCardWrapperDto>(cardsDtos, pageRequest, enititiesPage.getNumberOfElements());
		
		return new GetResult<Page<ModelCardWrapperDto>>("success", "cards returned", dtosPage);
	}
		
	@Transactional
	public PostResult deleteCardWrapper (UUID cardWrapperId, UUID creatorId) {
		
		List<ModelCardWrapperAddition> allAdditions = modelCardWrapperAdditionRepository.findOfCardWrapper(cardWrapperId);
		
		/* remove from all sections */
		for (ModelCardWrapperAddition cardWrapperAddition : allAdditions) {
			
			cardWrapperAddition.setStatus(Status.DELETED);
			
			/* remove from list of common cards */
			if(cardWrapperAddition.getScope() == ModelCardWrapperScope.COMMON) {
				ModelSection section = cardWrapperAddition.getSection(); 
				section.getCardsWrappersAdditionsCommon().remove(cardWrapperAddition);
				modelSectionRepository.save(section);
			}

			/* create notification */
			if (cardWrapperAddition.getScope() != ModelCardWrapperScope.PRIVATE) {
				activityService.modelCardWrapperRemoved(cardWrapperAddition, appUserRepository.findByC1Id(creatorId));
			}
		}
		
		return new PostResult("success", "card wrapper removed from all sections and deleted", cardWrapperId.toString());
	}
	
	@Transactional
	public GetResult<ModelSectionLinkedDto> getSectionParentGenealogy(UUID sectionId, Integer levels) {
		ModelSectionLinkedDto linkedDtos = getModelSectionDtosFromNodes(getSectionNode(sectionId, true, false, levels));
		return new GetResult<ModelSectionLinkedDto>("success", "parents retrieved", linkedDtos);
	}
	
	/* Recursive method that get the sections data and convert it to a DTO structure while keeping the
	 * links between sections as derived from the input Node */
	@Transactional
	public ModelSectionLinkedDto getModelSectionDtosFromNodes(GraphNode node) {
		
		ModelSectionLinkedDto sectionLinked = new ModelSectionLinkedDto();
		
		sectionLinked.setSection(modelSectionRepository.findById(node.elementId).toDtoLight());
		
		for (GraphNode parentNode : node.getParents()) {
			sectionLinked.getParents().add(getModelSectionDtosFromNodes(parentNode));
		}
		
		for (GraphNode childrenNode : node.getChildren()) {
			sectionLinked.getChildren().add(getModelSectionDtosFromNodes(childrenNode));
		}
		
		return sectionLinked;
	}
	
	
	@Transactional
	public GraphNode getSectionNode(UUID sectionId, Boolean addParents, Boolean addChildren, Integer levels) {
		/* levels = null means as many levels as available */
		return getSectionNodeRec(sectionId, addParents, addChildren, levels, new ArrayList<UUID>());
	}
	
	private GraphNode getSectionNodeRec(UUID sectionId, Boolean addParents, Boolean addChildren, Integer levels, List<UUID> readIds) {
		
		if (!modelSectionRepository.sectionExists(sectionId)) {
			return null;
		}
		
		GraphNode node = new GraphNode();
		node.setElementId(sectionId);
		readIds.add(sectionId);
		
		/* this node is one level */
		levels = levels != null ? levels - 1 : null;
		
		/* look for neighbors if input level was 2 or larger */
		Boolean this_neighbors = false;
		if (levels != null) {
			if (levels >= 1) {
				this_neighbors = true; 
			}
		} else {
			this_neighbors = true;
		}
		
		/* children are one more level */
		if (this_neighbors) {
			
			if (addParents) {
				List<UUID> parents = modelSectionRepository.findParentSectionsIds(sectionId);
				
				for (UUID inSectionId : parents) {
					if (!readIds.contains(inSectionId)) {
						GraphNode parentNode = null;
						parentNode = getSectionNodeRec(inSectionId, addParents, false, levels, readIds);	
						node.getParents().add(parentNode);
						
					} else {
						/* if section already added, add it but don't keep looking recursively */
						GraphNode repeatedNode = new GraphNode();
						repeatedNode.setElementId(inSectionId);
						node.getParents().add(repeatedNode);
					}
				}
			}
			
			if (addChildren) {
				List<UUID> children = modelSectionRepository.findSubsectionsIds(sectionId);
				
				for (UUID subSectionId : children) {
					if (!readIds.contains(subSectionId)) {
						GraphNode childrenNode = null;
						childrenNode = getSectionNodeRec(subSectionId, false, addChildren, levels, readIds);	
						node.getChildren().add(childrenNode);
						
					} else {
						/* if section already added, add it but don't keep looking recursively */
						GraphNode repeatedNode = new GraphNode();
						repeatedNode.setElementId(subSectionId);
						node.getChildren().add(repeatedNode);
					}
				}
			}
		}
		
		return node;
	}
	
	@Transactional
	public List<UUID> getAllSubsectionsIds (UUID sectionId, Integer level) {
		GraphNode subsection = getSectionNode(sectionId, false, true, level);
		return subsection.toList(false, true);
	}
	
	@Transactional
	public GetResult<Page<ActivityDto>> getActivityResultUnderSection (UUID sectionId, PageRequest page, Boolean onlyMessages, Integer level) {
		
		Page<Activity> activities = getActivityUnderSection(sectionId, page, onlyMessages, level);
		
		List<ActivityDto> activityDtos = new ArrayList<ActivityDto>();
		
		for(Activity activity : activities.getContent()) {
			activityDtos.add(activity.toDto());
		}
		
		Page<ActivityDto> dtosPage = new PageImpl<ActivityDto>(activityDtos, page, activities.getNumberOfElements());
		
		return new GetResult<Page<ActivityDto>>("succes", "actvity returned", dtosPage);
	}
	
	@Transactional
	public Page<Activity> getActivityUnderSection (UUID sectionId, PageRequest page, Boolean onlyMessages, Integer level) {
		
		List<UUID> allSectionIds = getAllSubsectionsIds(sectionId, level);
		List<UUID> cardsIds = allSectionIds.size() > 0 ? modelCardWrapperAdditionRepository.findAllCardWrapperIdsOfSections(allSectionIds) : new ArrayList<UUID>();
		
		if (cardsIds.size() == 0) {
			cardsIds.add(UUID.randomUUID());
		}
		
		Page<Activity> activities = null;
		
		if (!onlyMessages) {
			activities = activityRepository.findOfSectionsOrCards(allSectionIds, cardsIds, page);	
		} else {
			activities = activityRepository.findOfSectionsOrCardsAndType(allSectionIds, cardsIds, ActivityType.MESSAGE_POSTED, page);	
		}
			
		return activities;
	}
	
	@Transactional
	public GetResult<Long> countMessagesUnderCard (UUID cardWrapperId, Boolean onlyMessages) {
		Page<Activity> messages = getActivityUnderCard(cardWrapperId, new PageRequest(1, 1), true);
		return new GetResult<Long>("success", "activity counted", messages.getTotalElements());
	}
	
	@Transactional
	public Page<Message> getMessagesUnderCard (UUID cardWrapperId, PageRequest page) {
		Page<Message> activities = null;
		
		activities = messageRepository.findOfCard(cardWrapperId, page);
		
		return activities;
	}
	
	@Transactional
	public Page<Activity> getActivityUnderCard (UUID cardWrapperId, PageRequest page, Boolean onlyMessages) {
		Page<Activity> activities = null;
		List<UUID> dum = new ArrayList<UUID>();
		List<UUID> cardIds = new ArrayList<UUID>();
		
		dum.add(UUID.randomUUID());
		cardIds.add(cardWrapperId);
		
		if (!onlyMessages) {
			activities = activityRepository.findOfSectionsOrCards(dum, cardIds, page);
		} else {
			activities = activityRepository.findOfSectionsOrCardsAndType(dum, cardIds, ActivityType.MESSAGE_POSTED, page);
		}
		return activities;
	}
	
	@Transactional
	public GetResult<Page<ActivityDto>> getActivityResultUnderCard (UUID cardWrapperId, PageRequest page, Boolean onlyMessages) {
		
		Page<Activity> activities = getActivityUnderCard(cardWrapperId, page, onlyMessages);
	
		List<ActivityDto> activityDtos = new ArrayList<ActivityDto>();
		
		for(Activity activity : activities.getContent()) {
			activityDtos.add(activity.toDto());
		}
		
		Page<ActivityDto> dtosPage = new PageImpl<ActivityDto>(activityDtos, page, activities.getNumberOfElements());
		
		return new GetResult<Page<ActivityDto>>("succes", "actvity returned", dtosPage);
		
	}
	
	@Transactional
	public PostResult setLikeToCard (UUID cardWrapperId, UUID authorId, boolean likeStatus) {
		ModelCardWrapper card = modelCardWrapperRepository.findById(cardWrapperId);
		AppUser author = appUserRepository.findByC1Id(authorId);
		
		CardLike like = cardLikeRepository.findByCardWrapperIdAndAuthor_c1Id(cardWrapperId, authorId);
		
		/* add the like*/
		if (likeStatus == true) {
			if (like == null) {
				like = new CardLike();
				like.setAuthor(author);
				like.setCardWrapper(card);
				cardLikeRepository.save(like);
			} else {
				/* nothing to do, the like is already registered */
			}	
		} else {
			if (like != null) {
				cardLikeRepository.delete(like);
			} else {
				/* nothing to do, the like is already absent */
			}
		}
		
		return new PostResult("success", "like status changed", null);
	}
	
	@Transactional
	public GetResult<Integer> countCardLikes (UUID cardWrapperId) {
		return new GetResult<Integer>("success", "cards counted", cardLikeRepository.countOfCard(cardWrapperId));
	}
	
}
