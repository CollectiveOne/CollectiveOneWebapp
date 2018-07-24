package org.collectiveone.modules.conversations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.activity.ActivityService;
import org.collectiveone.modules.model.ModelCardWrapper;
import org.collectiveone.modules.model.ModelSection;
import org.collectiveone.modules.model.repositories.ModelCardWrapperRepositoryIf;
import org.collectiveone.modules.model.repositories.ModelSectionRepositoryIf;
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
	private ModelSectionRepositoryIf modelSectionRepository;
	
	@Autowired
	private ModelCardWrapperRepositoryIf modelCardWrapperRepository;


	@Transactional
	public UUID getModelSectionIdOfMessage(MessageThreadContextType contextType, UUID elementId) {
		
		switch (contextType) {
			case MODEL_CARD:
				return modelCardWrapperRepository.findById(elementId).getModelsection().getId();
			
			case MODEL_SECTION:
				return modelSectionRepository.findById(elementId).getId();
				
				
		}
		
		return null;
	}
	
	@Transactional
	public UUID getModelSectionIdOfMessageThreadId(UUID threadId) {
		return getModelSectionIdOfMessageThread(messageThreadRepository.findById(threadId));
	}
	
	@Transactional
	public UUID getModelSectionIdOfMessageThread(MessageThread thread) {
		
		switch (thread.getContextType()) {
		
			case MODEL_CARD:
				return thread.getModelCardWrapper().getModelsection().getId();
			
			case MODEL_SECTION:
				return thread.getModelSection().getId();
			
				
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
			
				
		}
		
		return null;
		
	}
	
	@Transactional
	public PostResult postMessage(
			MessageDto messageDto, 
			UUID authorId, 
			MessageThreadContextType contextType, 
			UUID elementId,
			UUID contextOfContextElementId) {
		
		AppUser author = appUserRepository.findByC1Id(authorId);
		Message message = messageDto.toEntity(messageDto, author);
		message = messageRepository.save(message);
		
		MessageThread thread = getOrCreateThreadFromElementId(contextType, elementId);
		
		thread.getMessages().add(message);
		message.setThread(thread);
		
		message = messageRepository.save(message);
		thread = messageThreadRepository.save(thread);
		
		List<AppUser> mentionedUsers = new ArrayList<AppUser>();
		for(String uuid : messageDto.getUuidsOfMentions()) {
			AppUser appUser = appUserRepository.findByC1Id(UUID.fromString(uuid));
			mentionedUsers.add(appUser);
		}
		
		activityService.messagePosted(message, author, contextType, elementId, contextOfContextElementId, mentionedUsers);

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
				
			case INITIATIVE:
				return messageThreadRepository.findByInitiative_Id(elementId);
			
		}
		
		return null;
	}
	
}
