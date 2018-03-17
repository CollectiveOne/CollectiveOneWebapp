package org.collectiveone.modules.conversations;

import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.activity.ActivityService;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.repositories.InitiativeRepositoryIf;
import org.collectiveone.modules.model.ModelCardWrapper;
import org.collectiveone.modules.model.ModelSection;
import org.collectiveone.modules.model.ModelView;
import org.collectiveone.modules.model.repositories.ModelCardWrapperRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelSectionRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelViewRepositoryIf;
import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
	
	@Autowired
	private ActivityService activityService;
	
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
	public UUID getInitiativeIdOfMessage(MessageThreadContextType contextType, UUID elementId) {
		
		switch (contextType) {
			case MODEL_CARD:
				return modelCardWrapperRepository.findById(elementId).getInitiative().getId();
			
			case MODEL_SECTION:
				return modelSectionRepository.findById(elementId).getInitiative().getId();
				
			case MODEL_VIEW:
				return modelViewRepository.findById(elementId).getInitiative().getId();
				
			case INITIATIVE:
				return initiativeRepository.findById(elementId).getId();
				
		}
		
		return null;
	}
	
	@Transactional
	public UUID getInitiativeIdOfMessageThreadId(UUID threadId) {
		return getInitiativeIdOfMessageThread(messageThreadRepository.findById(threadId));
	}
	
	@Transactional
	public UUID getInitiativeIdOfMessageThread(MessageThread thread) {
		
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
	public UUID getElementIdOfMessageThread(MessageThread thread) {
		
		switch (thread.getContextType()) {
		
			case MODEL_CARD:
				return thread.getModelCardWrapper().getId();
			
			case MODEL_SECTION:
				return thread.getModelSection().getId();
				
			case MODEL_VIEW:
				return thread.getModelView().getId();
				
			case INITIATIVE:
				return thread.getInitiative().getId();
				
		}
		
		return null;
		
	}
	
	@Transactional
	public PostResult postMessage(MessageDto messageDto, UUID authorId, MessageThreadContextType contextType, UUID elementId) {
		
		AppUser author = appUserRepository.findByC1Id(authorId);
		Message message = messageDto.toEntity(messageDto, author);
		message = messageRepository.save(message);
		
		MessageThread thread = getOrCreateThreadFromElementId(contextType, elementId);
		
		thread.getMessages().add(message);
		message.setThread(thread);
		
		message = messageRepository.save(message);
		thread = messageThreadRepository.save(thread);
		
		activityService.messagePosted(message, author, contextType, elementId);

		return new PostResult("success", "message posted", message.getId().toString());		 		
	}
	
	@Transactional
	public PostResult editMessage(MessageDto messageDto, UUID editorId, UUID messageId) {
		
		Message message = messageRepository.findById(messageId);
		
		if (message == null) {
			return new PostResult("error", "message not found", null);		 		
		}
		
		/* only author can edit a message */
		if (!message.getAuthor().getC1Id().equals(editorId)) {
			return new PostResult("error", "only author can edit a message", null);
		}
		
		message.setText(messageDto.getText());
		message = messageRepository.save(message);
		
		return new PostResult("success", "message edited", message.getId().toString());		 		
	}
		
	
	@Transactional
	private MessageThread getOrCreateThreadFromElementId(MessageThreadContextType contextType, UUID elementId) {
		MessageThread thread = getThreadFromElementId(contextType, elementId);
		
		if (thread == null) {
			thread = new MessageThread();
			thread.setContextType(contextType);
			thread = messageThreadRepository.save(thread);
			
			switch (contextType) {
				case MODEL_CARD:
					ModelCardWrapper card = modelCardWrapperRepository.findById(elementId);
					thread.setModelCardWrapper(card);
					card.setMessageThread(thread);
					modelCardWrapperRepository.save(card);
					break;
				
				case MODEL_SECTION:
					ModelSection section = modelSectionRepository.findById(elementId);
					thread.setModelSection(section);
					section.setMessageThread(thread);
					modelSectionRepository.save(section);
					break;
					
				case MODEL_VIEW:
					ModelView view = modelViewRepository.findById(elementId);
					thread.setModelView(view);
					view.setMessageThread(thread);
					modelViewRepository.save(view);
					break;
					
				case INITIATIVE:
					Initiative initiative = initiativeRepository.findById(elementId);
					thread.setInitiative(initiative);
					initiative.setMessageThread(thread);
					initiativeRepository.save(initiative);
					break;
			}
		}
		
		return thread;
	}
	
	@Transactional
	private MessageThread getThreadFromElementId(MessageThreadContextType contextType, UUID elementId) {
		switch (contextType) {
		
			case MODEL_CARD:
				return messageThreadRepository.findByModelCardWrapper_Id(elementId);
			
			case MODEL_SECTION:
				return messageThreadRepository.findByModelSection_Id(elementId);
				
			case MODEL_VIEW:
				return messageThreadRepository.findByModelView_Id(elementId);
				
			case INITIATIVE:
				return messageThreadRepository.findByInitiative_Id(elementId);
			
		}
		
		return null;
	}
	
}
