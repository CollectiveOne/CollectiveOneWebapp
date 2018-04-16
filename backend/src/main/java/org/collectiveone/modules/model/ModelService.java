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
import org.collectiveone.modules.model.dto.ModelCardDto;
import org.collectiveone.modules.model.dto.ModelCardWrapperDto;
import org.collectiveone.modules.model.dto.ModelSectionDto;
import org.collectiveone.modules.model.dto.ModelSectionLinkedDto;
import org.collectiveone.modules.model.repositories.ModelCardRepositoryIf;
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
	public GetResult<List<ModelCardWrapperDto>> getSectionCardWrappers(UUID sectionId, UUID requestByUserId) {
		ModelSection section = modelSectionRepository.findById(sectionId);
		
		List<ModelCardWrapperDto> cardWrappersDtos = new ArrayList<ModelCardWrapperDto>(); 
		
		for (ModelCardWrapper cardWrapper : section.getCardsWrappers()) {
			cardWrappersDtos.add(getCardWrapperDtoWithMetadata(cardWrapper, requestByUserId));
		}
		
		return new GetResult<List<ModelCardWrapperDto>>("success", "section retrieved",  cardWrappersDtos);
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
		fromSection = modelSectionRepository.save(fromSection);
	
		/* moving to another section add to section as subsection */
		ModelSection toSection = modelSectionRepository.findById(toSectionId);
		
		if (beforeSubsectionId != null) {
			ModelSection beforeSubsection = modelSectionRepository.findById(beforeSubsectionId);
			int index = toSection.getSubsections().indexOf(beforeSubsection);
			toSection.getSubsections().add(index, subSection);
		} else {
			toSection.getSubsections().add(subSection);
		}
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
	public PostResult addCardToSection (UUID sectionId, UUID cardWrapperId, UUID beforeCardWrapperId, UUID creatorId) {
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		ModelCardWrapper cardWrapper = modelCardWrapperRepository.findById(cardWrapperId);
		
		ModelCardWrapper cardWrapperInSection = modelCardWrapperRepository.findByIdAndSectionId(cardWrapperId, sectionId);
		
		if (cardWrapperInSection != null) {
			return new PostResult("error", "card already in section", section.getId().toString());
		}
		
		if (beforeCardWrapperId == null) {
			section.getCardsWrappers().add(cardWrapper);
		} else {
			ModelCardWrapper beforeCardWrapper = modelCardWrapperRepository.findById(beforeCardWrapperId);
			int index = section.getCardsWrappers().indexOf(beforeCardWrapper);
			section.getCardsWrappers().add(index, cardWrapper);
		}
		
		section = modelSectionRepository.save(section);
		
		activityService.modelCardWrapperAdded(cardWrapper, section, appUserRepository.findByC1Id(creatorId));
		
		return new PostResult("success", "card added to section", section.getId().toString());
	}
	
	@Transactional
	public PostResult removeCardFromSection (UUID sectionId, UUID cardWrapperId, UUID creatorId) {
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		ModelCardWrapper cardWrapper = modelCardWrapperRepository.findById(cardWrapperId);
		
		section.getCardsWrappers().remove(cardWrapper);
		section.getCardsWrappersTrash().add(cardWrapper);
		
		section = modelSectionRepository.save(section);
		
		activityService.modelCardWrapperRemoved(cardWrapper, section, appUserRepository.findByC1Id(creatorId));
		
		return new PostResult("success", "card added to section", section.getId().toString());
	}
	
	@Transactional
	public ModelSectionDto addSectionSubElements(ModelSectionDto sectionDto, UUID sectionId, Integer level, UUID requestByUserId, Boolean onlySections) {
		
		ModelSection section = modelSectionRepository.findById(sectionId);
		
		sectionDto.setSubElementsLoaded(true);
		
		if (!onlySections) {
			for (ModelCardWrapper cardWrapper : section.getCardsWrappers()) {
				sectionDto.getCardsWrappers().add(getCardWrapperDtoWithMetadata(cardWrapper, requestByUserId));
			}
		}
				
		if (level <= 0) {
			/* add only the subsection titles and metadata */
			for (ModelSection subsection : section.getSubsections()) {
				sectionDto.getSubsections().add(subsection.toDto());
			}
		} else {
			/* add the subsections with their sub-elements too */
			for (ModelSection subsection : section.getSubsections()) {
				sectionDto.getSubsections().add(addSectionSubElements(subsection.toDto(), subsection.getId(), level - 1, requestByUserId, onlySections));
			}
		} 
		
		return sectionDto; 
	}
	
	private ModelCardWrapperDto getCardWrapperDtoWithMetadata(ModelCardWrapper cardWrapper, UUID requestByUserId) {
		ModelCardWrapperDto cardWrapperDto = cardWrapper.toDto();
		
		cardWrapperDto.setnLikes(cardLikeRepository.countOfCard(cardWrapper.getId()));
		
		if (requestByUserId != null) {
			cardWrapperDto.setUserLiked(cardLikeRepository.findByCardWrapperIdAndAuthor_c1Id(cardWrapper.getId(), requestByUserId) != null);
		}
		
		List<ModelSection> inSections = modelCardWrapperRepository.findParentSections(cardWrapper.getId());
		
		for (ModelSection inSection : inSections) {
			cardWrapperDto.getInSections().add(inSection.toDto());
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
		
		ModelCardWrapper cardWrapper = new ModelCardWrapper();
		cardWrapper.setCard(card);
		cardWrapper.setInitiative(section.getInitiative());
		cardWrapper.setCreator(creator);
		cardWrapper.setCreationDate(new Timestamp(System.currentTimeMillis()));
		cardWrapper.getEditors().add(creator);
		cardWrapper.setLastEdited(new Timestamp(System.currentTimeMillis()));
		
		cardWrapper = modelCardWrapperRepository.save(cardWrapper);
		
		Integer ix = null;
		
		if (beforeId != null) {
			ModelCardWrapper beforeCardWrapper = modelCardWrapperRepository.findById(beforeId);
			ix = section.getCardsWrappers().indexOf(beforeCardWrapper);
		}
		
		if (afterId != null) {
			ModelCardWrapper afterCardWrapper = modelCardWrapperRepository.findById(afterId);
			ix = section.getCardsWrappers().indexOf(afterCardWrapper) + 1;
		}
		
		/* add location */
		if (ix == null) {
			/* at the end */
			section.getCardsWrappers().add(cardWrapper);
		} else {
			if ((ix == -1) || (ix == section.getCardsWrappers().size() - 1)) {
				/* at the end */
				section.getCardsWrappers().add(cardWrapper);
			} else {
				/* at a given ix */
				section.getCardsWrappers().add(ix, cardWrapper);
			}
		}
		
		modelSectionRepository.save(section);
		
		activityService.modelCardWrapperCreated(cardWrapper, section, appUserRepository.findByC1Id(creatorId));
		
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
		cardWrapper.setLastEdited(new Timestamp(System.currentTimeMillis()));
		
		/* add an editor only once */
		AppUser prevEditor = modelCardWrapperRepository.findEditor(cardWrapper.getId(), creatorId);
		if (prevEditor == null) {
			cardWrapper.getEditors().add(appUserRepository.findByC1Id(creatorId));
		}
		
		/* this inSections actually refer to add to new sections */
		for (ModelSectionDto sectionDto : cardDto.getInSections()) {
			ModelSection section = modelSectionRepository.findById(UUID.fromString(sectionDto.getId()));
			
			if (section != null) {
				section.getCardsWrappers().add(cardWrapper);
			}
		}
	
		activityService.modelCardWrapperEdited(cardWrapper, appUserRepository.findByC1Id(creatorId));
		
		return new PostResult("success", "card edited", cardWrapper.getId().toString());
	}
	
	
	@Transactional
	public PostResult moveCardWrapper(UUID fromSectionId, UUID cardWrapperId, UUID toSectionId, UUID beforeCardWrapperId, UUID creatorId) {
		
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
		
		activityService.modelCardWrapperMoved(cardWrapper, fromSection, toSection, appUserRepository.findByC1Id(creatorId));
		
		return new PostResult("success", "card moved", cardWrapper.getId().toString());
	}
	
	@Transactional
	public GetResult<ModelCardWrapperDto> getCardWrapper(UUID cardWrapperId, UUID requestByUserId) {
		ModelCardWrapper cardWrapper = modelCardWrapperRepository.findById(cardWrapperId);
		List<ModelSection> inSections = modelCardWrapperRepository.findParentSections(cardWrapper.getId());
		
		ModelCardWrapperDto cardWrapperDto = cardWrapper.toDto();
		
		cardWrapperDto.setnLikes(cardLikeRepository.countOfCard(cardWrapper.getId()));
		
		if (requestByUserId != null) {
			cardWrapperDto.setUserLiked(cardLikeRepository.findByCardWrapperIdAndAuthor_c1Id(cardWrapper.getId(), requestByUserId) != null);
		}
		
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
	public GetResult<Page<ModelCardWrapperDto>> searchCardWrapper(UUID sectionId, String query, Integer page, Integer pageSize, String sortByIn, Integer levels, UUID requestById) {
		List<UUID> allSectionIds = new ArrayList<UUID>();
		
		allSectionIds.add(sectionId);
		allSectionIds.addAll(getAllSubsectionsIds(sectionId, levels - 1));
		
		PageRequest pageRequest = null;
		
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
		
		Page<ModelCardWrapper> enititiesPage = 
				modelCardWrapperRepository.searchInSectionByQuery(allSectionIds, "%"+query.toLowerCase()+"%", pageRequest);
		
		List<ModelCardWrapperDto> cardsDtos = new ArrayList<ModelCardWrapperDto>();
		
		for(ModelCardWrapper cardWrapper : enititiesPage.getContent()) {
			cardsDtos.add(getCardWrapperDtoWithMetadata(cardWrapper, requestById));
		}
		
		Page<ModelCardWrapperDto> dtosPage = new PageImpl<ModelCardWrapperDto>(cardsDtos, pageRequest, enititiesPage.getNumberOfElements());
		
		return new GetResult<Page<ModelCardWrapperDto>>("success", "cards returned", dtosPage);
	}
		
	@Transactional
	public PostResult deleteCardWrapper (UUID cardWrapperId, UUID creatorId) {
		
		
		List<ModelSection> parents = modelCardWrapperRepository.findParentSections(cardWrapperId);
		ModelCardWrapper cardWrapper = modelCardWrapperRepository.findById(cardWrapperId);
		
		/* remove from all sections */
		if (parents.size() > 0) {
			for (ModelSection parent : parents) {
				parent.getCardsWrappers().remove(cardWrapper);
				parent.getCardsWrappersTrash().add(cardWrapper);
				modelSectionRepository.save(parent);
			}
		}
		
		activityService.modelCardWrapperDeleted(cardWrapper, appUserRepository.findByC1Id(creatorId));
		
		return new PostResult("success", "section deleted", cardWrapper.getId().toString());
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
		return getSectionNodeRec(sectionId, addParents, addChildren, levels, new ArrayList<UUID>());
	}
	
	private GraphNode getSectionNodeRec(UUID sectionId, Boolean addParents, Boolean addChildren, Integer levels, List<UUID> readIds) {
		
		if (!modelSectionRepository.sectionExists(sectionId)) {
			return null;
		}
		
		GraphNode node = new GraphNode();
		node.setElementId(sectionId);
		
		readIds.add(sectionId);
		
		Boolean recurse = false;
		
		if (levels != null) {
			if (levels > 1) {
				recurse = true; 
			}
		}
		
		if (addParents) {
			List<UUID> parents = modelSectionRepository.findParentSectionsIds(sectionId);
			
			for (UUID inSectionId : parents) {
				if (!readIds.contains(inSectionId)) {
					GraphNode parentNode = null;
					if (recurse) {
						/* recursive call to search for parent parents*/
						parentNode = getSectionNodeRec(inSectionId, addParents, false, levels - 1, readIds);	
					} else {
						readIds.add(inSectionId);
						parentNode = new GraphNode();
						parentNode.setElementId(inSectionId);
					}
					
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
					if (recurse) {
						/* recursive call to search for parent parents*/
						childrenNode = getSectionNodeRec(subSectionId, false, addChildren, levels - 1, readIds);	
					} else {
						readIds.add(subSectionId);
						childrenNode = new GraphNode();
						childrenNode.setElementId(subSectionId);
					}
					
					node.getChildren().add(childrenNode);
					
				} else {
					/* if section already added, add it but don't keep looking recursively */
					GraphNode repeatedNode = new GraphNode();
					repeatedNode.setElementId(subSectionId);
					node.getChildren().add(repeatedNode);
				}
			}
			
		}
		
		return node;
	}
	
	@Transactional
	public List<UUID> getAllSubsectionsIds (UUID sectionId, Integer level) {
		GraphNode subsection = getSectionNode(sectionId, false, true, null);
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
		
		List<UUID> allSectionIds = new ArrayList<UUID>();
		
		allSectionIds.add(sectionId);
		allSectionIds.addAll(getAllSubsectionsIds(sectionId, level - 1));
		
		List<UUID> cardsIds = allSectionIds.size() > 0 ? modelCardRepository.findAllCardsIdsOfSections(allSectionIds) : new ArrayList<UUID>();
		
		Page<Activity> activities = null;
		
		if (!onlyMessages) {
			if (cardsIds.size() > 0) {
				activities = activityRepository.findOfSectionsAndCards(allSectionIds, cardsIds, page);	
			} else {
				activities = activityRepository.findOfSections(allSectionIds, page);
			}
		} else {
			if (cardsIds.size() > 0) {
				activities = activityRepository.findOfSectionsAndCardsAndType(allSectionIds, cardsIds, ActivityType.MESSAGE_POSTED, page);	
			} else {
				activities = activityRepository.findOfSectionsAndType(allSectionIds, ActivityType.MESSAGE_POSTED, page);
			}
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
		if (!onlyMessages) {
			activities = activityRepository.findOfCard(cardWrapperId, page);
		} else {
			activities = activityRepository.findOfCardAndType(cardWrapperId, ActivityType.MESSAGE_POSTED, page);
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
