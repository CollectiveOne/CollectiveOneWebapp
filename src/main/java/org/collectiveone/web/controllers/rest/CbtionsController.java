package org.collectiveone.web.controllers.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.collectiveone.model.User;
import org.collectiveone.services.CbtionServiceImp;
import org.collectiveone.services.CommentServiceImp;
import org.collectiveone.services.UserServiceImp;
import org.collectiveone.web.dto.CbtionDto;
import org.collectiveone.web.dto.CbtionDtoListRes;
import org.collectiveone.web.dto.CommentDto;
import org.collectiveone.web.dto.Filters;
import org.collectiveone.web.dto.ReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/1")
public class CbtionsController { // NO_UCD (unused code)
	
	@Autowired
	CbtionServiceImp cbtionService;
	
	@Autowired
	UserServiceImp userService;
	
	@Autowired
	CommentServiceImp commentService;
	
	@RequestMapping(value="/cbtions", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getList(@RequestBody Filters filters) {
		if(filters.getPage() == 0) filters.setPage(1);
		if(filters.getNperpage() == 0) filters.setNperpage(15);
		
		CbtionDtoListRes cbtionsDtosRes = cbtionService.getFilteredDto(filters);
		
		List<CbtionDto> cbtionDtos = cbtionsDtosRes.getCbtionsDtos();
		int[] resSet = cbtionsDtosRes.getResSet();
		
		Map<String,Object> map = new HashMap<>();
		
		map.put("cbtionDtos", cbtionDtos);
		map.put("resSet", resSet);
		
		return map;
	}

	@RequestMapping(value="/cbtion/{id}", method = RequestMethod.GET)
	public @ResponseBody CbtionDto get(@PathVariable Long id) {
		return cbtionService.getDto(id);
	}
	
	@Secured("ROLE_USER")
	@RequestMapping(value="/cbtion/{cbtionId}/promote", method = RequestMethod.PUT)
	public @ResponseBody Boolean promote(@PathVariable Long cbtionId, @RequestParam("up") boolean up) {
		/* creator is the logged user */
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User logged = userService.get(auth.getName());
		if(logged != null) {
			cbtionService.promote(cbtionId, logged.getId(), up);
		}
		return true;
	}
	
	@RequestMapping(value="/comment/{commentId}", method = RequestMethod.GET)
	public @ResponseBody CommentDto getComment(@PathVariable Long commentId) {
		CommentDto commentsDto = cbtionService.getCommentDto(commentId);
		return commentsDto;
	}
	
	@RequestMapping(value="/cbtion/{cbtionId}/comments", method = RequestMethod.GET)
	public @ResponseBody List<CommentDto> getComments(@PathVariable Long cbtionId) {
		List<CommentDto> commentsDtos = cbtionService.getCommentsDtos(cbtionId);
		return commentsDtos;
	}
	
	@Secured("ROLE_USER")
	@RequestMapping(value="/comment", method = RequestMethod.POST)
	public @ResponseBody Boolean commentNew(@RequestBody CommentDto commentDto) throws IOException {
		/* creator is the logged user */
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth.isAuthenticated()) {
			commentDto.setCreatorUsername(auth.getName());
			commentService.create(commentDto);
			return true;
		}
		return false;
	}
	
	@RequestMapping(value="/comment/{commentId}/replies", method = RequestMethod.GET)
	public @ResponseBody  List<CommentDto> commentNew(@PathVariable Long commentId) {
		return commentService.getRepliesDtos(commentId);
	}
	
	@Secured("ROLE_USER")
	@RequestMapping(value="/comment/{commentId}/promote", method = RequestMethod.PUT)
	public @ResponseBody Boolean commentPromote(@PathVariable Long commentId, @RequestParam("up") boolean up) {
		/* creator is the logged user */
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User logged = userService.get(auth.getName());
		if(logged != null) {
			commentService.promote(commentId, logged.getId(), up);
		}
		return true;
	}
	
	@RequestMapping(value="/cbtion/{cbtionId}/reviews", method = RequestMethod.GET)
	public @ResponseBody List<ReviewDto> getReviews(@PathVariable Long cbtionId) {
		return cbtionService.getReviewsDtos(cbtionId);
	}
	
	@Secured("ROLE_USER")
	@RequestMapping(value="/cbtion", method = RequestMethod.PUT)
	public @ResponseBody boolean edit(@RequestBody CbtionDto cbtionDto) {
		/* creator is the logged user */
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		cbtionDto.setCreatorUsername(auth.getName()); 
		cbtionService.edit(cbtionDto);
		return true;
	}
}
