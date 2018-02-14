package org.collectiveone.modules.model;

import java.util.UUID;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.activity.dto.ActivityDto;
import org.collectiveone.modules.governance.DecisionVerdict;
import org.collectiveone.modules.governance.GovernanceService;
import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.initiatives.InitiativeService;
import org.collectiveone.modules.model.dto.ModelCardDto;
import org.collectiveone.modules.model.dto.ModelCardWrapperDto;
import org.collectiveone.modules.model.dto.ModelDto;
import org.collectiveone.modules.model.dto.ModelSectionDto;
import org.collectiveone.modules.model.dto.ModelViewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/1")
public class ModelController extends BaseController { 
	
	@Autowired
	private ModelService modelService;

	@Autowired
	private GovernanceService governanceService;
	
	@Autowired
	private InitiativeService initiativeService;
	
	
	@RequestMapping(path = "/initiative/{initiativeId}/model", method = RequestMethod.GET) 
	public GetResult<ModelDto> getModel(
			@PathVariable("initiativeId") String initiativeIdStr,
			@RequestParam(defaultValue = "1") Integer level) {
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (!initiativeService.canAccess(initiativeId, getLoggedUserId())) {
			return new GetResult<ModelDto>("error", "access denied", null);
		}
		
		return modelService.getModel(initiativeId, level, getLoggedUserId());
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/view", method = RequestMethod.POST) 
	public PostResult createView(
			@PathVariable("initiativeId") String initiativeIdStr,
			@RequestBody ModelViewDto viewDto) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.createView(initiativeId, viewDto, getLoggedUser().getC1Id(), true);
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/view/{viewId}", method = RequestMethod.GET) 
	public GetResult<ModelViewDto> getView(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("viewId") String viewIdStr, 
			@RequestParam(defaultValue = "1") Integer level) {
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (!initiativeService.canAccess(initiativeId, getLoggedUserId())) {
			return new GetResult<ModelViewDto>("error", "access denied", null);
		}
		
		return modelService.getView(UUID.fromString(viewIdStr), getLoggedUserId(), level, getLoggedUserId());
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/view/{viewId}", method = RequestMethod.PUT) 
	public PostResult editView(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("viewId") String viewIdStr,
			@RequestBody ModelViewDto viewDto) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.editView(initiativeId, UUID.fromString(viewIdStr), viewDto, getLoggedUser().getC1Id());
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/view/{viewId}", method = RequestMethod.DELETE) 
	public PostResult deleteView(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("viewId") String viewIdStr) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.deleteView(UUID.fromString(viewIdStr), getLoggedUser().getC1Id());
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/view/{viewId}/subsection", method = RequestMethod.POST)
	public PostResult createViewSubsection(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("viewId") String viewIdStr,
			@RequestBody ModelSectionDto sectionDto) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canEditModel(initiativeId, getLoggedUserId()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.createSection(sectionDto, null, UUID.fromString(viewIdStr), getLoggedUserId(), true);
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/section/{sectionId}/subsection", method = RequestMethod.POST)
	public PostResult createSectionSubsection(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("sectionId") String sectionIdStr,
			@RequestBody ModelSectionDto sectionDto) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canEditModel(initiativeId, getLoggedUserId()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.createSection(sectionDto, UUID.fromString(sectionIdStr), null, getLoggedUserId(), true);
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/view/{viewId}/subsection/{subsectionId}", method = RequestMethod.PUT)
	public PostResult addExistingViewSubsection(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("viewId") String viewIdStr,
			@PathVariable("subsectionId") String subsectionIdStr) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canEditModel(initiativeId, getLoggedUserId()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.addSection(UUID.fromString(subsectionIdStr), null, UUID.fromString(viewIdStr), getLoggedUserId());
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/section/{sectionId}/subsection/{subsectionId}", method = RequestMethod.PUT)
	public PostResult addExistingSectionSubsection(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("sectionId") String sectionIdStr,
			@PathVariable("subsectionId") String subsectionIdStr) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canEditModel(initiativeId, getLoggedUserId()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.addSection(UUID.fromString(subsectionIdStr), UUID.fromString(sectionIdStr), null, getLoggedUserId());
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/section/{sectionId}", method = RequestMethod.PUT) 
	public PostResult editSection(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("sectionId") String sectionIdStr,
			@RequestBody ModelSectionDto sectionDto) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.editSection(UUID.fromString(sectionIdStr), sectionDto, getLoggedUser().getC1Id());
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/view/{viewId}/removeSection/{sectionId}", method = RequestMethod.PUT) 
	public PostResult removeExistingSectionFromView(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("viewId") String viewIdStr,
			@PathVariable("sectionId") String sectionIdStr) {
	
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.removeSubsectionFromView(UUID.fromString(viewIdStr), UUID.fromString(sectionIdStr), getLoggedUserId());
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/section/{sectionId}/removeSubsection/{subsectionId}", method = RequestMethod.PUT) 
	public PostResult removeExistingSubsection(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("sectionId") String sectionIdStr,
			@PathVariable("subsectionId") String subsectionIdStr) {
	
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.removeSubsectionFromSection(UUID.fromString(sectionIdStr), UUID.fromString(subsectionIdStr), getLoggedUserId());
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/section/{sectionId}/moveSubsection/{subsectionId}", method = RequestMethod.PUT) 
	public PostResult moveSectionSubsection(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("sectionId") String sectionIdStr,
			@PathVariable("subsectionId") String subsectionIdStr,
			@RequestParam(name = "onViewId", defaultValue="") String onViewIdStr,
			@RequestParam(name = "onSectionId", defaultValue="") String onSectionIdStr,
			@RequestParam(name = "onSubsectionId") String onSubsectionIdStr) {
	
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		/* dropped on view can be empty */
		UUID onViewId =  onViewIdStr.equals("") ? null : UUID.fromString(onViewIdStr);
		
		/* dropped on section can be empty */
		UUID onSectionId =  onSectionIdStr.equals("") ? null : UUID.fromString(onSectionIdStr);
		
		/* dropped on subsection can be empty */
		UUID onSubsectionId =  onSubsectionIdStr.equals("") ? null : UUID.fromString(onSubsectionIdStr);
		
		return modelService.moveSubsection(
				UUID.fromString(sectionIdStr), 
				UUID.fromString(subsectionIdStr),
				onViewId,
				onSectionId,
				onSubsectionId,
				getLoggedUserId());
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/view/{viewId}/moveSection/{sectionId}", method = RequestMethod.PUT) 
	public PostResult moveViewSection(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("viewId") String viewIdStr,
			@PathVariable("sectionId") String sectionIdStr,
			@RequestParam(name = "onViewSectionId", defaultValue = "") String onViewSectionIdStr,
			@RequestParam(name = "onSectionId", defaultValue = "") String onSectionIdStr,
			@RequestParam(name = "onSubsectionId", defaultValue = "") String beforeSubsectionIdStr){
	
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		/* dropped on viewSection can be empty*/
		UUID onViewSectionId =  onViewSectionIdStr.equals("") ? null : UUID.fromString(onViewSectionIdStr);
		
		/* dropped on section can be empty*/
		UUID onSectionId =  onSectionIdStr.equals("") ? null : UUID.fromString(onSectionIdStr);
		
		/* dropped on subsection can be empty*/
		UUID onSubsectionId =  beforeSubsectionIdStr.equals("") ? null : UUID.fromString(beforeSubsectionIdStr);
		
		return modelService.moveSection(
				UUID.fromString(viewIdStr), 
				UUID.fromString(sectionIdStr),
				onViewSectionId,
				onSectionId,
				onSubsectionId,
				getLoggedUserId());
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/moveView/{viewId}", method = RequestMethod.PUT) 
	public PostResult moveView(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("viewId") String viewIdStr,
			@RequestParam(name = "onViewId", defaultValue = "") String onViewIdStr){
	
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		/* dropped on viewSection can be empty*/
		UUID onViewId =  onViewIdStr.equals("") ? null : UUID.fromString(onViewIdStr);
		
		return modelService.moveView(
				initiativeId,
				UUID.fromString(viewIdStr), 
				onViewId,
				getLoggedUserId());
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/section/{sectionId}/cardWrapper/{cardWrapperId}", method = RequestMethod.PUT) 
	public PostResult addExistingCard(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("sectionId") String sectionIdStr,
			@PathVariable("cardWrapperId") String cardWrapperIdStr,
			@RequestParam(name = "beforeCardWrapperId", defaultValue="") String beforeCardWrapperIdStr) {
	
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		UUID beforeCardWrapperId = beforeCardWrapperIdStr.equals("") ? null : UUID.fromString(beforeCardWrapperIdStr);
		
		return modelService.addCardToSection(UUID.fromString(sectionIdStr), UUID.fromString(cardWrapperIdStr), beforeCardWrapperId, getLoggedUserId());
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/section/{sectionId}/removeCard/{cardWrapperId}", method = RequestMethod.PUT) 
	public PostResult removeExistingCard(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("sectionId") String sectionIdStr,
			@PathVariable("cardWrapperId") String cardWrapperIdStr) {
	
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.removeCardFromSection(UUID.fromString(sectionIdStr), UUID.fromString(cardWrapperIdStr), getLoggedUserId());
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/section/{sectionId}/moveCard/{cardWrapperId}", method = RequestMethod.PUT) 
	public PostResult moveExistingCard(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("sectionId") String fromSectionIdStr,
			@PathVariable("cardWrapperId") String cardWrapperIdStr,
			@RequestParam(name = "onSectionId") String onSectionIdStr,
			@RequestParam(name = "onCardWrapperId") String onCardWrapperIdStr) {
	
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		/* dropped on card can be empty */
		UUID onCardWrapperID =  null;
		if (!onCardWrapperIdStr.equals("")) {
			onCardWrapperID = UUID.fromString(onCardWrapperIdStr);
		}		
		
		return modelService.moveCardWrapper(
				UUID.fromString(fromSectionIdStr), 
				UUID.fromString(cardWrapperIdStr),
				UUID.fromString(onSectionIdStr),
				onCardWrapperID,
				getLoggedUserId());
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/section/{sectionId}", method = RequestMethod.GET) 
	public GetResult<ModelSectionDto> getSection(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("sectionId") String sectionIdStr,
			@RequestParam(defaultValue = "1") Integer level) {
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (!initiativeService.canAccess(initiativeId, getLoggedUserId())) {
			return new GetResult<ModelSectionDto>("error", "access denied", null);
		}
		
		return modelService.getSection(UUID.fromString(sectionIdStr), getLoggedUserId(), level, getLoggedUserId());
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/section/{sectionId}", method = RequestMethod.DELETE) 
	public PostResult deleteSection(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("sectionId") String sectionIdStr) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.deleteSection(UUID.fromString(sectionIdStr), getLoggedUser().getC1Id());
	}
	
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/section/{sectionId}/cardWrapper", method = RequestMethod.POST)
	public PostResult createCardWrapper(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("sectionId") String sectionIdStr,
			@RequestBody ModelCardDto cardDto) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.createCardWrapper(cardDto, UUID.fromString(sectionIdStr) ,getLoggedUser().getC1Id());
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/cardWrapper/{cardWrapperId}", method = RequestMethod.GET) 
	public GetResult<ModelCardWrapperDto> getCardWrapper(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("cardWrapperId") String cardWrapperIdStr) {
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (!initiativeService.canAccess(initiativeId, getLoggedUserId())) {
			return new GetResult<ModelCardWrapperDto>("error", "access denied", null);
		}
		
		return modelService.getCardWrapper(UUID.fromString(cardWrapperIdStr), getLoggedUserId());
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/cardWrapper/{cardWrapperId}", method = RequestMethod.PUT) 
	public PostResult editCardWrapper(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("cardWrapperId") String cardIdWrapperStr,
			@RequestBody ModelCardDto cardDto) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.editCardWrapper(initiativeId, UUID.fromString(cardIdWrapperStr), cardDto, getLoggedUser().getC1Id());
	}
	
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/cardWrapper/{cardWrapperId}", method = RequestMethod.DELETE) 
	public PostResult deleteCardWrapper(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("cardWrapperId") String cardIdWrapperStr) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (governanceService.canEditModel(initiativeId, getLoggedUser().getC1Id()) == DecisionVerdict.DENIED) {
			return new PostResult("error", "not authorized", "");
		}
		
		return modelService.deleteCardWrapper(UUID.fromString(cardIdWrapperStr), getLoggedUser().getC1Id());
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/cardWrapper/search", method = RequestMethod.GET) 
	public GetResult<Page<ModelCardWrapperDto>> searchCardWrapper(
			@PathVariable("initiativeId") String initiativeIdStr,
			@RequestParam("query") String query,
			@RequestParam("page") Integer page,
			@RequestParam("size") Integer size) {
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (!initiativeService.canAccess(initiativeId, getLoggedUserId())) {
			return new GetResult<Page<ModelCardWrapperDto>>("error", "access denied", null);
		}
		
		return modelService.searchCardWrapper(query, new PageRequest(page, size), initiativeId);
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/section/search", method = RequestMethod.GET) 
	public GetResult<Page<ModelSectionDto>> searchSection(
			@PathVariable("initiativeId") String initiativeIdStr,
			@RequestParam("query") String query,
			@RequestParam("page") Integer page,
			@RequestParam("size") Integer size) {
		
		UUID initiativeId = UUID.fromString(initiativeIdStr);
		
		if (!initiativeService.canAccess(initiativeId, getLoggedUserId())) {
			return new GetResult<Page<ModelSectionDto>>("error", "access denied", null);
		}
		
		return modelService.searchSection(query, new PageRequest(page, size), initiativeId, getLoggedUserId());
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/view/{viewId}/countMessages", method = RequestMethod.GET)
	public GetResult<Long> countMessagesUnderView(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("viewId") String viewIdStr, 
			@RequestParam(name="onlyMessages", defaultValue="false") Boolean onlyMessages) {
		
		UUID viewId = UUID.fromString(viewIdStr);
		
		Initiative initiative = modelService.getViewInitiative(viewId);
		
		if (!initiativeService.canAccess(initiative.getId(), getLoggedUserId())) {
			return new GetResult<Long>("error", "access denied", null);
		}
		
		return modelService.countMessagesUnderView(viewId);
	}
	
	@RequestMapping(path = "/activity/model/view/{viewId}", method = RequestMethod.GET)
	public GetResult<Page<ActivityDto>> getActivityUnderView(
			@PathVariable("viewId") String viewIdStr,
			@RequestParam("page") Integer page,
			@RequestParam("size") Integer size, 
			@RequestParam(name="onlyMessages", defaultValue="false") Boolean onlyMessages) {
		
		UUID viewId = UUID.fromString(viewIdStr);
		
		Initiative initiative = modelService.getViewInitiative(viewId);
		
		if (!initiativeService.canAccess(initiative.getId(), getLoggedUserId())) {
			return new GetResult<Page<ActivityDto>>("error", "access denied", null);
		}
		
		return modelService.getActivityResultUnderView(viewId, new PageRequest(page, size), onlyMessages);
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/section/{sectionId}/countMessages", method = RequestMethod.GET)
	public GetResult<Long> countMessagesUnderSection(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("sectionId") String sectionIdStr,
			@RequestParam(name="onlyMessages", defaultValue="false") Boolean onlyMessages) {
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		
		Initiative initiative = modelService.getSectionInitiative(sectionId);
		
		if (!initiativeService.canAccess(initiative.getId(), getLoggedUserId())) {
			return new GetResult<Long>("error", "access denied", null);
		}
		
		return modelService.countMessagesUnderSection(sectionId);
	}
	
	@RequestMapping(path = "/activity/model/section/{sectionId}", method = RequestMethod.GET)
	public GetResult<Page<ActivityDto>> getActivityUnderSection(
			@PathVariable("sectionId") String sectionIdStr,
			@RequestParam("page") Integer page,
			@RequestParam("size") Integer size, 
			@RequestParam(name="onlyMessages", defaultValue="false") Boolean onlyMessages) {
		
		UUID sectionId = UUID.fromString(sectionIdStr);
		
		Initiative initiative = modelService.getSectionInitiative(sectionId);
		
		if (!initiativeService.canAccess(initiative.getId(), getLoggedUserId())) {
			return new GetResult<Page<ActivityDto>>("error", "access denied", null);
		}
		
		return modelService.getActivityResultUnderSection(sectionId, new PageRequest(page, size), onlyMessages);
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/card/{cardWrapperId}/countMessages", method = RequestMethod.GET)
	public GetResult<Long> countMessagesUnderCard(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("cardWrapperId") String cardWrapperIdStr, 
			@RequestParam(name="onlyMessages", defaultValue="false") Boolean onlyMessages) {
		
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
		
		Initiative initiative = modelService.getCardWrapperInitiative(cardWrapperId);
		
		if (!initiativeService.canAccess(initiative.getId(), getLoggedUserId())) {
			return new GetResult<Long>("error", "access denied", null);
		}
		
		return modelService.countMessagesUnderCard(cardWrapperId, onlyMessages);
	}
	
	@RequestMapping(path = "/activity/model/card/{cardWrapperId}", method = RequestMethod.GET)
	public GetResult<Page<ActivityDto>> getActivityUnderCard(
			@PathVariable("cardWrapperId") String cardWrapperIdStr,
			@RequestParam("page") Integer page,
			@RequestParam("size") Integer size, 
			@RequestParam(name="onlyMessages", defaultValue="false") Boolean onlyMessages) {
		
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
		
		Initiative initiative = modelService.getCardWrapperInitiative(cardWrapperId);
		
		if (!initiativeService.canAccess(initiative.getId(), getLoggedUserId())) {
			return new GetResult<Page<ActivityDto>>("error", "access denied", null);
		}
		
		return modelService.getActivityResultUnderCard(cardWrapperId, new PageRequest(page, size), onlyMessages);
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/card/{cardWrapperId}/like", method = RequestMethod.PUT)
	public PostResult setCardLike(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("cardWrapperId") String cardWrapperIdStr,
			@RequestParam("likeStatus") Boolean likeStatus) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		// UUID initiativeId = UUID.fromString(initiativeIdStr);
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
		Initiative initiative = modelService.getCardWrapperInitiative(cardWrapperId);
		
		if (!initiativeService.isMemberOfEcosystem(initiative.getId(), getLoggedUserId())) {
			return new PostResult("error", "not authorized", "");
		}
				
		return modelService.setLikeToCard(cardWrapperId, getLoggedUserId(), likeStatus);
	}
	
	@RequestMapping(path = "/initiative/{initiativeId}/model/card/{cardWrapperId}/countLikes", method = RequestMethod.GET)
	public GetResult<Integer> countCardLikes(
			@PathVariable("initiativeId") String initiativeIdStr,
			@PathVariable("cardWrapperId") String cardWrapperIdStr, 
			@RequestParam(name="onlyMessages", defaultValue="false") Boolean onlyMessages) {
		
		UUID cardWrapperId = UUID.fromString(cardWrapperIdStr);
		
		Initiative initiative = modelService.getCardWrapperInitiative(cardWrapperId);
		
		if (!initiativeService.canAccess(initiative.getId(), getLoggedUserId())) {
			return new GetResult<Integer>("error", "access denied", null);
		}
		
		return modelService.countCardLikes(cardWrapperId);
	}
}
