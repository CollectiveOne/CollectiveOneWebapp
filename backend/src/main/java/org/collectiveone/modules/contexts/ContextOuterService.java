package org.collectiveone.modules.contexts;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.contexts.dto.CommitDto;
import org.collectiveone.modules.contexts.dto.ContextMetadataDto;
import org.collectiveone.modules.contexts.dto.PerspectiveDto;
import org.collectiveone.modules.contexts.dto.StagedElementDto;
import org.collectiveone.modules.contexts.entities.Commit;
import org.collectiveone.modules.contexts.entities.Perspective;
import org.collectiveone.modules.contexts.entities.StagedElement;
import org.collectiveone.modules.contexts.entities.Subcontext;
import org.collectiveone.modules.contexts.entities.enums.StageAction;
import org.collectiveone.modules.contexts.entities.enums.StageStatus;
import org.collectiveone.modules.contexts.entities.enums.StageType;
import org.collectiveone.modules.contexts.repositories.CommitRepositoryIf;
import org.collectiveone.modules.contexts.repositories.PerspectiveRepositoryIf;
import org.collectiveone.modules.contexts.repositories.StagedElementRepositoryIf;
import org.collectiveone.modules.contexts.repositories.SubcontextRepositoryIf;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContextOuterService {
	
	private static final Logger logger = LogManager.getLogger(ContextOuterService.class);
	
	@Autowired
	private ContextInnerService contextInnerService;
	
	@Autowired
	private PerspectiveInnerService perspectiveInnerService;
	
	@Autowired
	private PerspectiveRepositoryIf perspectiveRepository;
	
	@Autowired
	private SubcontextRepositoryIf subcontextRepository;
	
	@Autowired
	private CommitRepositoryIf commitRepository;
	
	@Autowired
	private StagedElementRepositoryIf stagedElementRepository;
	
	@Autowired
	private AppUserRepositoryIf appUserRepository;
	
	
	/* A new context is created as a subcontext of the parentPerspectiveId, it is not committed
	 * but stored in the users workingCommit area for that parent perspective */
	@Transactional
	public PostResult createContext(
			ContextMetadataDto contextMetadataDto,
			UUID parentPerspectiveId, 
			UUID creatorId, 
			UUID beforeContextId, 
			UUID afterContextId) {
		
		Perspective perspective = contextInnerService.createContext(creatorId, contextMetadataDto);
		Perspective parentPerspective = perspectiveRepository.findOne(parentPerspectiveId);
		
		Subcontext subcontext = new Subcontext();
		
		subcontext.setAdder(appUserRepository.findOne(creatorId));
		subcontext.setOnPerspective(parentPerspective);
		subcontext.setPerspective(perspective);
		
		subcontext = subcontextRepository.save(subcontext);
		
		Commit workingCommit = perspectiveInnerService.getOrCreateWorkingCommit(parentPerspective.getId(), creatorId);
		
		StagedElement stage = new StagedElement(StageType.SUBCONTEXT, StageAction.ADD);
		stage.setSubcontext(subcontext);
		
		workingCommit.getStagedElements().add(stage);
		workingCommit = commitRepository.save(workingCommit);
		
		logger.debug("added perspective {} of context {} as subcontext of perspective {} of context {}",
				perspective.getId(), perspective.getContext().getId(), parentPerspective.getId(), parentPerspective.getContext().getId());
		
		return new PostResult("success", "context created", subcontext.getPerspective().getContext().getId().toString());
	}
	
	@Transactional
	public GetResult<PerspectiveDto> getContext(
			UUID contextId, 
			UUID requestBy,
			Integer levels,
			Boolean addCards) {
		
		/* the default branch of this context for this user is retrieved */
		UUID perspectiveId = contextInnerService.findDefaultPerspective(contextId, requestBy);
		
		return new GetResult<PerspectiveDto>(
				"success", 
				"default perspective retrieved", 
				contextInnerService.getPerspectiveDtoRec(
						perspectiveId,
						requestBy,
						levels,
						addCards, 
						new HashSet<UUID>()));		
	}
	
	@Transactional
	public PostResult commitWorkingCommit(
			UUID perspectiveId, 
			CommitDto commitDto,
			Integer levels,
			UUID requestBy) {
		
		return new PostResult(
				"success", 
				"commit created", 
				contextInnerService.commitWorkingCommit(
						perspectiveId, commitDto, levels, requestBy).toString());
					
	}
	
	@Transactional
	public GetResult<List<StagedElementDto>> getStagedElements(
			UUID perspectiveId,
			Integer levels,
			UUID requestBy) {
		
		List<StagedElement> stagedElements = 
				contextInnerService.getStagedElements(perspectiveId, requestBy);
		
		List<StagedElementDto> stagedElementsDtos = 
				stagedElements
				.stream()
				.map(stagedElement -> stagedElement.toDto())
				.collect(Collectors.toList());
		
		return new GetResult<List<StagedElementDto>>(
				"success", 
				"staged elements retrieved", 
				stagedElementsDtos);
		
	}
	
	@Transactional
	public PostResult setStagedElementStatus(
			UUID stagedElementId,
			StageStatus status,
			UUID requestBy) {
		
		/* only author can change status of staged elemets */
		UUID authorId = commitRepository.findAuthorIdFromStagedElementId(stagedElementId);
		
		if (!authorId.equals(requestBy)) {
			return new PostResult("error", "only authors can change staged element status", null); 
		}
		
		StagedElement stagedElement = stagedElementRepository.findOne(stagedElementId);
		stagedElement.setStatus(status);
		stagedElementRepository.save(stagedElement);
		
		return new PostResult("success", "staged element updated", stagedElement.getId().toString());
	}
	
	
	
}
