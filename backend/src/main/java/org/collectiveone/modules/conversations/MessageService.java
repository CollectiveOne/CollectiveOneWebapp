package org.collectiveone.modules.conversations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.activity.ActivityService;
import org.collectiveone.modules.initiatives.repositories.InitiativeRepositoryIf;
import org.collectiveone.modules.model.ModelScope;
import org.collectiveone.modules.model.ModelService;
import org.collectiveone.modules.model.dto.ModelCardDto;
import org.collectiveone.modules.model.exceptions.WrongLinkOfElement;
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
	private ModelService modelService;
	
	@Autowired
	private MessageRepositoryIf messageRepository;
	
	@Autowired
	private AppUserRepositoryIf appUserRepository;
	
	@Autowired
	private InitiativeRepositoryIf initiativeRepository;
	
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
				
			case INITIATIVE:
				return initiativeRepository.findById(elementId).getId();
				
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
		
		if (messageDto.getText() == null || messageDto.getText().equals("")) {
			return new PostResult("error", "empty messages cannot be posted", "");		 		
		}
		
		AppUser author = appUserRepository.findByC1Id(authorId);
		Message message = messageDto.toEntity(messageDto, author);
		message = messageRepository.save(message);
		
		List<AppUser> mentionedUsers = new ArrayList<AppUser>();
		for(String uuid : messageDto.getUuidsOfMentions()) {
			AppUser appUser = appUserRepository.findByC1Id(UUID.fromString(uuid));
			mentionedUsers.add(appUser);
		}
		
		activityService.messagePosted(message, author, contextType, elementId, contextOfContextElementId, mentionedUsers);

		return new PostResult("success", "message posted", message.getId().toString());		 		
	}
	
	@Transactional
	public PostResult moveMessage(
			UUID messageId,
			UUID toElementId,
			MessageThreadContextType toContextType,
			UUID contextOfContextElementId) {
		
		/* message location is stored in the activity object */
		return activityService.moveMessage(
				messageId, 
				toElementId, 
				toContextType,
				contextOfContextElementId);
	}
	
	@Transactional
	public PostResult convertMessageToCard(
			UUID messageId,
			UUID sectionId,
			UUID userId) throws WrongLinkOfElement {
		
		Message message = messageRepository.findById(messageId);
		
		ModelCardDto cardDto = new ModelCardDto();
    	
    	cardDto.setText(message.getText());
    	cardDto.setNewScope(ModelScope.COMMON);
    	
		return modelService.createCardWrapper(
				cardDto, 
				sectionId, 
				message.getAuthor().getC1Id(), 
				null, 
				false,
				userId);
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
	
}
