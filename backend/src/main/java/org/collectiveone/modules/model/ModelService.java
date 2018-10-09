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
import org.collectiveone.modules.files.FileService;
import org.collectiveone.modules.files.FileStored;
import org.collectiveone.modules.files.FileStoredRepositoryIf;
import org.collectiveone.modules.governance.CardLike;
import org.collectiveone.modules.governance.CardLikeRepositoryIf;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.collectiveone.modules.initiatives.repositories.InitiativeRepositoryIf;
import org.collectiveone.modules.model.dto.CardWrappersHolderDto;
import org.collectiveone.modules.model.dto.ElementConsentPositionDto;
import org.collectiveone.modules.model.dto.ModelCardDto;
import org.collectiveone.modules.model.dto.ModelCardWrapperDto;
import org.collectiveone.modules.model.dto.ModelSectionDto;
import org.collectiveone.modules.model.dto.ModelSectionLinkedDto;
import org.collectiveone.modules.model.dto.SubsectionsHolderDto;
import org.collectiveone.modules.model.enums.ElementConsentPositionColor;
import org.collectiveone.modules.model.enums.ElementGovernanceType;
import org.collectiveone.modules.model.enums.SimpleConsentState;
import org.collectiveone.modules.model.enums.Status;
import org.collectiveone.modules.model.exceptions.WrongLinkOfElement;
import org.collectiveone.modules.model.repositories.ConsentPositionRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelCardRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelCardWrapperAdditionRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelCardWrapperRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelSectionRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelSubsectionRepositoryIf;
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
	private CardLikeRepositoryIf cardLikeRepository;
	
	@Autowired
	private ModelCardWrapperAdditionRepositoryIf modelCardWrapperAdditionRepository;
	
	@Autowired
	private ModelSubsectionRepositoryIf modelSubsectionRepository;
	
	@Autowired
	private ConsentPositionRepositoryIf consentPositionRepository;
	
	
	@Transactional(rollbackOn = Exception.class)
	public GetResult<ModelSectionDto> getModel(UUID initiativeId, Integer level, UUID requestById, Boolean onlySections) {
		Initiative initiative = initiativeRepository.findById(initiativeId);
		if (initiative == null) return new GetResult<ModelSectionDto>("error", "initiative not found", null);
		
		ModelSectionDto sectionDto = initiative.getTopModelSubsection().getSection().toDto();
		if (level > 0) {
			sectionDto = addSectionSubElements(sectionDto, initiative.getTopModelSubsection().getSection().getId(), level - 1, requestById, onlySections);	
		}
			
		return new GetResult<ModelSectionDto> ("success", "model found", sectionDto);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Initiative getSectionInitiative (UUID sectionId) {
		return modelSectionRepository.findById(sectionId).getInitiative();
	}
	
	@Transactional(rollbackOn = Exception.class)
	public PostResult createSection(
			ModelSectionDto sectionDto, 
			UUID parentSectionId, 
			UUID creatorId, 
			UUID onSectionId,
			Boolean isBefore) {
		
		ModelSection section = sectionDto.toEntity(null, sectionDto);
		
		section = modelSectionRepository.save(section);
		
		ModelSection parent = modelSectionRepository.findById(parentSectionId);
		if (parent == null) return new PostResult("error", "parent section not found", "");
		
		section.setInitiative(parent.getInitiative());
		
		ModelSubsection onSubsection = getOnSubsectionFromId(
				onSectionId, 
				parentSectionId, 
				sectionDto.getNewScope(), 
				creatorId);
		
		ModelSubsection subsection = new ModelSubsection();
		
		subsection.setAdder(appUserRepository.findByC1Id(creatorId));
		subsection.setSection(section);
		subsection.setParentSection(parent);
		subsection.setScope(sectionDto.getNewScope());
		subsection.setStatus(Status.VALID);
		
		subsection = modelSubsectionRepository.save(subsection);
		
		if (onSubsection != null) {
			String result = linkOrderedElement(subsection, onSubsection, isBefore); 
			if (result != "success") {
				return new PostResult("error", result, section.getId().toString());
			}	
		}
		
		if (subsection.getScope() != ModelScope.PRIVATE) {
			activityService.modelSectionCreatedOnSection(subsection, parent, appUserRepository.findByC1Id(creatorId));
		}
		
		modelSectionRepository.save(parent);
		
		return new PostResult("success", "section created", section.getId().toString());
	}
	
	@Transactional
	public GetResult<ModelSectionDto> getSection(UUID sectionId, UUID inSectionId, Integer level, UUID requestByUserId, Boolean onlySections) {
		return new GetResult<ModelSectionDto>("success", "section retrieved", getSectionDto(sectionId, inSectionId, level, requestByUserId, onlySections));
	}
	
	@Transactional
	public ModelSectionDto getSectionDto(UUID sectionId, UUID inSectionId, Integer level, UUID requestByUserId, Boolean onlySections) {
		
		if (!modelSubsectionRepository.subsectionUserHaveAccess(sectionId, requestByUserId)) {
			return null;
		}
		
		ModelSubsection subsection = new ModelSubsection();
		/* check if this is a card addition */
		if (inSectionId != null) {
			subsection = 
					modelSubsectionRepository.findByParentSectionAndSectionVisibleToUser(inSectionId, sectionId, requestByUserId);	
		} else {
			subsection = new ModelSubsection();
			subsection.setSection(modelSectionRepository.findById(sectionId));
		}
		
		ModelSectionDto sectionDto = getSubsectionDto(subsection);
		
		List<ModelSubsection> inSubsections = modelSubsectionRepository.findOfSection(sectionId);
		
		for (ModelSubsection inSubsection : inSubsections) {
			if (inSubsection.getParentSection() != null) {
				sectionDto.getInSections().add(inSubsection.getParentSection().toDto());
			}
		}
		
		if(level > 0) {
			sectionDto = addSectionSubElements(sectionDto, sectionId, level - 1, requestByUserId, onlySections);
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
			sectionDtos.add(getSectionDto(section.getId(), null, 0, requestByUserId, true));
		}
		
		Page<ModelSectionDto> dtosPage = new PageImpl<ModelSectionDto>(sectionDtos, page, enititiesPage.getNumberOfElements());
		
		return new GetResult<Page<ModelSectionDto>>("succes", "sections returned", dtosPage);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public PostResult editSection (UUID sectionId, UUID parentSectionId, ModelSectionDto sectionDto, UUID creatorId) {
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		
		section = sectionDto.toEntity(section, sectionDto);
		
		section = modelSectionRepository.save(section);
		
		ModelSubsection subsection = null;
		if (parentSectionId != null) {
			subsection = modelSubsectionRepository.findByParentSectionAndSectionVisibleToUser(parentSectionId, sectionId, creatorId);
			subsection.setScope(sectionDto.getNewScope());
		} else {
			subsection = new ModelSubsection();
			subsection.setSection(section);
		}
		
		subsection = modelSubsectionRepository.save(subsection);
		
		activityService.modelSectionEdited(subsection, appUserRepository.findByC1Id(creatorId));
		
		return new PostResult("success", "section edited", section.getId().toString());
	}
	
	@Transactional
	public PostResult removeSubsectionFromSection(UUID sectionId, UUID subsectionId, UUID requestedById) {
		
		ModelSubsection subsection = 
				modelSubsectionRepository.findByParentSectionAndSectionVisibleToUser(sectionId, subsectionId, requestedById);
		
		removeSubsectionKeepOrder(subsection, requestedById);
		
		if (subsection.getScope() != ModelScope.PRIVATE) {
			activityService.modelSubsectionRemoved(subsection, appUserRepository.findByC1Id(requestedById));
		}
		
		return new PostResult("success", "card added to section", subsection.getId().toString());
	}
	
	@Transactional(rollbackOn = Exception.class)
	public PostResult moveSubsection(
			UUID fromSectionId, 
			UUID subSectionId, 
			UUID toSectionId, 
			UUID onSubsectionId,
			Boolean isBefore,
			UUID requestedById) {
		
		/* move a subsection to another section or subsection or, as top section, to a view */
		if (subSectionId.equals(toSectionId)) {
			return new PostResult("warning", "cannot move on itself", null);
		}
		
		/* check there is not a visible subsection already in this section */
		if (!fromSectionId.equals(toSectionId)) {
			ModelSubsection existingSubsection = 
					modelSubsectionRepository.findByParentSectionAndSectionVisibleToUser(
							toSectionId, subSectionId, requestedById);
					
			if (existingSubsection != null) {
				return new PostResult("error", "subsection already in this section", null);
			}	
		}
		
		ModelSection toSection = modelSectionRepository.findById(toSectionId);
		
		ModelSubsection subsectionFrom = 
				modelSubsectionRepository.findByParentSectionAndSectionVisibleToUser(
						fromSectionId, subSectionId, requestedById);
		
		ModelSubsection subsectionTo = null;
		
		if (!fromSectionId.equals(toSectionId)) {
			/* remove card from original section */
			removeSubsectionKeepOrder(subsectionFrom, requestedById);
			
			/* check if this card was already deleted by this adder in this section */
			List<ModelSubsection> subsectionsDeleted = 
					modelSubsectionRepository.findDeletedByParentSectionAndSectionAndAdder(
							toSectionId, subSectionId, requestedById);
			
			if (subsectionsDeleted.size() == 0) {
				/* check if this card was already deleted from this section by this user, in which case, just update it */
				subsectionTo = new ModelSubsection();
				subsectionTo = modelSubsectionRepository.save(subsectionTo);
			} else {
				subsectionTo = subsectionsDeleted.get(0);
			}
			
			subsectionTo.setAdder(appUserRepository.findByC1Id(requestedById));
			subsectionTo.setParentSection(toSection);
			subsectionTo.setSection(subsectionFrom.getSection());
			subsectionTo.setScope(subsectionFrom.getScope());
			subsectionTo.setStatus(Status.VALID);
			
		} else {
			/* moving within the same section */
			unlinkOrderedElement(subsectionFrom);
			subsectionTo = subsectionFrom;
		}
		
		ModelSubsection onSubsection = getOnSubsectionFromId(
				onSubsectionId, 
				toSectionId,
				subsectionFrom.getScope(), 
				requestedById);
		
		/* save after finding the onCardWrapperAddition */
		modelSubsectionRepository.save(subsectionTo);
		
		if (onSubsection != null) {
			String result = linkOrderedElement(subsectionTo, onSubsection, isBefore); 
			if (result != "success") {
				return new PostResult("error", result, null);
			}
		}
		
		if (subsectionTo.getScope() != ModelScope.PRIVATE) {
			ModelSection fromSection = modelSectionRepository.findById(fromSectionId);
			activityService.modelSubsectionMoved(subsectionTo, fromSection, toSection, appUserRepository.findByC1Id(requestedById));
		}
		
		return new PostResult("success", "card wrapper moved", subsectionTo.getId().toString());
	}
	
	@Transactional(rollbackOn = Exception.class)
	public PostResult addSubsectionToSection (
			UUID sectionId, 
			UUID parentSectionId, 
			UUID onSubsectionId, 
			Boolean isBefore,
			UUID requestByUserId ,
			ModelScope scope) {
		
		
		ModelSubsection existingSubsection = 
				modelSubsectionRepository.findByParentSectionAndSectionVisibleToUser(
						parentSectionId, sectionId, requestByUserId);
		
		if (existingSubsection != null) {
			return new PostResult("error", "section already in this section", null);
		}
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		ModelSection parentSection = modelSectionRepository.findById(parentSectionId);
		
		/* check if this card was already deleted by this adder in this section */
		ModelSubsection subsection = null;
		
		List<ModelSubsection> subsectionsDeleted = 
				modelSubsectionRepository.findDeletedByParentSectionAndSectionAndAdder(
						parentSectionId, sectionId, requestByUserId);
		
		if (subsectionsDeleted.size() == 0) {
			/* check if this card was already deleted from this section by this user, in which case, just update it */
			subsection = new ModelSubsection();
			subsection = modelSubsectionRepository.save(subsection);
		} else {
			subsection = subsectionsDeleted.get(0);
		} 
		
		subsection.setAdder(appUserRepository.findByC1Id(requestByUserId));
		subsection.setSection(section);
		subsection.setParentSection(parentSection);
		subsection.setScope(scope);
		subsection.setStatus(Status.VALID);
		
		subsection = modelSubsectionRepository.save(subsection);
		
		ModelSubsection onSubsection = getOnSubsectionFromId(
				onSubsectionId, 
				parentSectionId, 
				scope, 
				requestByUserId);
				
		if (onSubsection != null) {
			String result = linkOrderedElement(subsection, onSubsection, isBefore); 
			if (result != "success") {
				return new PostResult("error", result, section.getId().toString());
			}
		}
				
		if (scope != ModelScope.PRIVATE) {
			activityService.modelSubsectionAdded(subsection, appUserRepository.findByC1Id(requestByUserId));
		}
		
		return new PostResult("success", "card added to section", section.getId().toString());
		
	}
	
	private void updateScopeAndReorder(OrderedElement element, ModelScope newScope, OrderedElement lastElement) {
		switch (element.getScope()) {
			case COMMON:
				switch (newScope) {
					case COMMON:
						return;
					
					case PRIVATE:
					case SHARED:
						/* a common card becomes non-common
						 * find the element after which the updated element should be placed */
						
						OrderedElement onElement = element.getAfterElement();
						if (onElement == null) {
							onElement = element.getBeforeElement();
						}
						
						unlinkOrderedElement(element);
						
						if (onElement != null) {
							linkOrderedElement(element, onElement, false);
						} else {
							linkOrderedElement(element, lastElement, false);
						}
						
						return;
				}
				break;
				
			case PRIVATE:
			case SHARED:
				switch (newScope) {
					case COMMON:
						/* a non-common card becomes common
						 * - update the non-common card that came after this card
						 * - link on the card this card came after */
						
						OrderedElement onElement = element.getAfterElement();
						unlinkOrderedElement(element);
						
						if (onElement != null) {
							
							while (onElement.getScope() == ModelScope.COMMON) {
								onElement = onElement.getAfterElement();
								if (onElement == null) break;
							}
							
							if (onElement != null) {
								linkOrderedElement(element, onElement, false);
							} else {
								linkOrderedElement(element, lastElement, false);
							}
						}
						
						return;
					
					case PRIVATE:
					case SHARED:
						return;
				}
				break;
		
		}
	}
	
	private String unlinkOrderedElement(
			OrderedElement element) {

	    /* update neighbor cards links */
		OrderedElement leftElement = element.getAfterElement();
		OrderedElement rightElement = element.getBeforeElement();
		
		if (leftElement != null) {
			/* common cards are linked only to other common cards */
			if (((leftElement.getScope() == ModelScope.COMMON) && (element.getScope() == ModelScope.COMMON)) ||
				(leftElement.getScope() != ModelScope.COMMON) && (element.getScope() != ModelScope.COMMON)) {
			
				leftElement.setBeforeElement(rightElement);
			}
		}
		if (rightElement != null) {
			if (((rightElement.getScope() == ModelScope.COMMON) && (element.getScope() == ModelScope.COMMON)) ||
					(rightElement.getScope() != ModelScope.COMMON) && (element.getScope() != ModelScope.COMMON)) {
				
				rightElement.setAfterElement(leftElement);
			}
		}
		
		return "success";
	}
	
	private String removeSubsectionKeepOrder(
			ModelSubsection subsection,
			UUID requestByUserId) {
		
		String res = unlinkOrderedElement(subsection);
		
		if (res == "success") {
			subsection.setStatus(Status.DELETED);
			modelSubsectionRepository.save(subsection);
			return "success";	
		} else {
			return "error";
		}
	}
	
	private String removeCardWrapperAdditionKeepOrder(
			ModelCardWrapperAddition cardWrapperAddition,
			UUID requestByUserId) {
		
		String res = unlinkOrderedElement(cardWrapperAddition);
		
		if (res == "success") {
			cardWrapperAddition.setStatus(Status.DELETED);
			modelCardWrapperAdditionRepository.save(cardWrapperAddition);
			return "success";	
		} else {
			return "error";
		}
	}
	
	
	/* Cards order uses a single-linked list (using afterCardWrapperAddition) with some constraints:
	 * - common cards must only go after other common cards.
	 * - shared or private cards can go after common cards, or after private or shared cards.
	 * - the first card is the one without a reference. 
	 * - only one common card can be the first card.
	 * - only one private card can be the first private card.
	 * - only one own-shared card can be the first shared card.
	 * */
	private String linkOrderedElement(
			OrderedElement element, 
			OrderedElement onElement,
			Boolean isBefore) {
		
		/* reset any info */
		element.setAfterElement(null);
		element.setBeforeElement(null);
		
		OrderedElement leftElement = null;
		OrderedElement rightElement = null;
		
		/* protection logic to prevent wrong orders */
		switch (element.getScope()) {
			case COMMON:
				if (onElement.getScope() != ModelScope.COMMON) {
					return "error, cannot place a common element relative to a non-common element";
				}
				break;
				
			case PRIVATE:
			case SHARED:				
				break;
		}
		
		if (isBefore) {
			leftElement = onElement.getAfterElement();
			rightElement = onElement;
		} else {
			leftElement = onElement;
			rightElement = onElement.getBeforeElement();
		}
		
		/* all cards can be after a common or shared or public card */
		element.setAfterElement(leftElement);
		
		if (leftElement != null) {
			/* common cards are linked only to other common cards */
			if (((leftElement.getScope() == ModelScope.COMMON) && (element.getScope() == ModelScope.COMMON)) ||
				(leftElement.getScope() != ModelScope.COMMON) && (element.getScope() != ModelScope.COMMON)) {
				
				leftElement.setBeforeElement(element);
			}
		}
		
		if (rightElement != null) {
			if (((rightElement.getScope() == ModelScope.COMMON) && (element.getScope() == ModelScope.COMMON)) ||
					(rightElement.getScope() != ModelScope.COMMON) && (element.getScope() != ModelScope.COMMON)) {
				
				/* but only common cards are marked as being before a common card */
				element.setBeforeElement(rightElement);
				rightElement.setAfterElement(element);
			}
		} else {
			element.setBeforeElement(rightElement);
		}
		
		return "success";
	}
		
	@Transactional
	public PostResult addCardToSection (
			UUID sectionId, 
			UUID cardWrapperId, 
			UUID onCardWrapperId,
			Boolean isBefore,
			UUID requestByUserId,
			ModelScope scope) {
		
		ModelCardWrapperAddition existingCard = 
				modelCardWrapperAdditionRepository.findBySectionAndCardWrapperVisibleToUser(sectionId, cardWrapperId, requestByUserId);
		
		if (existingCard != null) {
			return new PostResult("error", "card already in this section", null);
		}
		
		ModelCardWrapper cardWrapper = modelCardWrapperRepository.findById(cardWrapperId);
		ModelSection section = modelSectionRepository.findById(sectionId);
		
		/* check if this card was already deleted by this adder in this section */
		ModelCardWrapperAddition cardWrapperAddition = null;
		
		List<ModelCardWrapperAddition> cardWrapperAdditionsDeleted = 
				modelCardWrapperAdditionRepository.findDeletedBySectionAndCardWrapperAndAdder(sectionId, cardWrapperId, requestByUserId);
		
		if (cardWrapperAdditionsDeleted.size() == 0) {
			/* check if this card was already deleted from this section by this user, in which case, just update it */
			cardWrapperAddition = new ModelCardWrapperAddition();
			cardWrapperAddition = modelCardWrapperAdditionRepository.save(cardWrapperAddition);
		} else {
			cardWrapperAddition = cardWrapperAdditionsDeleted.get(0);
		} 
		
		cardWrapperAddition.setAdder(appUserRepository.findByC1Id(requestByUserId));
		cardWrapperAddition.setSection(section);
		cardWrapperAddition.setCardWrapper(cardWrapper);
		cardWrapperAddition.setScope(scope);
		cardWrapperAddition.setStatus(Status.VALID);
		
		cardWrapperAddition = modelCardWrapperAdditionRepository.save(cardWrapperAddition);
		
		ModelCardWrapperAddition onCardWrapperAddition = getOnCardWrapperAdditionFromId(
				onCardWrapperId, 
				sectionId, 
				scope, 
				requestByUserId);
				
		if (onCardWrapperAddition != null) {
			String result = linkOrderedElement(cardWrapperAddition, onCardWrapperAddition, isBefore); 
			if (result != "success") {
				return new PostResult("error", result, section.getId().toString());
			}
		}
				
		if (scope != ModelScope.PRIVATE) {
			activityService.modelCardWrapperAdded(cardWrapperAddition, appUserRepository.findByC1Id(requestByUserId));
		}
		
		return new PostResult("success", "card added to section", section.getId().toString());
	}
	
	private ModelSubsection getOnSubsectionFromId(UUID onSectionId, UUID parentSectionId, ModelScope scope, UUID requestByUserId) {
		
		ModelSubsection onSubsection = null;
		
		if (onSectionId != null) {
			onSubsection = 
					modelSubsectionRepository.findByParentSectionAndSectionVisibleToUser(
							parentSectionId, 
							onSectionId, 
							requestByUserId);	
		} else {
			/* if no place is specified */
			/* try to place if after the last section with the same scope added by this user */
			List<ModelSubsection> lastSections 
				= modelSubsectionRepository.findLastByParentSectionAndAdderAndScope(
					parentSectionId, requestByUserId, scope);
			
			if (lastSections.size() > 0) {
				onSubsection = lastSections.get(0);
			}
			
			/* if nothing found, try to place it after the last common card */
			if (onSubsection == null) {
			
				List<ModelSubsection> lastCommonSections 
					= modelSubsectionRepository.findLastByParentSectionAndScope(
						parentSectionId, ModelScope.COMMON);
				
				if (lastCommonSections.size() > 0) {
					onSubsection = lastCommonSections.get(0);
				}
			}
		}
		
		return onSubsection;
	} 
	
	private ModelCardWrapperAddition getOnCardWrapperAdditionFromId(
			UUID onCardWrapperId, UUID sectionId, ModelScope scope, UUID requestByUserId) {
		
		ModelCardWrapperAddition onCardWrapperAddition = null;
		
		if (onCardWrapperId != null) {
			onCardWrapperAddition = 
					modelCardWrapperAdditionRepository.findBySectionAndCardWrapperVisibleToUser(
							sectionId, 
							onCardWrapperId, 
							requestByUserId);	
		} else {
			/* if no place is specified */
			onCardWrapperAddition = getLastCardWrapperAdditionInSection(sectionId, onCardWrapperId, scope);
		}
		
		return onCardWrapperAddition;
	} 
	
	private ModelCardWrapperAddition getLastCardWrapperAdditionInSection (UUID sectionId, UUID requestByUserId, ModelScope scope) {
		ModelCardWrapperAddition onCardWrapperAddition = null;
		
		/* try to place if after the last card with the same scope added by this user */
		List<ModelCardWrapperAddition> lastCards = modelCardWrapperAdditionRepository.findLastBySectionAndAdderAndScope(
				sectionId, requestByUserId, scope);
		
		if (lastCards.size() > 0) {
			onCardWrapperAddition = lastCards.get(0);
		}
		
		/* if nothing found, try to place it after the last common card */
		if (onCardWrapperAddition == null) {
		
			List<ModelCardWrapperAddition> lastCommonCards = modelCardWrapperAdditionRepository.findLastBySectionAndScope(
					sectionId, ModelScope.COMMON);
			
			if (lastCommonCards.size() > 0) {
				onCardWrapperAddition = lastCommonCards.get(0);
			}
		}
		
		return onCardWrapperAddition;
	}
	
	@Transactional
	public PostResult removeCardFromSection (UUID sectionId, UUID cardWrapperId, UUID requestedById) {
		
		ModelCardWrapperAddition cardWrapperAddition = 
				modelCardWrapperAdditionRepository.findBySectionAndCardWrapperVisibleToUser(sectionId, cardWrapperId, requestedById);
		
		removeCardWrapperAdditionKeepOrder(cardWrapperAddition, requestedById);
		
		if (cardWrapperAddition.getScope() != ModelScope.PRIVATE) {
			activityService.modelCardWrapperRemoved(cardWrapperAddition, appUserRepository.findByC1Id(requestedById));
		}
		
		return new PostResult("success", "card added to section", cardWrapperAddition.getId().toString());
	}
	
	@Transactional
	private ModelSectionDto getSubsectionDto(ModelSubsection subsection) {
		ModelSectionDto sectionDto = subsection.getSection().toDto();
		
		sectionDto.setScope(subsection.getScope());
		
		if (subsection.getBeforeElement() != null) {
			sectionDto.setBeforeElementId(subsection.getBeforeSubsection().getSection().getId().toString());
		}
		
		if (subsection.getAfterElement() != null) {
			sectionDto.setAfterElementId(subsection.getAfterSubsection().getSection().getId().toString());
		}
		
		List<ModelSubsection> inSubsections = modelSubsectionRepository.findOfSection(subsection.getSection().getId());
		
		for (ModelSubsection inSubsection : inSubsections) {
			if (inSubsection.getParentSection() != null) {
				sectionDto.getInSections().add(inSubsection.getParentSection().toDto());
			}
		}
		
		return sectionDto;
	}
	
	@Transactional
	private SubsectionsHolderDto getSectionSubsectionsDtos(UUID sectionId, Integer level, UUID requestByUserId, Boolean onlySections) {
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		SubsectionsHolderDto subsectionsHolder = new SubsectionsHolderDto();
		
		List<ModelSubsection> modelSubsectionsCommon = 
				modelSubsectionRepository.findInParentSectionWithScope(section.getId(), ModelScope.COMMON);
		
		for (ModelSubsection subsection : modelSubsectionsCommon) {
			subsectionsHolder.getSubsectionsCommon().add(
					addSectionSubElements(getSubsectionDto(subsection), subsection.getSection().getId(), level - 1, requestByUserId, onlySections));
		}
		
		List<ModelSubsection> modelSubsectionsPrivate = 
				modelSubsectionRepository.findOfUserInParentSection(requestByUserId, section.getId(), ModelScope.PRIVATE);
		
		for (ModelSubsection subsection : modelSubsectionsPrivate) {
			subsectionsHolder.getSubsectionsPrivate().add(
					addSectionSubElements(getSubsectionDto(subsection), subsection.getSection().getId(), level - 1, requestByUserId, onlySections));
		}
		
		/* if request user is in ecosystem add shared cards too */
		if(initiativeService.isMemberOfEcosystem(section.getInitiative().getId(), requestByUserId)) {
			
			List<ModelSubsection> modelSubsectionsShared = 
					modelSubsectionRepository.findInParentSectionWithScope(section.getId(), ModelScope.SHARED);
			
			for (ModelSubsection subsection : modelSubsectionsShared) {
				subsectionsHolder.getSubsectionsShared().add(
						addSectionSubElements(getSubsectionDto(subsection), subsection.getSection().getId(), level - 1, requestByUserId, onlySections));
			}
		}
		
		return subsectionsHolder;
	}
	
	@Transactional
	private CardWrappersHolderDto getSectionCardWrappersDtos(UUID sectionId, UUID requestByUserId) {
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		CardWrappersHolderDto cardWrappersHolder = new CardWrappersHolderDto();
		
		List<ModelCardWrapperAddition> modelCardWrappersCommon = 
				modelCardWrapperAdditionRepository.findCommonInSection(section.getId());
		
		for (ModelCardWrapperAddition cardWrapperAddition : modelCardWrappersCommon) {
			cardWrappersHolder.getCardsWrappersCommon().add(getCardWrapperDtoWithMetadata(cardWrapperAddition, requestByUserId));
		}
		
		List<ModelCardWrapperAddition> modelCardWrappersPrivate = 
				modelCardWrapperAdditionRepository.findOfUserInSection(requestByUserId, section.getId(), ModelScope.PRIVATE);
		
		for (ModelCardWrapperAddition cardWrapperAddition : modelCardWrappersPrivate) {
			ModelCardWrapperDto cardWrapperDto = getCardWrapperDtoWithMetadata(cardWrapperAddition, requestByUserId);
			cardWrappersHolder.getCardsWrappersPrivate().add(cardWrapperDto);
		}
		
		/* if request user is in ecosystem add shared cards too */
		if(initiativeService.isMemberOfEcosystem(section.getInitiative().getId(), requestByUserId)) {
			
			List<ModelCardWrapperAddition> modelCardWrappersShared = 
					modelCardWrapperAdditionRepository.findInSectionWithScope(section.getId(), ModelScope.SHARED);
			
			for (ModelCardWrapperAddition cardWrapperAddition : modelCardWrappersShared) {
				ModelCardWrapperDto cardWrapperDto = getCardWrapperDtoWithMetadata(cardWrapperAddition, requestByUserId);
				cardWrappersHolder.getCardsWrappersShared().add(cardWrapperDto);
			}
		}
		
		return cardWrappersHolder;
	}
	
	@Transactional
	public ModelSectionDto addSectionSubElements(ModelSectionDto sectionDto, UUID sectionId, Integer level, UUID requestByUserId, Boolean onlySections) {
		
		sectionDto.setSubElementsLoaded(true);
		
		if (!onlySections) {
			CardWrappersHolderDto cardWrappersHolder = getSectionCardWrappersDtos(sectionId, requestByUserId);
			
			sectionDto.setCardsWrappersCommon(cardWrappersHolder.getCardsWrappersCommon());
			sectionDto.setCardsWrappersPrivate(cardWrappersHolder.getCardsWrappersPrivate());
			sectionDto.setCardsWrappersShared(cardWrappersHolder.getCardsWrappersShared());
		}
				
		if (level > 0) {
			/* add the subsections with their sub-elements too */
			SubsectionsHolderDto subsectionsHolder = getSectionSubsectionsDtos(sectionId, level, requestByUserId, onlySections);
			
			sectionDto.setSubsectionsCommon(subsectionsHolder.getSubsectionsCommon());
			sectionDto.setSubsectionsPrivate(subsectionsHolder.getSubsectionsPrivate());
			sectionDto.setSubsectionsShared(subsectionsHolder.getSubsectionsShared());
		} 
		
		return sectionDto; 
	}
	
	private ModelCardWrapperDto getCardWrapperDtoWithMetadata(ModelCardWrapperAddition cardWrapperAddition, UUID requestByUserId) {
		
		/* Card wrapper additions are converted into card wrappers dtos for simplicity */
		ModelCardWrapperDto cardWrapperDto = cardWrapperAddition.getCardWrapper().toDto();
		
		/* data associated to the addition */
		cardWrapperDto.setAdditionId(cardWrapperAddition.getId() != null ? cardWrapperAddition.getId().toString() : null);
		cardWrapperDto.setScope(cardWrapperAddition.getScope());
		cardWrapperDto.setGovernanceType(cardWrapperAddition.getGovernanceType());
		cardWrapperDto.setSimpleConsentState(cardWrapperAddition.getSimpleConsentState());
		
		/* check if this card wrapper is private on that section only adder is able to see it */
		if (cardWrapperAddition.getScope() == ModelScope.PRIVATE) {
			if (!cardWrapperAddition.getAdder().getC1Id().equals(requestByUserId)) {
				return null;
			}
		}
		
		if (cardWrapperAddition.getBeforeElement() != null) {
			cardWrapperDto.setBeforeElementId(cardWrapperAddition.getBeforeCardWrapperAddition().getCardWrapper().getId().toString());
		}
		
		if (cardWrapperAddition.getAfterElement() != null) {
			cardWrapperDto.setAfterElementId(cardWrapperAddition.getAfterCardWrapperAddition().getCardWrapper().getId().toString());
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
			if (thisAddition.getScope() == ModelScope.PRIVATE) {
				if (!thisAddition.getAdder().getC1Id().equals(requestByUserId)) {
					skip = true;
				}
			}
			
			if (!skip) {
				cardWrapperDto.getInModelSections().add(thisAddition.toInModelSectionDto());	
			}
		}
		
		/* section governance */
		if (cardWrapperAddition.getGovernanceType() == ElementGovernanceType.SIMPLE_CONSENT) {
			ElementConsentPosition consentStatus = consentPositionRepository.findByElementIdAndAuthor_c1Id(cardWrapperAddition.getId(), requestByUserId);
			if (consentStatus != null) {
				cardWrapperDto.setOwnPosition(consentStatus.getPositionColor());
			} 
		}
		
		
		return cardWrapperDto;
	}
	
	@Transactional
	public PostResult deleteSection (UUID sectionId, UUID creatorId) {
		
		List<ModelSubsection> allSubsections = modelSubsectionRepository.findOfSection(sectionId);
		
		/* remove from all sections */
		for (ModelSubsection subsection : allSubsections) {
			
			subsection.setStatus(Status.DELETED);
			
			/* create notification */
			if (subsection.getScope() != ModelScope.PRIVATE) {
				activityService.modelSubsectionRemoved(subsection, appUserRepository.findByC1Id(creatorId));
			}
		}
		
		return new PostResult("success", "card wrapper removed from all sections and deleted", sectionId.toString());
	}
	
	@Transactional(rollbackOn = Exception.class)
	public PostResult createCardWrapper(
			ModelCardDto cardDto, 
			UUID sectionId, 
			UUID creatorId, 
			UUID onCardWrapperId, 
			Boolean isBefore) throws WrongLinkOfElement {
		
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
		
		/* must run before the cardwrappperaddition is created */
		ModelCardWrapperAddition onCardWrapperAddition = getOnCardWrapperAdditionFromId(
				onCardWrapperId, 
				sectionId, 
				cardDto.getNewScope(), 
				creatorId);
		
		/* create cardwrapper addition */
		ModelCardWrapperAddition cardWrapperAddition = new ModelCardWrapperAddition();
				
		cardWrapperAddition.setAdder(creator);
		cardWrapperAddition.setSection(section);
		cardWrapperAddition.setCardWrapper(cardWrapper);
		cardWrapperAddition.setScope(cardDto.getNewScope());
		
		cardWrapperAddition = modelCardWrapperAdditionRepository.save(cardWrapperAddition);
		
		if (onCardWrapperAddition != null) {
			String result = linkOrderedElement(cardWrapperAddition, onCardWrapperAddition, isBefore); 
			if (result != "success") {
				throw new WrongLinkOfElement(result);
			}	
		}
		
		
		if (cardWrapperAddition.getScope() != ModelScope.PRIVATE) {
			activityService.modelCardWrapperCreated(cardWrapperAddition, appUserRepository.findByC1Id(creatorId));
		}
		
		return new PostResult("success", "card created", cardWrapper.getId().toString());
	}
	
	@Transactional(rollbackOn = Exception.class)
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
		
		/* only author can edit the content. Adders can only move the card around */
		if (cardWrapperAddition.getScope() != ModelScope.COMMON) {
			if (!cardWrapperAddition.getCardWrapper().getCreator().getC1Id().equals(creatorId)) {
				return new PostResult("error", "access denied, only author can edit a card", "");
			}
		}
		
		if (cardWrapperAddition != null) {
			if (cardDto.getNewScope() != null) {
				if (cardDto.getNewScope() != cardWrapperAddition.getScope()) {
					
					ModelCardWrapperAddition lastCardWrapper = 
							getLastCardWrapperAdditionInSection (cardWrapperAddition.getSection().getId(), creatorId, cardWrapperAddition.getScope());
					
					updateScopeAndReorder(cardWrapperAddition, cardDto.getNewScope(), lastCardWrapper);
					cardWrapperAddition.setScope(cardDto.getNewScope());
					modelCardWrapperAdditionRepository.save(cardWrapperAddition);
				}	
			}
		}
		
		ModelCard originalCard = cardWrapper.getCard();
		
		if (!cardWrapper.getOldVersions().contains(originalCard)) {
			cardWrapper.getOldVersions().add(originalCard);	
		}
		
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
		
		modelCardWrapperRepository.save(cardWrapper);
		
		if (cardWrapperAddition.getScope() != ModelScope.PRIVATE) {
			activityService.modelCardWrapperEdited(cardWrapperAddition, appUserRepository.findByC1Id(creatorId));	
		}
		
		return new PostResult("success", "card edited", cardWrapper.getId().toString());
	}
	
	@Transactional(rollbackOn = Exception.class)
	public PostResult makeCardWrapperShared(UUID cardWrapperId, UUID onSectionId, UUID requestByUserId) {
		
		ModelCardWrapperAddition cardWrapperAddition = 
				modelCardWrapperAdditionRepository.findBySectionAndCardWrapperIdAndScope(onSectionId, cardWrapperId, ModelScope.PRIVATE);
				
		if (cardWrapperAddition == null) return new PostResult("error", "card wrapper not found", "");
		
		cardWrapperAddition.setScope(ModelScope.SHARED);
		modelCardWrapperAdditionRepository.save(cardWrapperAddition);
		
		activityService.modelCardWrapperMadeShared(cardWrapperAddition, appUserRepository.findByC1Id(requestByUserId));
		
		return new PostResult("success", "card edited", cardWrapperAddition.getId().toString());
	}
	
	@Transactional(rollbackOn = Exception.class)
	public PostResult makeCardWrapperCommon(UUID cardWrapperId, UUID onSectionId, UUID requestByUserId) {
		
		ModelCardWrapperAddition cardWrapperAddition = 
				modelCardWrapperAdditionRepository.findBySectionAndCardWrapperVisibleToUser(onSectionId, cardWrapperId, requestByUserId);
				
		if (cardWrapperAddition == null) return new PostResult("error", "card wrapper not found", "");
		
		cardWrapperAddition.setScope(ModelScope.COMMON);
		
		/* update the order */
		if (cardWrapperAddition.getAfterElement() != null) {
			linkOrderedElement(
					cardWrapperAddition, 
					cardWrapperAddition.getAfterElement(),
					false);	
		}
		
		activityService.modelCardWrapperMadeCommon(cardWrapperAddition, appUserRepository.findByC1Id(requestByUserId));
		
		return new PostResult("success", "card wrapper converted to common", cardWrapperAddition.getId().toString());
	}
	
	
	@Transactional(rollbackOn = Exception.class)
	public PostResult moveCardWrapper(
			UUID fromSectionId, 
			UUID cardWrapperId, 
			UUID toSectionId, 
			UUID onCardWrapperId,
			Boolean isBefore,
			UUID requestedById) {
		
		if (cardWrapperId.equals(onCardWrapperId)) {
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
		
		ModelSection toSection = modelSectionRepository.findById(toSectionId);
		
		ModelCardWrapperAddition cardWrapperAdditionFrom = 
				modelCardWrapperAdditionRepository.findBySectionAndCardWrapperVisibleToUser(fromSectionId, cardWrapperId, requestedById);
		
		ModelCardWrapperAddition cardWrapperAdditionTo = null;
		
		if (!fromSectionId.equals(toSectionId)) {
			/* remove card from original section */
			removeCardWrapperAdditionKeepOrder(cardWrapperAdditionFrom, requestedById);
			
			/* check if this card was already deleted by this adder in this section */
			List<ModelCardWrapperAddition> cardWrapperAdditionsDeleted = 
					modelCardWrapperAdditionRepository.findDeletedBySectionAndCardWrapperAndAdder(toSectionId, cardWrapperId, requestedById);
			
			if (cardWrapperAdditionsDeleted.size() == 0) {
				/* check if this card was already deleted from this section by this user, in which case, just update it */
				cardWrapperAdditionTo = new ModelCardWrapperAddition();
				cardWrapperAdditionTo = modelCardWrapperAdditionRepository.save(cardWrapperAdditionTo);
			} else {
				cardWrapperAdditionTo = cardWrapperAdditionsDeleted.get(0);
			}
			
			cardWrapperAdditionTo.setAdder(appUserRepository.findByC1Id(requestedById));
			cardWrapperAdditionTo.setSection(toSection);
			cardWrapperAdditionTo.setCardWrapper(cardWrapperAdditionFrom.getCardWrapper());
			cardWrapperAdditionTo.setScope(cardWrapperAdditionFrom.getScope());
			cardWrapperAdditionTo.setStatus(Status.VALID);
			
		} else {
			/* moving within the same section */
			unlinkOrderedElement(cardWrapperAdditionFrom);
			cardWrapperAdditionTo = cardWrapperAdditionFrom;
		}
		
		ModelCardWrapperAddition onCardWrapperAddition = getOnCardWrapperAdditionFromId(
				onCardWrapperId, 
				toSectionId,
				cardWrapperAdditionFrom.getScope(), 
				requestedById);
		
		/* save after finding the onCardWrapperAddition */
		modelCardWrapperAdditionRepository.save(cardWrapperAdditionTo);
		
		if (onCardWrapperAddition != null) {
			String result = linkOrderedElement(cardWrapperAdditionTo, onCardWrapperAddition, isBefore); 
			if (result != "success") {
				return new PostResult("error", result, null);
			}
		}
		
		if (cardWrapperAdditionTo.getScope() != ModelScope.PRIVATE) {
			ModelSection fromSection = modelSectionRepository.findById(fromSectionId);
			activityService.modelCardWrapperMoved(cardWrapperAdditionTo, fromSection, toSection, appUserRepository.findByC1Id(requestedById));
		}
		
		return new PostResult("success", "card wrapper moved", cardWrapperAdditionTo.getId().toString());
		
	}
	
	@Transactional
	public GetResult<ModelCardWrapperDto> getCardWrapper(UUID cardWrapperId, UUID requestByUserId) {
		
		ModelCardWrapperDto cardWrapperDto = null;
		
		if (!modelCardWrapperAdditionRepository.cardWrapperUserHaveAccess(cardWrapperId, requestByUserId)) {
			return new GetResult<ModelCardWrapperDto>("error", "dont have access to this card", null);
		}
		
		ModelCardWrapperAddition cardWrapperAddition = new ModelCardWrapperAddition();
		cardWrapperAddition.setCardWrapper(modelCardWrapperRepository.findById(cardWrapperId));
		cardWrapperAddition.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
		
		cardWrapperDto = getCardWrapperDtoWithMetadata(cardWrapperAddition, requestByUserId);
		
		return new GetResult<ModelCardWrapperDto>("success", "card retrieved", cardWrapperDto);
	}
	
	@Transactional
	public GetResult<ModelCardWrapperDto> getCardWrapperAddition(UUID cardWrapperId, UUID requestByUserId, UUID inSectionId) {
		
		ModelCardWrapperDto cardWrapperDto = null;
		
		if (!modelCardWrapperAdditionRepository.cardWrapperUserHaveAccess(cardWrapperId, requestByUserId)) {
			return new GetResult<ModelCardWrapperDto>("error", "dont have access to this card", null);
		}
		
		ModelCardWrapperAddition cardWrapperAddition = new ModelCardWrapperAddition();
		/* check if this is a card addition */
		if (inSectionId != null) {
			cardWrapperAddition = 
					modelCardWrapperAdditionRepository.findBySectionAndCardWrapperVisibleToUser(inSectionId, cardWrapperId, requestByUserId);	
		} else {
			cardWrapperAddition = new ModelCardWrapperAddition();
			cardWrapperAddition.setCardWrapper(modelCardWrapperRepository.findById(cardWrapperId));
		}
		
		cardWrapperDto = getCardWrapperDtoWithMetadata(cardWrapperAddition, requestByUserId);
		
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
	public Initiative getCardWrapperAdditionInitiative(UUID cardWrapperAdditionId) {
		return modelCardWrapperAdditionRepository.findById(cardWrapperAdditionId).getSection().getInitiative();
	}
	
	
	@Transactional
	public GetResult<Page<ModelCardWrapperDto>> searchCardWrapper(
			UUID sectionId, 
			String query, 
			Integer page, 
			Integer pageSize, 
			String sortByIn, 
			Integer levels, 
			UUID requestByUserId, 
			Boolean inInitiativeEcosystem) {
		
		PageRequest pageRequest = null;
		Page<ModelCardWrapper> enititiesPage = null;
		
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
			
			ModelSection section = modelSectionRepository.findById(sectionId);
			Boolean isMemberOfEcosystem = initiativeService.isMemberOfEcosystem(section.getInitiative().getId(), requestByUserId);
			
			allSectionIds.addAll(getAllSubsectionsIds(sectionId, levels - 1, requestByUserId, isMemberOfEcosystem));

			enititiesPage = modelCardWrapperAdditionRepository.searchInSectionsByQuery(allSectionIds, "%"+query.toLowerCase()+"%", requestByUserId, pageRequest);
			
		} else {
			
			ModelSection section = modelSectionRepository.findById(sectionId);
			List<UUID> initiativeEcosystemIds = initiativeService.findAllInitiativeEcosystemIds(section.getInitiative().getId());
			
			enititiesPage = modelCardWrapperAdditionRepository.searchInInitiativesByQuery(initiativeEcosystemIds, "%"+query.toLowerCase()+"%", requestByUserId, pageRequest);
		}
		
		List<ModelCardWrapperDto> cardsDtos = new ArrayList<ModelCardWrapperDto>();
		
		for(ModelCardWrapper cardWrapper: enititiesPage.getContent()) {
			ModelCardWrapperAddition cardWrapperAddition = new ModelCardWrapperAddition();
			cardWrapperAddition.setCardWrapper(cardWrapper);
			
			cardsDtos.add(getCardWrapperDtoWithMetadata(cardWrapperAddition, requestByUserId));
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
			
			/* create notification */
			if (cardWrapperAddition.getScope() != ModelScope.PRIVATE) {
				activityService.modelCardWrapperRemoved(cardWrapperAddition, appUserRepository.findByC1Id(creatorId));
			}
		}
		
		return new PostResult("success", "card wrapper removed from all sections and deleted", cardWrapperId.toString());
	}
	
	@Transactional
	public GetResult<ModelSectionLinkedDto> getSectionParentGenealogy(UUID sectionId, Integer levels, UUID requestByUserId) {
		ModelSection section = modelSectionRepository.findById(sectionId);
		Boolean isMemberOfEcosystem = initiativeService.isMemberOfEcosystem(section.getInitiative().getId(), requestByUserId);
		ModelSectionLinkedDto linkedDtos = getModelSectionDtosFromNodes(getSectionNode(sectionId, true, false, levels, requestByUserId, isMemberOfEcosystem));
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
	public GraphNode getSectionNode(
			UUID sectionId, 
			Boolean addParents, 
			Boolean addChildren, 
			Integer levels) {
		
		/* levels = null means as many levels as available */
		return getSectionNodeRec(sectionId, addParents, addChildren, levels, null, false, new ArrayList<UUID>());
	}	
	
	@Transactional
	public GraphNode getSectionNode(
			UUID sectionId, 
			Boolean addParents, 
			Boolean addChildren, 
			Integer levels, 
			UUID requestByUserId, 
			Boolean isMemberOfEcosystem) {
		
		/* levels = null means as many levels as available */
		return getSectionNodeRec(sectionId, addParents, addChildren, levels, requestByUserId, isMemberOfEcosystem, new ArrayList<UUID>());
	}
	
	private GraphNode getSectionNodeRec(UUID sectionId, Boolean addParents, Boolean addChildren, Integer levels, UUID requestByUserId, Boolean isMemberOfEcosystem, List<UUID> readIds) {
		
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
				List<UUID> parents = null;
				if (requestByUserId != null) {
					parents = modelSubsectionRepository.findParentSectionsIdsVisibleToUser(sectionId, requestByUserId, isMemberOfEcosystem);
				} else {
					parents = modelSubsectionRepository.findParentSectionsIds(sectionId);
				}
				
				for (UUID inSectionId : parents) {
					if (!readIds.contains(inSectionId)) {
						GraphNode parentNode = null;
						parentNode = getSectionNodeRec(inSectionId, addParents, false, levels, requestByUserId, isMemberOfEcosystem, readIds);	
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
				List<UUID> children = null;
				if (requestByUserId != null) {
					children = modelSubsectionRepository.findSubsectionsIdsVisibleToUser(sectionId, requestByUserId, isMemberOfEcosystem);
				} else {
					children = modelSubsectionRepository.findSubsectionsIds(sectionId);
				} 
				
				for (UUID subSectionId : children) {
					if (!readIds.contains(subSectionId)) {
						GraphNode childrenNode = null;
						childrenNode = getSectionNodeRec(subSectionId, false, addChildren, levels, requestByUserId, isMemberOfEcosystem, readIds);	
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
	public List<UUID> getAllSubsectionsIds (
			UUID sectionId,
			UUID userId, 
			Integer level) {
		
		GraphNode subsection = getSectionNode(sectionId, false, true, level, userId, false);
		return subsection.toList(false, true);
	}
	
	@Transactional
	public List<UUID> getAllSubsectionsIds (
			UUID sectionId, 
			Integer level, 
			UUID requestByUserId, 
			Boolean isMemberOfEcosystem) {
		
		GraphNode subsection = getSectionNode(sectionId, false, true, level, requestByUserId, isMemberOfEcosystem);
		return subsection.toList(false, true);
	}
	
	@Transactional
	public GetResult<Page<ActivityDto>> getActivityResultUnderSection (
			UUID sectionId, 
			PageRequest page, 
			Boolean addMessages, 
			Boolean addEvents,
			Integer level, 
			UUID requestByUserId) {
		
		Page<Activity> activities = getActivityUnderSection(sectionId, page, addMessages, addEvents, level, requestByUserId);
		
		List<ActivityDto> activityDtos = new ArrayList<ActivityDto>();
		
		for(Activity activity : activities.getContent()) {
			activityDtos.add(activity.toDto());
		}
		
		Page<ActivityDto> dtosPage = new PageImpl<ActivityDto>(activityDtos, page, activities.getNumberOfElements());
		
		return new GetResult<Page<ActivityDto>>("succes", "actvity returned", dtosPage);
	}
	
	@Transactional
	public Page<Activity> getActivityUnderSection (
			UUID sectionId, 
			PageRequest page, 
			Boolean addMessages, 
			Boolean addEvents, 
			Integer level, 
			UUID requestByUserId) {
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		Boolean isMemberOfEcosystem = initiativeService.isMemberOfEcosystem(section.getInitiative().getId(), requestByUserId);
		
		List<UUID> allSectionIds = getAllSubsectionsIds(sectionId, level, requestByUserId, isMemberOfEcosystem);
		List<UUID> cardsIds = allSectionIds.size() > 0 ? modelCardWrapperAdditionRepository.findAllCardWrapperIdsOfSections(allSectionIds) : new ArrayList<UUID>();
		
		if (cardsIds.size() == 0) {
			cardsIds.add(UUID.randomUUID());
		}
		
		Page<Activity> activities = null;
		activities = activityRepository.findOfSectionsOrCardsAndType(
				allSectionIds, 
				cardsIds,
				ActivityType.MESSAGE_POSTED,
				addMessages,
				addEvents,
				page);	
			
		return activities;
	}
	
	@Transactional
	public GetResult<Long> countMessagesUnderCard (UUID cardWrapperId, Boolean onlyMessages) {
		Page<Activity> messages = getActivityUnderCard(cardWrapperId, new PageRequest(1, 1), true, false);
		return new GetResult<Long>("success", "activity counted", messages.getTotalElements());
	}
	
	@Transactional
	public Page<Activity> getActivityUnderCard (UUID cardWrapperId, PageRequest page, Boolean addMessages, Boolean addEvents) {
		Page<Activity> activities = null;
		List<UUID> dum = new ArrayList<UUID>();
		List<UUID> cardIds = new ArrayList<UUID>();
		
		dum.add(UUID.randomUUID());
		cardIds.add(cardWrapperId);
		
		activities = activityRepository.findOfSectionsOrCardsAndType(
				dum, 
				cardIds, 
				ActivityType.MESSAGE_POSTED,
				addMessages,
				addEvents,
				page);
		
		return activities;
	}
	
	@Transactional
	public GetResult<Page<ActivityDto>> getActivityResultUnderCard (UUID cardWrapperId, PageRequest page, Boolean addMessages, Boolean addEvents) {
		
		Page<Activity> activities = getActivityUnderCard(cardWrapperId, page, addMessages, addEvents);
	
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
	
	@Transactional
	public PostResult setSimpleConsentState (UUID elementId, SimpleConsentState state, UUID requestByUserId) {
		ModelCardWrapperAddition cardWrapperAddition = modelCardWrapperAdditionRepository.findById(elementId);
		
		if (cardWrapperAddition.getGovernanceType() != ElementGovernanceType.SIMPLE_CONSENT) {
			cardWrapperAddition.setGovernanceType(ElementGovernanceType.SIMPLE_CONSENT);
			cardWrapperAddition.setSimpleConsentState(SimpleConsentState.OPENED);
			
			activityService.consentStatusStarted(cardWrapperAddition, appUserRepository.findByC1Id(requestByUserId));
		} else {
			if (state == SimpleConsentState.CLOSED) {
				cardWrapperAddition.setSimpleConsentState(SimpleConsentState.CLOSED);
				activityService.consentStatusClosed(cardWrapperAddition, appUserRepository.findByC1Id(requestByUserId));
			} else {
				cardWrapperAddition.setSimpleConsentState(SimpleConsentState.OPENED);
				activityService.consentStatusReopened(cardWrapperAddition, appUserRepository.findByC1Id(requestByUserId));
			}
		}
		
		modelCardWrapperAdditionRepository.save(cardWrapperAddition);
		
		return new PostResult("success", "card consent state changed", cardWrapperAddition.getId().toString()); 
	}
	
	@Transactional
	public PostResult setSimpleConsentUserPosition(UUID elementId, UUID authorId, ElementConsentPositionColor positionColor) {
		ModelCardWrapperAddition cardWrapperAddition = modelCardWrapperAdditionRepository.findById(elementId);
		ElementConsentPosition consentPosition = consentPositionRepository.findByElementIdAndAuthor_c1Id(elementId, authorId);
		
		if (consentPosition == null) {
			consentPosition = new ElementConsentPosition();
			consentPosition.setAuthor(appUserRepository.findByC1Id(authorId));
			consentPosition.setElementId(elementId);
			
			activityService.consentPositionStated(cardWrapperAddition, positionColor, appUserRepository.findByC1Id(authorId));
		} else {
			activityService.consentPositionChanged(cardWrapperAddition, positionColor, appUserRepository.findByC1Id(authorId));
		}
		
		consentPosition.setPositionColor(positionColor);
		consentPosition = consentPositionRepository.save(consentPosition);
		
		return new PostResult("success", "semaphore state changed", consentPosition.getId().toString());
	}
	
	@Transactional
	public GetResult<List<ElementConsentPositionDto>> getConsentPositions(UUID elementId) {
		List<ElementConsentPosition> semaphores = consentPositionRepository.findByElementId(elementId);
		
		List<ElementConsentPositionDto> semaphoresDtos = new ArrayList<ElementConsentPositionDto>();
		
		for (ElementConsentPosition semaphore : semaphores) {
			semaphoresDtos.add(semaphore.toDto());
		}
		return new GetResult<List<ElementConsentPositionDto>>("success", "semaphores retreived", semaphoresDtos);
	}
	
}
