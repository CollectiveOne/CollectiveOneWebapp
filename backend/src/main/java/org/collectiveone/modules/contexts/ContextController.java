package org.collectiveone.modules.contexts;

import java.util.UUID;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.contexts.dto.ContextMetadataDto;
import org.collectiveone.modules.contexts.dto.NewCardDto;
import org.collectiveone.modules.contexts.dto.PerspectiveDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/1")
public class ContextController extends BaseController { 
	
	@Autowired
	private ContextOuterService contextService;
	
	@Autowired
	private PerspectiveOuterService perspectiveService;
	
	
	/**
	 * Creates a new context.
	 * 
	 * URLParams 
	 * - (optional) parentPerspectiveId=[UUID]. If not provided, the private perspective of the user is used.
	 * 
	 * DataParams
	 * 	Required
	 * 	- contextDto 
	 * 		{
	 * 			title: (required max 1024 length)[string],
	 * 			description: (optional)[string]
	 * 		} 
	 * */
	@RequestMapping(path = "/ctx", method = RequestMethod.POST)
	public PostResult createContext(
			@RequestBody ContextMetadataDto contextMetadataDto,
			@RequestParam(name="parentPerspectiveId", defaultValue="") UUID parentPerspectiveId,
			@RequestParam(name="beforePerspectiveId", defaultValue="") UUID beforePerspectiveId,
			@RequestParam(name="afterPerspectiveId", defaultValue="") UUID afterPerspectiveId) {
		
		if (parentPerspectiveId == null) {
			parentPerspectiveId = appUserService.getUserPerspectiveId(getLoggedUserId());
		}
		
		contextService.createContext(
				contextMetadataDto, 
				parentPerspectiveId, 
				getLoggedUserId(),
				beforePerspectiveId,
				afterPerspectiveId);
		
		return new PostResult("success", "new context staged", "");	
	}
	
	/**
	 * Returns the default perspective to the request author on a given context .
	 * 
	 * URLParams
	 *  
	 * - contextId: The id of the context (not of the perspective!) that is requested.
	 * 				The default perspective is retrieved. 
	 * 
	 * DataParams (Optional)
	 * 
	 * - levels: 	Number of levels of subcontext that are retrieved. 
	 * 		     	0: none, 1: first child, and so on
	 * 
	 * - addCards:  If true, the context (and subcontexts if levels > 0) 
	 * 				cards are included. If false, only the context metadata is
	 * 				included  
	 * 
	 * */
	@RequestMapping(path = "/ctx/{contextId}", method = RequestMethod.GET)
	public GetResult<PerspectiveDto> getContext(
			@PathVariable(name="contextId") UUID contextId,
			@RequestParam(name="levels", defaultValue="0") Integer levels,
			@RequestParam(name="addCards", defaultValue="false") Boolean addCards) {
		
		return contextService.getContext(contextId, getLoggedUserId(), levels, addCards);	
	}
	

	/** 
	 * Stages a new card wrapper on a trail.
	 * 
	 * The card wrapper is created on the working commit. if no working commit is found,
	 * one is created pointing to the current head of that trail.
	 * 
	 * Any user can create a personal working commit. Permissions are check
	 * when trying to push that commit as the head of the trail.
	 *  
	 * */
	
	@RequestMapping(path = "/tr/{trailId}/cardWrapper", method = RequestMethod.POST)
	public PostResult stageCardWrapper(
			@PathVariable("trailId") String trailIdStr,
			@RequestBody NewCardDto cardDto,
			@RequestParam(name="onCardWrapperId", defaultValue="") String onCardWrapperIdStr,
			@RequestParam(name = "isBefore", defaultValue="false") Boolean isBefore) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled users only", null);
		}
		
		UUID trailId = UUID.fromString(trailIdStr);
		UUID onCardWrapperId = onCardWrapperIdStr.equals("") ? null : UUID.fromString(onCardWrapperIdStr);
		
		return perspectiveService.stageCardWrapper(
				cardDto, 
				trailId, 
				getLoggedUserId(), 
				onCardWrapperId, 
				isBefore);
	}}
