package org.collectiveone.modules.contexts;

import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.modules.contexts.dto.NewContextDto;
import org.collectiveone.modules.contexts.repositories.ContextRepositoryIf;
import org.collectiveone.modules.contexts.repositories.PerspectiveRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContextInnerService {
	
	@Autowired
	private ContextRepositoryIf contextRepository;
	
	@Autowired
	private PerspectiveRepositoryIf perspectiveRepository;
	
	/**
	 * create a brand new context and its default perspective
	 *  
	 * */
	@Transactional
	public Perspective createContext(NewContextDto contextDto, UUID creatorId) {
		
		Context context = new Context(contextDto.getTitle(), contextDto.getDescription());
		context = contextRepository.save(context);
		
		/* an empty perspective has a null head commit and no working commits */
		Perspective pespective = new Perspective("initial", context, null, null);
		pespective = perspectiveRepository.save(pespective);
		
		return pespective;
	}

}
