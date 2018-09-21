package org.collectiveone.modules.contexts;

import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.contexts.dto.NewContextDto;
import org.collectiveone.modules.contexts.repositories.CommitRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContextOuterService {
	
	@Autowired
	private ContextInnerService contextInnerService;
	
	@Autowired
	private PerspectiveInnerService perspectiveInnerService;
	
	@Autowired
	private CommitRepositoryIf commitRepository;
	
	
	@Transactional
	public PostResult createContext(
			NewContextDto contextDto, 
			UUID perspectiveId, 
			UUID creatorId, 
			UUID beforeContextId, 
			UUID afterContextId) {
		
		Commit workingCommit = perspectiveInnerService.getOrCreateWorkingCommit(perspectiveId, creatorId);
		
		Perspective subcontextPerspective = contextInnerService.createContext(contextDto, creatorId);
		
		StageSubcontext stage = new StageSubcontext(workingCommit, StageAction.ADD, subcontextPerspective);
		workingCommit.getSubcontextStaged().add(stage);
		
		workingCommit = commitRepository.save(workingCommit);
		
		return new PostResult("success", "new card staged", workingCommit.getId().toString());
	}


}
