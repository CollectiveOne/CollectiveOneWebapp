package org.collectiveone.modules.contexts;

import java.util.ArrayList;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.contexts.dto.ContextMetadataDto;
import org.collectiveone.modules.contexts.dto.PerspectiveDto;
import org.collectiveone.modules.contexts.entities.Commit;
import org.collectiveone.modules.contexts.entities.Perspective;
import org.collectiveone.modules.contexts.entities.StageAction;
import org.collectiveone.modules.contexts.entities.StageSubcontext;
import org.collectiveone.modules.contexts.entities.Subcontext;
import org.collectiveone.modules.contexts.repositories.CommitRepositoryIf;
import org.collectiveone.modules.contexts.repositories.PerspectiveRepositoryIf;
import org.collectiveone.modules.contexts.repositories.SubcontextRepositoryIf;
import org.collectiveone.modules.contexts.repositories.UserActivePerspectiveRepositoryIf;
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
	private UserActivePerspectiveRepositoryIf userActivePerspectiveRepository;
	
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
		Perspective parentPerspective = perspectiveRepository.findById(parentPerspectiveId);
		
		Subcontext subcontext = new Subcontext();
		
		subcontext.setOnPerspective(parentPerspective);
		subcontext.setPerspective(perspective);
		
		subcontext = subcontextRepository.save(subcontext);
		
		Commit workingCommit = perspectiveInnerService.getOrCreateWorkingCommit(parentPerspective.getId(), creatorId);
		
		StageSubcontext stage = new StageSubcontext(workingCommit, StageAction.ADD, subcontext);
		workingCommit.getSubcontextStaged().add(stage);
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
		UUID perspectiveId = 
				userActivePerspectiveRepository.findDefaultPerspetiveIdByContextIdAndUserId(
						contextId, requestBy);
		
		return new GetResult<PerspectiveDto>(
				"success", 
				"default perspective retrieved", 
				contextInnerService.getPerspectiveDto(
						perspectiveId,
						requestBy,
						levels,
						addCards, 
						new ArrayList<UUID>()));		
	}
	
}
