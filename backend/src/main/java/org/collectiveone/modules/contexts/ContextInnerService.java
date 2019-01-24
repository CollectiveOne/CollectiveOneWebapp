package org.collectiveone.modules.contexts;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.collectiveone.modules.contexts.cards.CardW;
import org.collectiveone.modules.contexts.dto.ContextMetadataDto;
import org.collectiveone.modules.contexts.dto.PerspectiveDto;
import org.collectiveone.modules.contexts.entities.CardInP;
import org.collectiveone.modules.contexts.entities.Commit;
import org.collectiveone.modules.contexts.entities.Context;
import org.collectiveone.modules.contexts.entities.ContextMetadata;
import org.collectiveone.modules.contexts.entities.Perspective;
import org.collectiveone.modules.contexts.entities.StageCard;
import org.collectiveone.modules.contexts.entities.StageMetadata;
import org.collectiveone.modules.contexts.entities.StageSubcontext;
import org.collectiveone.modules.contexts.entities.UserActivePerspective;
import org.collectiveone.modules.contexts.entitiesRedundant.PerspectiveCache;
import org.collectiveone.modules.contexts.repositories.CommitRepositoryIf;
import org.collectiveone.modules.contexts.repositories.ContextMetadataRepositoryIf;
import org.collectiveone.modules.contexts.repositories.ContextRepositoryIf;
import org.collectiveone.modules.contexts.repositories.PerspectiveCacheRepositoryIf;
import org.collectiveone.modules.contexts.repositories.PerspectiveRepositoryIf;
import org.collectiveone.modules.contexts.repositories.StageMetadataRepositoryIf;
import org.collectiveone.modules.contexts.repositories.UserActivePerspectiveRepositoryIf;
import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
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
	private StageMetadataRepositoryIf stageMetadataRepository;
	
	@Autowired
	private UserActivePerspectiveRepositoryIf userDefaultPerspectiveRepository;	
	
	@Autowired
	private AppUserRepositoryIf appUserRepository;
	
	@Autowired
	private PerspectiveCacheRepositoryIf perspectiveCacheRepository;
	
	
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
		
		StageMetadata stageMetadata = new StageMetadata();
		ContextMetadata contextMetadata = new ContextMetadata();
		
		contextMetadata.setTitle(contextMetadataDto.getTitle());
		contextMetadata.setDescription(contextMetadataDto.getDescription());
		contextMetadata = contextMetadataRepository.save(contextMetadata);
		
		stageMetadata.setContextMetadata(contextMetadata);
		stageMetadata = stageMetadataRepository.save(stageMetadata);
		
		Commit firstCommit = new Commit(author);
		firstCommit.setMetadataStaged(stageMetadata);
		firstCommit = commitRepository.save(firstCommit); ;
		
		commitToPerspective(perspective.getId(), firstCommit);
		
		/* set this as the default perspective for this context for the author */ 
		UserActivePerspective defaultPerspective = new UserActivePerspective();
		defaultPerspective.setContext(context);
		defaultPerspective.setPerspective(perspective);
		defaultPerspective.setUser(author);
		
		userDefaultPerspectiveRepository.save(defaultPerspective);
		
		return perspective;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Boolean commitToPerspective(UUID perspectiveId, Commit commit) {
		Perspective perspective = perspectiveRepository.findById(perspectiveId);
		perspective.setHead(commit);
		perspectiveRepository.save(perspective);
				
		/* update cache */
		PerspectiveCache perspectiveCache = perspectiveCacheRepository.findByPerspectiveId(perspectiveId);
		
		if(commit.getMetadataStaged() != null) {
			perspectiveCache.setMetadata(commit.getMetadataStaged().getContextMetadata());
		}
		
		if(commit.getCardsStaged().size() > 0) {
			for(StageCard stageCard : commit.getCardsStaged()) {
				
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
		
		if(commit.getSubcontextStaged().size() > 0) {
			for(StageSubcontext stageSubcontext : commit.getSubcontextStaged()) {
				
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
	public PerspectiveDto getPerspectiveDto(
			UUID perspectiveId, 
			UUID requestBy,
			Integer levels,
			Boolean addCards,
			List<UUID> readIds) {
		
		PerspectiveCache perspectiveCache = perspectiveCacheRepository.findByPerspectiveId(perspectiveId);
		
		PerspectiveDto perspectiveDto = new PerspectiveDto();
		
		perspectiveDto.setMetadata(perspectiveCache.getMetadata().toDto());
		
		if(addCards) {
			for (CardInP cardInP : perspectiveCache.getCardsInP()) {
				perspectiveDto.getCards().add(cardInP.getCardW().getCard().toDto());
			}
		}
		
		/* add changes from working commit */
		Commit workingCommit = perspectiveRepository.findWorkingCommit(perspectiveId, requestBy);
		
		if (workingCommit != null) {
			if(workingCommit.getMetadataStaged() != null) {
				perspectiveDto.setMetadata(workingCommit.getMetadataStaged().getContextMetadata().toDto());
			}
			
			if(addCards) {
				for (StageCard stageCard : workingCommit.getCardsStaged()) {
					perspectiveDto.getCards().add(stageCard.getCardInP().getCardW().getCard().toDto());
				}
			}	
		}
		
		
		/* add subcontexts */
		if(levels > 0) {
			List<UUID> subperspectiveIds = 
					perspectiveCacheRepository.findSubperspectivesIds(perspectiveId);
			
			/* add the subcontexts added in the working commit */
			if (workingCommit != null) {
				subperspectiveIds.addAll(commitRepository.findSubperspectivesIds(workingCommit.getId()));
			}
			
			/* recursively call this very same function to get the subcontexts perspectives dtos */
			for(UUID subperspectiveId : subperspectiveIds) {
				/* cycle protection */
				if(!readIds.contains(subperspectiveId)) {
					readIds.add(subperspectiveId);
					PerspectiveDto subperspectiveDto = getPerspectiveDto(subperspectiveId, requestBy, levels - 1, addCards, readIds);
					perspectiveDto.getSubcontexts().add(subperspectiveDto);
				}
			}
				
		}
		
		return perspectiveDto;
	}

}