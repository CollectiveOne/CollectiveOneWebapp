package org.collectiveone.modules.contexts;

import java.util.UUID;

import javax.transaction.Transactional;

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
import org.collectiveone.modules.contexts.repositories.UserDefaultPerspectiveRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContextOuterService {
	
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
	private UserDefaultPerspectiveRepositoryIf userDefaultPerspectiveRepository;
	
	
	@Transactional
	public PostResult createContext(
			ContextMetadataDto contextMetadataDto,
			UUID perspectiveId, 
			UUID creatorId, 
			UUID beforeContextId, 
			UUID afterContextId) {
		
		Perspective perspective = contextInnerService.createContext(creatorId, contextMetadataDto);
		Perspective parentPerspective = perspectiveRepository.findById(perspectiveId);
		
		Subcontext subcontext = new Subcontext();
		
		subcontext.setOnPerspective(parentPerspective);
		subcontext.setPerspective(perspective);
		/* TODO: add before and after element */
		
		subcontext = subcontextRepository.save(subcontext);
		
		Commit workingCommit = perspectiveInnerService.getOrCreateWorkingCommit(perspectiveId, creatorId);
		
		StageSubcontext stage = new StageSubcontext(workingCommit, StageAction.ADD, subcontext);
		workingCommit.getSubcontextStaged().add(stage);
		workingCommit = commitRepository.save(workingCommit);
		
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
				userDefaultPerspectiveRepository.findDefaultPerspetiveIdByContextIdAndUserId(
						contextId, requestBy);
		
		return new GetResult<PerspectiveDto>(
				"success", 
				"default perspective retrieved", 
				contextInnerService.getPerspectiveDto(
						perspectiveId,
						requestBy,
						levels,
						addCards));		
	}
	
}
