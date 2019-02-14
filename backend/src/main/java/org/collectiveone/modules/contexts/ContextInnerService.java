package org.collectiveone.modules.contexts;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.collectiveone.modules.contexts.cards.CardW;
import org.collectiveone.modules.contexts.dto.CardDto;
import org.collectiveone.modules.contexts.dto.CommitDto;
import org.collectiveone.modules.contexts.dto.ContextMetadataDto;
import org.collectiveone.modules.contexts.dto.PerspectiveDto;
import org.collectiveone.modules.contexts.entities.CardInP;
import org.collectiveone.modules.contexts.entities.Commit;
import org.collectiveone.modules.contexts.entities.CommitGroup;
import org.collectiveone.modules.contexts.entities.Context;
import org.collectiveone.modules.contexts.entities.ContextMetadata;
import org.collectiveone.modules.contexts.entities.Perspective;
import org.collectiveone.modules.contexts.entities.StagedElement;
import org.collectiveone.modules.contexts.entities.UserActivePerspective;
import org.collectiveone.modules.contexts.entities.enums.CommitStatus;
import org.collectiveone.modules.contexts.entities.enums.StageAction;
import org.collectiveone.modules.contexts.entities.enums.StageStatus;
import org.collectiveone.modules.contexts.entities.enums.StageType;
import org.collectiveone.modules.contexts.entitiesRedundant.PerspectiveCache;
import org.collectiveone.modules.contexts.repositories.CommitGroupRepositoryIf;
import org.collectiveone.modules.contexts.repositories.CommitRepositoryIf;
import org.collectiveone.modules.contexts.repositories.ContextMetadataRepositoryIf;
import org.collectiveone.modules.contexts.repositories.ContextRepositoryIf;
import org.collectiveone.modules.contexts.repositories.PerspectiveCacheRepositoryIf;
import org.collectiveone.modules.contexts.repositories.PerspectiveRepositoryIf;
import org.collectiveone.modules.contexts.repositories.StagedElementRepositoryIf;
import org.collectiveone.modules.contexts.repositories.UserActivePerspectiveRepositoryIf;
import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ContextInnerService {
	
	private static final Logger logger = LogManager.getLogger(ContextInnerService.class);
	
	@Autowired
	private ContextRepositoryIf contextRepository;
	
	@Autowired
	private PerspectiveRepositoryIf perspectiveRepository;
	
	@Autowired
	private CommitRepositoryIf commitRepository;

	@Autowired
	private ContextMetadataRepositoryIf contextMetadataRepository;

	@Autowired
	private StagedElementRepositoryIf stagedElementRepository;
	
	@Autowired
	private UserActivePerspectiveRepositoryIf userActivePerspectiveRepository;	
	
	@Autowired
	private AppUserRepositoryIf appUserRepository;
	
	@Autowired
	private PerspectiveCacheRepositoryIf perspectiveCacheRepository;
	
	@Autowired
	private CommitGroupRepositoryIf commitGroupRepository;
	
	
	
	/**
	 * create a brand new context and its default perspective
	 *  
	 * */
	@Transactional(rollbackOn = Exception.class)
	public Perspective createContext(UUID creatorId, ContextMetadataDto contextMetadataDto) {
		
		Context context = new Context();
		context = contextRepository.save(context);
		
		Perspective pespective = createPerspective(context, creatorId, contextMetadataDto, "default");
		
		logger.debug("context created id: {}", context.getId());
		logger.debug("default perspective id: {}", pespective.getId());
		
		return pespective;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Perspective createPerspective(Context context, UUID creatorId, ContextMetadataDto contextMetadataDto, String name) {
		
		Perspective perspective = new Perspective();
		perspective = perspectiveRepository.save(perspective);
		perspective.setContext(context);
		perspective.setName(name);
		
		/* each perspective must have one perspective cache */
		PerspectiveCache perspectiveCache = new PerspectiveCache();
		perspectiveCache.setPerspective(perspective);
		perspectiveCache = perspectiveCacheRepository.save(perspectiveCache);
		
		AppUser author = appUserRepository.findById(creatorId);
		
		StagedElement stageMetadata = new StagedElement(StageType.METADATA, StageAction.ADD);
		ContextMetadata contextMetadata = new ContextMetadata();
		
		contextMetadata.setTitle(contextMetadataDto.getTitle());
		contextMetadata.setDescription(contextMetadataDto.getDescription());
		contextMetadata = contextMetadataRepository.save(contextMetadata);
		
		stageMetadata.setContextMetadata(contextMetadata);
		stageMetadata.setStatus(StageStatus.ADDED);
		stageMetadata = stagedElementRepository.save(stageMetadata);
		
		Commit firstCommit = new Commit(author);
		firstCommit.getStagedElements().add(stageMetadata);
		firstCommit = commitRepository.save(firstCommit); ;
		
		CommitGroup group = new CommitGroup();
		group = commitGroupRepository.save(group);
		
		firstCommit.setGroup(group);
		
		commitToPerspective(perspective.getId(), firstCommit);
		
		/* set this as the default perspective for this context for the author */ 
		UserActivePerspective activePerspective = new UserActivePerspective();
		activePerspective.setContext(context);
		activePerspective.setPerspective(perspective);
		activePerspective.setUser(author);
		
		userActivePerspectiveRepository.save(activePerspective);
		
		return perspective;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public UUID findDefaultPerspective(UUID contextId, UUID userId) {
		UUID perspectiveId = 
				userActivePerspectiveRepository.findDefaultPerspetiveIdByContextIdAndUserId(contextId, userId);
		
		if(perspectiveId == null) {
			
			PageRequest pageRequest = 
					new PageRequest(0, 1, new Sort(Sort.Direction.DESC, "creationDate"));
			
			/* what is the default perspective for a context? */
			Page<Perspective> page = perspectiveRepository.findByContextId(contextId, pageRequest);
			
			Perspective perspective = page.getContent().get(0);
			
			UserActivePerspective activePerspective = new UserActivePerspective();
			
			AppUser author = appUserRepository.findById(userId);
			Context context = contextRepository.findOne(contextId);
			
			activePerspective.setContext(context);
			activePerspective.setPerspective(perspective);
			activePerspective.setUser(author);
			
			userActivePerspectiveRepository.save(activePerspective);
			
			perspectiveId = perspective.getId();
		}
		
		return perspectiveId;
	}
		
	@Transactional(rollbackOn = Exception.class)
	public Boolean addStage(UUID stageId) {
		
		StagedElement stageElement = stagedElementRepository.findOne(stageId);
		
		stageElement.setStatus(StageStatus.ADDED);
		stagedElementRepository.save(stageElement);
		
		return true;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public List<StagedElement> getStagedElements(UUID perspectiveId, UUID requestBy) {
		List<StagedElement> stagedElements = perspectiveRepository.findStagedElements(perspectiveId, requestBy);
		return stagedElements;
	}

	/* *creates* a commitGroup and calls the recursive commit of working commits */
	@Transactional(rollbackOn = Exception.class)
	public UUID commitWorkingCommit(
			UUID perspectiveId, 
			CommitDto commitDto,
			Integer levels, 
			UUID requestBy) {
		
		CommitGroup group = new CommitGroup();
		group = commitGroupRepository.save(group);
		
		return commitWorkingCommitRec(perspectiveId, commitDto, levels, requestBy, group, new HashSet<UUID>());
	}
	
	/* commits the working commit of a perspective and of its subperspectives (up to a number of levels) 
	 * in the same commitGroup */
	@Transactional(rollbackOn = Exception.class)
	public UUID commitWorkingCommitRec(
			UUID perspectiveId, 
			CommitDto commitDto,
			Integer levels, 
			UUID requestBy,
			CommitGroup group,
			HashSet<UUID> addedIds) {
		
		Commit workingCommit = perspectiveRepository.findWorkingCommit(perspectiveId, requestBy);
		
		/* create a new commit object that is the one actually added as head of the perspective. 
		 * The working commit remains as the working commit for that user. ADDED stagedElements
		 * from the working commit must be moved to the new commit */
		
		List<StagedElement> stagedElements = 
				commitRepository.findStagedElementsWithStatus(workingCommit.getId(), StageStatus.ADDED);
		
		UUID commitId = null;
		
		if (stagedElements.size() > 0) {
			Commit commit = new Commit();
			
			commit.setAuthor(workingCommit.getAuthor());
			commit.setGroup(group);
			commit.setMessage(commitDto.getMessage());
			commit.getParents().add(perspectiveRepository.findHead(perspectiveId));
			
			for (StagedElement element : stagedElements) {
				workingCommit.getStagedElements().remove(element);
				commit.getStagedElements().add(element);
			}
			
			commit = commitRepository.save(commit);
			workingCommit = commitRepository.save(workingCommit);
			
			commitToPerspective(perspectiveId, commit);
			commitId = commit.getId();
		}
	
		if (levels > 0) {
			List<UUID> allPerspectivesIds = getSubperspectivesIds(perspectiveId, requestBy);
			
			allPerspectivesIds.forEach(id -> {
				if(!addedIds.contains(id)) {
					addedIds.add(id);
					commitWorkingCommitRec(
							id,
							commitDto,
							levels - 1,
							requestBy,
							group, 
							addedIds);
				}
			});
		}
			
		return commitId;
	}
	
	
	@Transactional(rollbackOn = Exception.class)
	public Boolean commitToPerspective(
			UUID perspectiveId, 
			Commit commit) {
		
		/** Committing to a perspective means updating the head commit of that perspective */
		Perspective perspective = perspectiveRepository.findOne(perspectiveId);
		perspective.setHead(commit);
		perspectiveRepository.save(perspective);
				
		/** However, the perspective cache (accumulation of changes) 
		* needs to be updated too */
		PerspectiveCache perspectiveCache = perspectiveCacheRepository.findByPerspectiveId(perspectiveId);
		
		StagedElement stageMetadata = commitRepository.getStageMetadata(commit.getId());
		
		if(stageMetadata != null) {
			if(stageMetadata.getStatus() == StageStatus.ADDED) {
				perspectiveCache.setMetadata(stageMetadata.getContextMetadata());
			}
		}
		
		List<StagedElement> stagedCards = commitRepository.getStagedElementsOfType(commit.getId(), StageType.CARD);
		
		for(StagedElement stageCard : stagedCards) {
			
			if(stageCard.getStatus() == StageStatus.ADDED) {
				switch (stageCard.getAction()) {
				
				case ADD:
					perspectiveCache.getCardsInP().add(stageCard.getCardInP());
					break;
					
				case REMOVE:
					perspectiveCache.getCardsInP().remove(stageCard.getCardInP());
					break;
					
				case EDIT:
					CardW cardW = stageCard.getCardInP().getCardW();
					stageCard.setOldVersion(cardW.getCard());
					cardW.setCard(stageCard.getNewVersion());
					break;
				}
			}
		}
		
		
		List<StagedElement> stagedSubcontexts = commitRepository.getStagedElementsOfType(commit.getId(), StageType.SUBCONTEXT);
		
		for(StagedElement stageSubcontext : stagedSubcontexts) {
			
			if(stageSubcontext.getStatus() == StageStatus.ADDED) {
				switch (stageSubcontext.getAction()) {
				
				case ADD:
					perspectiveCache.getSubcontexts().add(stageSubcontext.getSubcontext());
					break;
				
				case REMOVE:
					perspectiveCache.getSubcontexts().remove(stageSubcontext.getSubcontext());
					break;
					
				case EDIT:
					/* NOP */
					break;
				}
			}
		}
		
		perspectiveCacheRepository.save(perspectiveCache);
		
		return true;
	} 
	
	@Transactional
	public List<UUID> getSubperspectivesIds(UUID perspectiveId, UUID requestBy) {
		UUID workingCommitId = perspectiveRepository.findWorkingCommitId(perspectiveId, requestBy);
		
		List<UUID> subperspectiveIds = 
				perspectiveCacheRepository.findSubperspectivesIds(perspectiveId);
		
		/* add the subcontexts added in the working commit */
		if (workingCommitId != null) {
			subperspectiveIds.addAll(commitRepository.findSubperspectivesIds(workingCommitId));
		}
		
		return subperspectiveIds;
	}
	
	@Transactional
	public PerspectiveDto getPerspectiveDtoRec(
			UUID perspectiveId, 
			UUID requestBy,
			Integer levels,
			Boolean addCards,
			HashSet<UUID> readIds) {
		
		Perspective perspectice = perspectiveRepository.findOne(perspectiveId);
		PerspectiveCache perspectiveCache = perspectiveCacheRepository.findByPerspectiveId(perspectiveId);
		
		PerspectiveDto perspectiveDto = new PerspectiveDto();
		perspectiveDto.setId(perspectiveId.toString());
		perspectiveDto.setHeadId(perspectice.getHead().getId().toString());
		perspectiveDto.setCommitStatus(CommitStatus.COMMITTED);
		perspectiveDto.setMetadata(perspectiveCache.getMetadata().toDto());
		
		if(addCards) {
			for (CardInP cardInP : perspectiveCache.getCardsInP()) {
				CardDto cardDto = cardInP.getCardW().getCard().toDto();
				cardDto.setCommitStatus(CommitStatus.COMMITTED);
				perspectiveDto.getCards().add(cardDto);
			}
		}
		
		/* add changes from working commit */
		UUID workingCommitId = perspectiveRepository.findWorkingCommitId(perspectiveId, requestBy);
		
		if (workingCommitId != null) {
			
			StagedElement stageMetadata = commitRepository.getStageMetadata(workingCommitId);
			
			if(stageMetadata != null) {
				perspectiveDto.setMetadata(stageMetadata.getContextMetadata().toDto());
			}
			
			if(addCards) {
				for (StagedElement stageCard : commitRepository.getStagedElementsOfType(workingCommitId, StageType.CARD)) {
					CardDto cardDto = stageCard.getCardInP().getCardW().getCard().toDto();
					cardDto.setCommitStatus(CommitStatus.PENDING);
					perspectiveDto.getCards().add(cardDto);
				}
			}	
		}
		
		/* add subcontexts */
		if(levels > 0) {
			List<UUID> subperspectiveIds = 
					perspectiveCacheRepository.findSubperspectivesIds(perspectiveId);
			
			subperspectiveIds.forEach(id -> {
				if(!readIds.contains(id)) {
					readIds.add(id);
					PerspectiveDto subperspectiveDto = 
							getPerspectiveDtoRec(id, requestBy, levels - 1, addCards, readIds);
					
					subperspectiveDto.setCommitStatus(CommitStatus.COMMITTED);
					perspectiveDto.getSubcontexts().add(subperspectiveDto);
				}
			});
				
			/* add the subcontexts added in the working commit */
			if (workingCommitId != null) {
				List<UUID> workingSubperspectiveIds = commitRepository.findSubperspectivesIds(workingCommitId);
				
				workingSubperspectiveIds.forEach(id -> {
					if(!readIds.contains(id)) {
						readIds.add(id);
						PerspectiveDto subperspectiveDto = 
								getPerspectiveDtoRec(id, requestBy, levels - 1, addCards, readIds);
						
						subperspectiveDto.setCommitStatus(CommitStatus.PENDING);
						perspectiveDto.getSubcontexts().add(subperspectiveDto);
					}
				});
			}
		}
		
		return perspectiveDto;
	}
	
}