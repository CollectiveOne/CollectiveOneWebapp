package org.collectiveone.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.collectiveone.AbstractTest;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.activity.dto.ActivityDto;
import org.collectiveone.modules.conversations.MessageDto;
import org.collectiveone.modules.conversations.MessageService;
import org.collectiveone.modules.conversations.MessageThreadContextType;
import org.collectiveone.modules.governance.DecisionMakerRole;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.collectiveone.modules.initiatives.dto.MemberDto;
import org.collectiveone.modules.initiatives.dto.NewInitiativeDto;
import org.collectiveone.modules.initiatives.repositories.InitiativeRepositoryIf;
import org.collectiveone.modules.model.ModelScope;
import org.collectiveone.modules.model.ModelService;
import org.collectiveone.modules.model.dto.ModelCardDto;
import org.collectiveone.modules.model.dto.ModelCardWrapperDto;
import org.collectiveone.modules.model.dto.ModelSectionDto;
import org.collectiveone.modules.model.exceptions.WrongLinkOfElement;
import org.collectiveone.modules.users.AppUser;
import org.collectiveone.modules.users.AppUserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class TestModelService extends AbstractTest {
	
	@Autowired
    private ModelService modelService;
	
	@Autowired
    private AppUserService appUserService;
	
	@Autowired
    private InitiativeService initiativeService;
	
	@Autowired
    private MessageService messageService;
	
	@Autowired
	private InitiativeRepositoryIf initiativeRepository;
	
	@Value("${AUTH0ID}")
	String userAuth0Id;
	
	UUID userId;
	
	UUID initiativeId;
	
	UUID topLevelSectionId;
	
	UUID subsection1Id;
	
    @Before
    public void setUp() {
    	
    	PostResult result;
    	
    	/* create user */
    	AppUser user = appUserService.getOrCreateFromAuth0Id(userAuth0Id);
    	userId = user.getC1Id();
    	
    	/* create initiative */
    	NewInitiativeDto initiativeDto = new NewInitiativeDto();
    	initiativeDto.setName("Test Initiative");
    	initiativeDto.setDriver("test initiative");
    	
    	List<MemberDto> members = new ArrayList<MemberDto>();
    	
    	MemberDto member = new MemberDto();
    	
    	member.setUser(user.toDtoLight());
    	member.setRole(DecisionMakerRole.ADMIN.toString());
    	members.add(member);
    	
    	initiativeDto.setMembers(members);
    	
    	result = initiativeService.init(userId, initiativeDto);
    	
    	System.out.print("creating dummy section... ");
    	
    	initiativeId = UUID.fromString(result.getElementId());
    	
    	Initiative initiative = initiativeRepository.findById(initiativeId);
    	topLevelSectionId = initiative.getTopModelSubsection().getSection().getId(); 
    	
    	ModelSectionDto sectionDto = new ModelSectionDto();
    	
    	sectionDto.setDescription("test section");
    	sectionDto.setTitle("Test Section");
    	
    	result = modelService.createSection(sectionDto, topLevelSectionId, userId, null, false);
    	
    	subsection1Id = Str2UUID(result.getElementId());
    	
    	System.out.println("done!");
    }

    @After
    public void tearDown() {
        // clean up after each test method
    }
    
    private UUID Str2UUID(String str) {
    	try {
    		return UUID.fromString(str);
    	} catch (Exception e) {
    		return null;
    	}    	
    }
    
    @Test
    public void isCreateAndReorderCards() throws WrongLinkOfElement {
    	
    	PostResult result;
    	
    	/* add three cards without specifying location */
    	
    	ModelCardDto cardDto = new ModelCardDto();
    	
    	cardDto.setTitle("card common 1");
    	cardDto.setText("card common 1 - text");
    	cardDto.setNewScope(ModelScope.COMMON);
    	
    	result = modelService.createCardWrapper(
				cardDto, 
				topLevelSectionId, 
				userId, 
				null, 
				false,
				null);
    	
    	UUID commCardWrp0Id = Str2UUID(result.getElementId());
    	
    	cardDto.setTitle("card common 2");
    	cardDto.setText("card common 2 - text");
    	cardDto.setNewScope(ModelScope.COMMON);
    	
    	result = modelService.createCardWrapper(
				cardDto, 
				topLevelSectionId, 
				userId, 
				null, 
				false,
				null);
    	
    	UUID commCardWrp1Id = Str2UUID(result.getElementId());

    	cardDto.setTitle("card common 3");
    	cardDto.setText("card common 3 - text");
    	cardDto.setNewScope(ModelScope.COMMON);
    	
    	result = modelService.createCardWrapper(
				cardDto, 
				topLevelSectionId, 
				userId, 
				null, 
				false,
				null);
    	
    	UUID commCardWrp2Id = Str2UUID(result.getElementId());
    	
    	
    	/* get cards order */
    	
    	GetResult<ModelSectionDto> cardsResult = modelService.getSection(topLevelSectionId, null, 1, userId, false);
    	ModelSectionDto sectionDto = (ModelSectionDto) cardsResult.getData();
    	List<ModelCardWrapperDto> commonCards = sectionDto.getCardsWrappersCommon();
    	
    	assertTrue("wrong number of cards", commonCards.size() == 3);
    	
    	assertTrue("wrong card link", Str2UUID(commonCards.get(0).getAfterElementId()) == null);
    	assertTrue("wrong card link", Str2UUID(commonCards.get(0).getBeforeElementId()).equals(commCardWrp1Id));
    	
    	assertTrue("wrong card link", Str2UUID(commonCards.get(1).getAfterElementId()).equals(commCardWrp0Id));
    	assertTrue("wrong card link", Str2UUID(commonCards.get(1).getBeforeElementId()).equals(commCardWrp2Id));
    	
    	assertTrue("wrong card link", Str2UUID(commonCards.get(2).getAfterElementId()).equals(commCardWrp1Id));
    	assertTrue("wrong card link", Str2UUID(commonCards.get(2).getBeforeElementId()) == null);
    	
    	/* add shared card without specifying the location */
    	
    	cardDto.setTitle("card shared 0");
    	cardDto.setText("card shared 0 - text");
    	cardDto.setNewScope(ModelScope.SHARED);
    	
    	result = modelService.createCardWrapper(
				cardDto, 
				topLevelSectionId, 
				userId, 
				null, 
				false,
				null);
    	
    	UUID shCardWrp0Id = Str2UUID(result.getElementId());
    	
    	cardsResult = modelService.getSection(topLevelSectionId, null, 1, userId, false);
    	sectionDto = (ModelSectionDto) cardsResult.getData();
    	commonCards = sectionDto.getCardsWrappersCommon();
    	List<ModelCardWrapperDto> sharedCards = sectionDto.getCardsWrappersShared();
    	
    	assertTrue("wrong number of cards", commonCards.size() == 3);
    	assertTrue("wrong number of cards", sharedCards.size() == 1);
    	
    	
    	assertTrue("shared card shall be located after the last common card by default", 
    			Str2UUID(sharedCards.get(0).getAfterElementId()).equals(commCardWrp2Id));
    	
    	assertTrue("wrong card link", 
    			Str2UUID(sharedCards.get(0).getBeforeElementId()) == null);
    	
    	
    	assertTrue("wrong card link", 
    			Str2UUID(commonCards.get(2).getAfterElementId()).equals(commCardWrp1Id));
    	
    	assertTrue("last common card shall not be marked as being before the new shared card", 
    			Str2UUID(commonCards.get(2).getBeforeElementId()) == null);
    	
    	
    	/* add common card without specifying the location */
    	
    	cardDto.setTitle("card shared 4");
    	cardDto.setText("card shared 4 - text");
    	cardDto.setNewScope(ModelScope.COMMON);
    	
    	result = modelService.createCardWrapper(
				cardDto, 
				topLevelSectionId, 
				userId, 
				null, 
				false,
				null);
    	
    	UUID commCardWrp3Id = Str2UUID(result.getElementId());
    	
    	cardsResult = modelService.getSection(topLevelSectionId, null, 1, userId, false);
    	sectionDto = (ModelSectionDto) cardsResult.getData();
    	commonCards = sectionDto.getCardsWrappersCommon();
    	sharedCards = sectionDto.getCardsWrappersShared();
    	
    	assertTrue("wrong number of cards", commonCards.size() == 4);
    	assertTrue("wrong number of cards", sharedCards.size() == 1);
    	
    	
    	assertTrue("shared card shall be located after the same common card it was marked as originally", 
    			Str2UUID(sharedCards.get(0).getAfterElementId()).equals(commCardWrp2Id));
    	
    	assertTrue("shared card shall not be marked as being before any card", 
    			Str2UUID(sharedCards.get(0).getBeforeElementId()) == null);
    	
    	
    	assertTrue("wrong card link", 
    			Str2UUID(commonCards.get(2).getAfterElementId()).equals(commCardWrp1Id));
    	
    	assertTrue("third common card shall now be marked as before of new common card", 
    			Str2UUID(commonCards.get(2).getBeforeElementId()).equals(commCardWrp3Id));
    	
    	
    	assertTrue("new common card is the last one as shall be marked as after the previous last one. At this point two cards, one common and one shared are marked as being after the third common card.", 
    			Str2UUID(commonCards.get(3).getAfterElementId()).equals(commCardWrp2Id));
    	
    	assertTrue("third common card shall now be marked as the last common card", 
    			Str2UUID(commonCards.get(3).getBeforeElementId()) == null);
    	
    	
    	/* add shared card after the previously created shared card */
    	
    	cardDto.setTitle("card shared 1");
    	cardDto.setText("card shared 1 - text");
    	cardDto.setNewScope(ModelScope.SHARED);
    	
    	result = modelService.createCardWrapper(
				cardDto, 
				topLevelSectionId, 
				userId, 
				shCardWrp0Id, 
				false,
				null);
    	
    	UUID shCardWrp1Id = Str2UUID(result.getElementId());
    	
    	cardsResult = modelService.getSection(topLevelSectionId, null, 1, userId, false);
    	sectionDto = (ModelSectionDto) cardsResult.getData();
    	commonCards = sectionDto.getCardsWrappersCommon();
    	sharedCards = sectionDto.getCardsWrappersShared();
    	
    	assertTrue("wrong number of cards", commonCards.size() == 4);
    	assertTrue("wrong number of cards", sharedCards.size() == 2);
    	
    	
    	assertTrue("shared card shall be located after the same common card it was marked as originally", 
    			Str2UUID(sharedCards.get(0).getAfterElementId()).equals(commCardWrp2Id));
    	
    	assertTrue("shared card shall be new before the new shared card", 
    			Str2UUID(sharedCards.get(0).getBeforeElementId()).equals(shCardWrp1Id));
    	
    	
    	assertTrue("shared card shall be located after the other shared card", 
    			Str2UUID(sharedCards.get(1).getAfterElementId()).equals(shCardWrp0Id));
    	
    	assertTrue("shared card shall not be marked as being before any card", 
    			Str2UUID(sharedCards.get(1).getBeforeElementId()) == null);
    	
    	
    	/* add shared card before the first shared card */
    	
    	cardDto.setTitle("card shared 2");
    	cardDto.setText("card shared 2 - text");
    	cardDto.setNewScope(ModelScope.SHARED);
    	
    	result = modelService.createCardWrapper(
				cardDto, 
				topLevelSectionId, 
				userId, 
				shCardWrp0Id, 
				true,
				null);
    	
    	UUID shCardWrp2Id = Str2UUID(result.getElementId());
    	
    	cardsResult = modelService.getSection(topLevelSectionId, null, 1, userId, false);
    	sectionDto = (ModelSectionDto) cardsResult.getData();
    	commonCards = sectionDto.getCardsWrappersCommon();
    	sharedCards = sectionDto.getCardsWrappersShared();
    	
    	assertTrue("wrong number of cards", commonCards.size() == 4);
    	assertTrue("wrong number of cards", sharedCards.size() == 3);
    	
    	assertTrue("last added card should be the one marked as being after the common card", 
    			Str2UUID(sharedCards.get(2).getAfterElementId()).equals(commCardWrp2Id));
    	
    	assertTrue("shared card shall be new before the new shared card", 
    			Str2UUID(sharedCards.get(2).getBeforeElementId()).equals(shCardWrp0Id));
    	
    	
    	assertTrue("shared card shall be located after the other shared card", 
    			Str2UUID(sharedCards.get(0).getAfterElementId()).equals(shCardWrp2Id));
    	
    	assertTrue("shared card shall not be marked as being before any card", 
    			Str2UUID(sharedCards.get(0).getBeforeElementId()).equals(shCardWrp1Id));
    	
    	
    	assertTrue("shared card shall be located after the other shared card", 
    			Str2UUID(sharedCards.get(1).getAfterElementId()).equals(shCardWrp0Id));
    	
    	assertTrue("shared card shall not be marked as being before any card", 
    			Str2UUID(sharedCards.get(1).getBeforeElementId()) == null);
    	
    	
    	/* move the first shared card to be after the second instead of the third common card */
    	
    	result = modelService.moveCardWrapper(
    			topLevelSectionId, 
    			shCardWrp2Id, 
    			topLevelSectionId, 
    			commCardWrp1Id, 
    			false, 
    			userId);
    	
    	cardsResult = modelService.getSection(topLevelSectionId, null, 1, userId, false);
    	sectionDto = (ModelSectionDto) cardsResult.getData();
    	commonCards = sectionDto.getCardsWrappersCommon();
    	sharedCards = sectionDto.getCardsWrappersShared();
    	
    	assertTrue("wrong number of cards", commonCards.size() == 4);
    	assertTrue("wrong number of cards", sharedCards.size() == 3);
    	
    	assertTrue("shared card 2 must be now after common card 1", 
    			Str2UUID(sharedCards.get(2).getAfterElementId()).equals(commCardWrp1Id));
    	
    	assertTrue("shared card 2 must not be marked as before any other card", 
    			Str2UUID(sharedCards.get(2).getBeforeElementId()) == null);
    	
    	
    	assertTrue("shared card 0 shall now be located after the common card 2", 
    			Str2UUID(sharedCards.get(0).getAfterElementId()).equals(commCardWrp2Id));
    	
    	assertTrue("shared card 0 shall still be located before shared card 1", 
    			Str2UUID(sharedCards.get(0).getBeforeElementId()).equals(shCardWrp1Id));
    	
    	
    	assertTrue("shared card 1 shall be located after the shared card 0", 
    			Str2UUID(sharedCards.get(1).getAfterElementId()).equals(shCardWrp0Id));
    	
    	assertTrue("shared card 1 shall not be marked as being before any card", 
    			Str2UUID(sharedCards.get(1).getBeforeElementId()) == null);
    	
    	
    	assertTrue("common card 2 shall still be marked after common card 1", 
    			Str2UUID(commonCards.get(2).getAfterElementId()).equals(commCardWrp1Id));
    	
    	assertTrue("common card 2 shall still be marked before common card 3", 
    			Str2UUID(commonCards.get(2).getBeforeElementId()).equals(commCardWrp3Id));	
    	
    	
    	System.out.println(result.getResult());
    }
   
    @Test
    public void isCommentAndMoveCommentsAndConvertToCard() throws WrongLinkOfElement {
    	
    	/* add three cards without specifying location */
    	
    	MessageDto messageDto = new MessageDto();
    	String messageContent = "test message";
    	
    	messageDto.setText(messageContent);
    	messageDto.setUuidsOfMentions(new String[0]);
    	
    	messageService.postMessage(
    			messageDto, 
    			userId, 
    			MessageThreadContextType.MODEL_SECTION, 
    			subsection1Id,
    			null);
    	
    	
    	GetResult<Page<ActivityDto>> actResult;
    	
    	/* get messages */
    	PageRequest page = new PageRequest(0, 10);
    	
    	List<ActivityDto> actPage;
    	
    	actResult = modelService.getActivityResultUnderSection(
    			subsection1Id, 
    			page, 
    			true, 
    			false, 
    			1, 
    			userId);
    	
    	actPage = actResult.getData().getContent();
    	
    	assertTrue("wrong comment content", actPage.get(0).getMessage().getText().equals(messageContent));
    	
    	/* move comment to another section */
    	
    	UUID messageId = Str2UUID(actPage.get(0).getMessage().getId());
    	
    	messageService.moveMessage(
    			messageId,
    			topLevelSectionId,
    			MessageThreadContextType.MODEL_SECTION,
    			null);
    	
    	actResult = modelService.getActivityResultUnderSection(
    			topLevelSectionId, 
    			page, 
    			true, 
    			false, 
    			1, 
    			userId);
    	
    	actPage = actResult.getData().getContent();
    	
    	assertTrue("wrong comment content after moving", actPage.get(0).getMessage().getText().equals(messageContent));
    	
    
    	/* add a card wrapper and comment on it */
    	PostResult result;
    	
    	ModelCardDto cardDto = new ModelCardDto();
    	
    	cardDto.setTitle("card common 1");
    	cardDto.setText("card common 1 - text");
    	cardDto.setNewScope(ModelScope.COMMON);
    	
    	result = modelService.createCardWrapper(
				cardDto, 
				subsection1Id, 
				userId, 
				null, 
				false,
				null);
    	
    	UUID commCardWrp0Id = Str2UUID(result.getElementId());
    	
    	messageDto = new MessageDto();
    	String messageContent2 = "test message on card";
    	
    	messageDto.setText(messageContent2);
    	messageDto.setUuidsOfMentions(new String[0]);
    	
    	messageService.postMessage(
    			messageDto, 
    			userId, 
    			MessageThreadContextType.MODEL_CARD, 
    			commCardWrp0Id,
    			subsection1Id);
    	
    	actResult = modelService.getActivityResultUnderSection(
    			subsection1Id, 
    			page, 
    			true, 
    			false, 
    			1, 
    			userId);
    	
    	actPage = actResult.getData().getContent();
    	messageId = Str2UUID(actPage.get(0).getMessage().getId());
    	
    	assertTrue("wrong comment content", 
    			actPage.get(0).getMessage().getText().equals(messageContent2));
    	
    	/* move comment from card to section */
    	messageService.moveMessage(
    			messageId,
    			topLevelSectionId,
    			MessageThreadContextType.MODEL_SECTION,
    			null);
    	
    	actResult = modelService.getActivityResultUnderSection(
    			topLevelSectionId, 
    			page, 
    			true, 
    			false, 
    			1,
    			userId);
    	
    	actPage = actResult.getData().getContent();
    	messageId = Str2UUID(actPage.get(0).getMessage().getId());
    	
    	assertTrue("wrong comment content after moving to section", 
    			actPage.get(0).getMessage().getText().equals(messageContent2));
    	
    	
    	/* move comment from section back to card */
    	
    	messageService.moveMessage(
    			messageId,
    			commCardWrp0Id,
    			MessageThreadContextType.MODEL_CARD,
    			null);
    	
    	actResult = modelService.getActivityResultUnderCard(
    			commCardWrp0Id, 
    			page, 
    			true, 
    			false);
    	
    	actPage = actResult.getData().getContent();
    	messageId = Str2UUID(actPage.get(0).getMessage().getId());
    	
    	assertTrue("wrong comment content after moving it back to card", 
    			actPage.get(0).getMessage().getText().equals(messageContent2));
    	
    	
    	actResult = modelService.getActivityResultUnderSection(
    			subsection1Id, 
    			page, 
    			true, 
    			false, 
    			1,
    			userId);
    	
    	actPage = actResult.getData().getContent();
    	messageId = Str2UUID(actPage.get(0).getMessage().getId());
    	
    	assertTrue("wrong comment content after moving it back to card", 
    			actPage.get(0).getMessage().getText().equals(messageContent2));
    	
    	
    	/* convert comment to card */
    	
    	messageService.convertMessageToCard(
    			messageId,
    			subsection1Id,
    			userId);
    	
    	GetResult<ModelSectionDto> cardsResult = 
    			modelService.getSection(subsection1Id, null, 1, userId, false);
    	
    	ModelSectionDto sectionDto = (ModelSectionDto) cardsResult.getData();
    	List<ModelCardWrapperDto> commonCards = sectionDto.getCardsWrappersCommon();
    	
    	assertTrue("wrong number of cards", 
    			commonCards.size() == 2);
    	
    	assertTrue("card text is not the comment text", 
    			commonCards.get(1).getCard().getText().equals(messageContent2));
    	
    }
}
