package org.collectiveone.modules.contexts;

import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.modules.contexts.entities.Commit;
import org.collectiveone.modules.contexts.entities.Perspective;
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
	private PerspectiveRepositoryIf trailRepository;
	
	@Autowired
	private CommitRepositoryIf commitRepository;
	
	@Transactional
	public Commit getOrCreateWorkingCommit(
			UUID trailId, 
			UUID authorId) {
		
		/* Get or create the user workingCommit on this trail */
		Commit workingCommit = trailRepository.findWorkingCommit(trailId, authorId);
		
		if (workingCommit == null) {
			Perspective trail = trailRepository.findById(trailId);
			
			workingCommit = new Commit(appUserRepositoryIf.findById(authorId), trail, trail.getHead());
			workingCommit = commitRepository.save(workingCommit);
			
			trail.getWorkingCommits().add(workingCommit);
			trailRepository.save(trail);
		}
		
		return workingCommit;
	}

}
