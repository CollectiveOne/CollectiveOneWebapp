package org.collectiveone.service;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

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
    public void setUp() throws SQLException {
    	
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
    	
    	Initiative initiative = initiativeRepository.findById(initiativeId).get();
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
    	PageRequest page = PageRequest.of(0, 10);
    	
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
    
    @Test
    public void isCopyAndDetachSection() throws WrongLinkOfElement {
    	
    	PostResult result;
    	
    	/* create sections scheleton */
    	
    	ModelSectionDto s1_Dto = new ModelSectionDto();
    		
    	s1_Dto.setTitle("Test Origin Section s1");
    	s1_Dto.setDescription("test origin section s1");
    	
    	UUID s1Id = Str2UUID(
    			modelService.createSection(
	    			s1_Dto, 
	    			topLevelSectionId, 
	    			userId, 
	    			null, 
	    			false).getElementId());
    	
    	ModelSectionDto s11_Dto = new ModelSectionDto();
    	
    	s11_Dto.setTitle("Test Origin Section s11");
    	s11_Dto.setDescription("test origin section s11");
    	

    	UUID s11Id = Str2UUID(
    			modelService.createSection(
    				s11_Dto, 
	    			s1Id, 
	    			userId, 
	    			null, 
	    			false).getElementId());
    	
    	ModelSectionDto s12_Dto = new ModelSectionDto();
    	
    	s12_Dto.setTitle("Test Origin Section s12");
    	s12_Dto.setDescription("test origin section s12");
    	

    	UUID s12Id = Str2UUID(
    			modelService.createSection(
    				s12_Dto, 
	    			s1Id, 
	    			userId, 
	    			null, 
	    			false).getElementId());
    	
    	ModelSectionDto s121_Dto = new ModelSectionDto();
    	
    	s121_Dto.setTitle("Test Origin Section s121");
    	s121_Dto.setDescription("test origin section s121");
    	

    	UUID s121Id = Str2UUID(
    			modelService.createSection(
    				s121_Dto, 
	    			s12Id, 
	    			userId, 
	    			null, 
	    			false).getElementId());
    	
    	/* create cards in sections */
    	
    	ModelCardDto cardDto_s1 = new ModelCardDto();
    	
    	cardDto_s1.setTitle("card common s1");
    	cardDto_s1.setText("card common s1 - text");
    	cardDto_s1.setNewScope(ModelScope.COMMON);
    	
    	result = modelService.createCardWrapper(
    			cardDto_s1, 
				s1Id, 
				userId, 
				null, 
				false,
				null);
    	
    	UUID crdWrp_s1Id = Str2UUID(result.getElementId());
    	
    	ModelCardDto cardDto_s11 = new ModelCardDto();
    	
    	cardDto_s11.setTitle("card common s11");
    	cardDto_s11.setText("card common s11 - text");
    	cardDto_s11.setNewScope(ModelScope.COMMON);
    	
    	result = modelService.createCardWrapper(
    			cardDto_s11, 
				s11Id, 
				userId, 
				null, 
				false,
				null);
    	
    	UUID crdWrp_s11Id = Str2UUID(result.getElementId());
    	
    	ModelCardDto cardDto_s12 = new ModelCardDto();
    	
    	cardDto_s12.setTitle("card common s12");
    	cardDto_s12.setText("card common s12 - text");
    	cardDto_s12.setNewScope(ModelScope.COMMON);
    	
    	result = modelService.createCardWrapper(
    			cardDto_s12, 
				s12Id, 
				userId, 
				null, 
				false,
				null);
    	
    	UUID crdWrp_s12Id = Str2UUID(result.getElementId());
    	
    	ModelCardDto cardDto_s121 = new ModelCardDto();
    	
    	cardDto_s121.setTitle("card common s121");
    	cardDto_s121.setText("card common s121 - text");
    	cardDto_s121.setNewScope(ModelScope.COMMON);
    	
    	result = modelService.createCardWrapper(
    			cardDto_s121, 
				s121Id, 
				userId, 
				null, 
				false,
				null);
    	
    	UUID crdWrp_s121Id = Str2UUID(result.getElementId());
    	
    	/* add new subsection to top level section which is a detached 
    	 * copy of the s1 section */
    	
    	result = modelService.addSubsectionToSection (
    			s1Id, 
    			topLevelSectionId, 
    			null, 
    			false,
    			userId ,
    			ModelScope.COMMON,
    			true);
    	
    	UUID s1_cp_Id = Str2UUID(result.getElementId());
    	
    	assertTrue("error in addSubsectionToSection method:" + result.getMessage(),
    			result.getResult().equals("success"));
    	
    	/* get section with all subsections */
    	GetResult<ModelSectionDto> sectionResult = null;
    			
    	sectionResult = modelService.getSection(
    			s1_cp_Id, null, 999, userId, false);
    	
    	ModelSectionDto s1_Dto_cp_check = (ModelSectionDto) sectionResult.getData();
    	
    	assertTrue("ids are equal", 
    			!s1_Dto_cp_check.getId().equals(s1Id.toString()));
    	assertTrue("title dont match",
    			s1_Dto_cp_check.getTitle().equals("Test Origin Section s1"));
    	assertTrue("description dont match",
    			s1_Dto_cp_check.getDescription().equals("test origin section s1"));
    	assertTrue("wrong number of cards",
    			s1_Dto_cp_check.getCardsWrappersCommon().size() == 1);
    	
    	assertTrue("card id is the same",
    			!s1_Dto_cp_check.getCardsWrappersCommon().get(0).getId()
    			.equals(crdWrp_s1Id.toString()));
    	assertTrue("card title dont match",
    			s1_Dto_cp_check.getCardsWrappersCommon().get(0).getCard().getTitle()
    			.equals("card common s1"));
    	assertTrue("card description dont match",
    			s1_Dto_cp_check.getCardsWrappersCommon().get(0).getCard().getText()
    			.equals("card common s1 - text"));
    	
    	
    	assertTrue("wrong number of subsections",
    			s1_Dto_cp_check.getSubsectionsCommon().size() == 3);
    	
    	for (ModelSectionDto subsection_Dto : s1_Dto_cp_check.getSubsectionsCommon()) {
    		
    		if (subsection_Dto.getTitle().equals("Test Origin Section s11")) {
    			
    			assertTrue("ids are equal", 
    	    			!subsection_Dto.getId().equals(s11Id.toString()));
    			assertTrue("title dont match",
    					subsection_Dto.getTitle().equals("Test Origin Section s11"));
    	    	assertTrue("description dont match",
    	    			subsection_Dto.getDescription().equals("test origin section s11"));
    	    	assertTrue("wrong number of subsections",
    	    			subsection_Dto.getSubsectionsCommon().size() == 0);
    	    	
    	    	assertTrue("card id is the same",
    	    			!subsection_Dto.getCardsWrappersCommon().get(0).getId()
    	    			.equals(crdWrp_s11Id.toString()));
    	    	assertTrue("card title dont match",
    	    			subsection_Dto.getCardsWrappersCommon().get(0).getCard().getTitle()
    	    			.equals("card common s11"));
    	    	assertTrue("card description dont match",
    	    			subsection_Dto.getCardsWrappersCommon().get(0).getCard().getText()
    	    			.equals("card common s11 - text"));
    	    	
    		} else if (subsection_Dto.getTitle().equals("Test Origin Section s12")) {
    			
    			assertTrue("ids are equal", 
    	    			!subsection_Dto.getId().equals(s12Id.toString()));
    			assertTrue("title dont match",
    					subsection_Dto.getTitle().equals("Test Origin Section s12"));
    	    	assertTrue("description dont match",
    	    			subsection_Dto.getDescription().equals("test origin section s12"));
    	    	assertTrue("wrong number of subsections",
    	    			subsection_Dto.getSubsectionsCommon().size() == 1);
    	    	
    	    	assertTrue("card id is the same",
    	    			!subsection_Dto.getCardsWrappersCommon().get(0).getId()
    	    			.equals(crdWrp_s12Id.toString()));
    	    	assertTrue("card title dont match",
    	    			subsection_Dto.getCardsWrappersCommon().get(0).getCard().getTitle()
    	    			.equals("card common s12"));
    	    	assertTrue("card description dont match",
    	    			subsection_Dto.getCardsWrappersCommon().get(0).getCard().getText()
    	    			.equals("card common s12 - text"));
    	    	
    	    	for (ModelSectionDto subsubsection_Dto : subsection_Dto.getSubsectionsCommon()) {
    	    		
    	    		assertTrue("ids are equal", 
        	    			!subsubsection_Dto.getId().equals(s121Id.toString()));
    	    		assertTrue("title dont match",
    	    				subsubsection_Dto.getTitle().equals("Test Origin Section s121"));
        	    	assertTrue("description dont match",
        	    			subsubsection_Dto.getDescription().equals("test origin section s121"));
        	    	assertTrue("wrong number of subsections",
        	    			subsubsection_Dto.getSubsectionsCommon().size() == 0);
        	    	
        	    	assertTrue("card id is the same",
        	    			!subsubsection_Dto.getCardsWrappersCommon().get(0).getId()
        	    			.equals(crdWrp_s121Id.toString()));
        	    	assertTrue("card title dont match",
        	    			subsubsection_Dto.getCardsWrappersCommon().get(0).getCard().getTitle()
        	    			.equals("card common s121"));
        	    	assertTrue("card description dont match",
        	    			subsubsection_Dto.getCardsWrappersCommon().get(0).getCard().getText()
        	    			.equals("card common s121 - text"));
    	    		
    	    	}
    			
    		} else {
    			assertTrue("subsection with this title not found", false);
    		}
    	}
    }
}
