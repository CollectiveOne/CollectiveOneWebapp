package org.collectiveone.modules.contexts;

import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.modules.contexts.repositories.CommitRepositoryIf;
import org.collectiveone.modules.contexts.repositories.TrailRepositoryIf;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrailInnerService {
	
	@Autowired
	private AppUserRepositoryIf appUserRepositoryIf;
	
	@Autowired
	private TrailRepositoryIf trailRepository;
	
	@Autowired
	private CommitRepositoryIf commitRepository;
	
	@Transactional
	public Commit getOrCreateWorkingCommit(
			UUID trailId, 
			UUID authorId) {
		
		/* Get or create the user workingCommit on this trail */
		Commit workingCommit = trailRepository.findWorkingCommit(trailId, authorId);
		
		if (workingCommit == null) {
			Trail trail = trailRepository.findById(trailId);
			
			workingCommit = new Commit(appUserRepositoryIf.findByC1Id(authorId), trail, trail.getHead());
			workingCommit = commitRepository.save(workingCommit);
			
			trail.getWorkingCommits().add(workingCommit);
			trailRepository.save(trail);
		}
		
		return workingCommit;
	}

}
