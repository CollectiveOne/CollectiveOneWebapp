package org.collectiveone.modules.contexts;

import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.modules.contexts.dto.ContextMetadataDto;
import org.collectiveone.modules.contexts.dto.PerspectiveDto;
import org.collectiveone.modules.contexts.entities.Commit;
import org.collectiveone.modules.contexts.entities.Context;
import org.collectiveone.modules.contexts.entities.ContextMetadata;
import org.collectiveone.modules.contexts.entities.Perspective;
import org.collectiveone.modules.contexts.entities.StageMetadata;
import org.collectiveone.modules.contexts.entities.UserDefaultPerspective;
import org.collectiveone.modules.contexts.repositories.CommitRepositoryIf;
import org.collectiveone.modules.contexts.repositories.ContextMetadataRepositoryIf;
import org.collectiveone.modules.contexts.repositories.ContextRepositoryIf;
import org.collectiveone.modules.contexts.repositories.PerspectiveRepositoryIf;
import org.collectiveone.modules.contexts.repositories.StageMetadataRepositoryIf;
import org.collectiveone.modules.contexts.repositories.UserDefaultPerspectiveRepositoryIf;
import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContextInnerService {
	
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
	private UserDefaultPerspectiveRepositoryIf userDefaultPerspectiveRepository;	
	
	@Autowired
	private AppUserRepositoryIf appUserRepository;
	
	
	
	
	
	/**
	 * create a brand new context and its default perspective
	 *  
	 * */
	@Transactional
	public Perspective createContext(UUID creatorId, ContextMetadataDto contextMetadataDto) {
		
		Context context = new Context();
		context = contextRepository.save(context);
		
		Perspective pespective = createPerspective(context, creatorId, contextMetadataDto);
		
		return pespective;
	}
	
	@Transactional
	public Perspective createPerspective(Context context, UUID creatorId, ContextMetadataDto contextMetadataDto) {
		
		Perspective perspective = new Perspective();
		perspective = perspectiveRepository.save(perspective);
		perspective.setContext(context);
		
		AppUser author = appUserRepository.findById(creatorId);
		
		Commit firstCommit = new Commit();
		firstCommit = commitRepository.save(firstCommit); 
				
		StageMetadata stageMetadata = new StageMetadata();
		ContextMetadata contextMetadata = new ContextMetadata();
		
		contextMetadata.setTitle(contextMetadataDto.getTitle());
		contextMetadata.setDescription(contextMetadataDto.getDescription());
		contextMetadata = contextMetadataRepository.save(contextMetadata);
		
		stageMetadata.setContextMetadata(contextMetadata);
		stageMetadata = stageMetadataRepository.save(stageMetadata);
		
		stageMetadata.setCommit(firstCommit);
		
		firstCommit.setPerspective(perspective);
		firstCommit.setAuthor(author);
		
		perspective.setHead(firstCommit);
		
		/* set this as the default perspective for this context for the author */ 
		UserDefaultPerspective defaultPerspective = new UserDefaultPerspective();
		defaultPerspective.setContext(context);
		defaultPerspective.setPerspective(perspective);
		defaultPerspective.setUser(author);
		
		userDefaultPerspectiveRepository.save(defaultPerspective);
		
		return perspective;
		
	}
	
	@Transactional
	public PerspectiveDto getPerspectiveDto(
			UUID perspectiveId, 
			UUID requestBy,
			Integer levels,
			Boolean addCards) {
		
		Perspective perspective = perspectiveRepository.findById(perspectiveId);
		
		if (levels > 0) {
			for (UUID subContext : perspectiveRepository.findSubperspectivesIds()) {
				
			}
		}
		
	}

}
