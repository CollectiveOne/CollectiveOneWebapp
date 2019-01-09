package org.collectiveone.modules.contexts;

import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.modules.contexts.entities.Commit;
import org.collectiveone.modules.contexts.repositories.CommitRepositoryIf;
import org.collectiveone.modules.contexts.repositories.PerspectiveRepositoryIf;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerspectiveInnerService {
	
	@Autowired
	private AppUserRepositoryIf appUserRepositoryIf;
	
	@Autowired
	private PerspectiveRepositoryIf perspectiveRepository;
	
	@Autowired
	private CommitRepositoryIf commitRepository;
	
	@Transactional
	public Commit getOrCreateWorkingCommit(
			UUID perspectiveId, 
			UUID authorId) {
		
		/* Get or create the user workingCommit on this trail */
		Commit workingCommit = perspectiveRepository.findWorkingCommit(perspectiveId, authorId);
		
		if (workingCommit == null) {
			workingCommit = new Commit(appUserRepositoryIf.findById(authorId));
			workingCommit = commitRepository.save(workingCommit);
		}
		
		return workingCommit;
	}

}
