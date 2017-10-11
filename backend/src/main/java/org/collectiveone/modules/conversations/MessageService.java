package org.collectiveone.modules.conversations;

import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.initiatives.repositories.InitiativeRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelCardWrapperRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelSectionRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelViewRepositoryIf;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
	
	@Autowired
	private MessageThreadRepositoryIf messageThreadRepository;
	
	@Autowired
	private MessageRepositoryIf messageRepository;
	
	@Autowired
	private AppUserRepositoryIf appUserRepository;
	
	@Autowired
	private InitiativeRepositoryIf initiativeRepository;
	
	@Autowired
	private ModelViewRepositoryIf modelViewRepository;
	
	@Autowired
	private ModelSectionRepositoryIf modelSectionRepository;
	
	@Autowired
	private ModelCardWrapperRepositoryIf modelCardWrapperRepository;


	@Transactional
	public UUID getInitiativeIdOfMessageDto(MessageDto messageDto) {
		
		MessageThreadContextType contextType = MessageThreadContextType.valueOf(messageDto.getContextType());
		
		switch (contextType) {
			case MODEL_CARD:
				return modelCardWrapperRepository.findById(UUID.fromString(messageDto.getContextElementId())).getInitiative().getId();
			
			case MODEL_SECTION:
				return modelSectionRepository.findById(UUID.fromString(messageDto.getContextElementId())).getInitiative().getId();
				
			case MODEL_VIEW:
				return modelViewRepository.findById(UUID.fromString(messageDto.getContextElementId())).getInitiative().getId();
				
			case INITIATIVE:
				return initiativeRepository.findById(UUID.fromString(messageDto.getContextElementId())).getId();
				
		}
		
		return null;
	}
	
	@Transactional
	public UUID getInitiativeIdOfMessageThread(UUID threadId) {
		MessageThread thread = messageThreadRepository.findById(threadId);
		
		switch (thread.getContextType()) {
		
			case MODEL_CARD:
				return thread.getModelCardWrapper().getInitiative().getId();
			
			case MODEL_SECTION:
				return thread.getModelSection().getInitiative().getId();
				
			case MODEL_VIEW:
				return thread.getModelView().getInitiative().getId();
				
			case INITIATIVE:
				return thread.getInitiative().getId();
				
		}
		
		return null;
		
	}
	
	@Transactional
	public PostResult postThreadAndMessage(MessageDto messageDto, UUID authorId) {
		
		Message message = messageDto.toEntity(messageDto, appUserRepository.findByC1Id(authorId));
		message = messageRepository.save(message);
		
		MessageThread thread = new MessageThread();
		thread = messageThreadRepository.save(thread);
		thread.getMessages().add(message);
		
		thread.setContextType(MessageThreadContextType.valueOf(messageDto.getContextType()));
		setThreadContext(thread, UUID.fromString(messageDto.getContextElementId()));
		
		message.setThread(thread);
		
		message = messageRepository.save(message);
		thread = messageThreadRepository.save(thread);
		
		return new PostResult("success", "message posted", message.getId().toString());		 		
		
	}
	
	@Transactional
	public PostResult postMessage(UUID threadId, MessageDto messageDto, UUID authorId) {
		
		Message message = messageDto.toEntity(messageDto, appUserRepository.findByC1Id(authorId));
		message = messageRepository.save(message);
		
		MessageThread thread = messageThreadRepository.findById(threadId);
		
		thread.getMessages().add(message);
		thread = messageThreadRepository.save(thread);
		
		return new PostResult("success", "message posted", message.getId().toString());		 
	}
	
	
	private void setThreadContext(MessageThread thread, UUID elementId) {
		
		switch (thread.getContextType()) {
			
			case MODEL_CARD:
				thread.setModelCardWrapper(modelCardWrapperRepository.findById(elementId));
			
			case MODEL_SECTION:
				thread.setModelSection(modelSectionRepository.findById(elementId));
				
			case MODEL_VIEW:
				thread.setModelView(modelViewRepository.findById(elementId));
				
			case INITIATIVE:
				thread.setInitiative(initiativeRepository.findById(elementId));
			
		}
		
	}
	
	
	
}
