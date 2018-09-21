package org.collectiveone.modules.contexts;

import java.util.UUID;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.contexts.dto.NewCardDto;
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
	private PerspectiveOuterService trailService;

	/* 
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
		
		return trailService.stageCardWrapper(
				cardDto, 
				trailId, 
				getLoggedUserId(), 
				onCardWrapperId, 
				isBefore);
	}}
