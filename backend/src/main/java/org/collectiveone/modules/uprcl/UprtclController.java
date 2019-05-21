package org.collectiveone.modules.uprcl;

import java.util.Arrays;
import java.util.List;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.uprcl.dtos.CommitDto;
import org.collectiveone.modules.uprcl.dtos.ContextDto;
import org.collectiveone.modules.uprcl.dtos.PerspectiveDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/1")
public class UprtclController extends BaseController { 
	
	@Autowired
	private UprtclService uprtclService;
		
	@RequestMapping(path = "/ctx", method = RequestMethod.POST)
	public PostResult createContext(
			@RequestBody List<ContextDto> contextDtos) throws Exception {
		
		List<String> contextIds = uprtclService.createContexts(contextDtos, getLoggedUserId());
		
		return new PostResult(
				"success", 
				"contex created", 
				contextIds);
	}
	
	@RequestMapping(path = "/ctx/{contextId}", method = RequestMethod.GET)
	public GetResult<ContextDto> getContext(
			@PathVariable("contextId") String contextId) throws Exception {
		
		return new GetResult<ContextDto>(
				"success", 
				"contex created", 
				uprtclService.getContextDto(contextId));
	}
	
	@RequestMapping(path = "/ctxId", method = RequestMethod.PUT)
	public GetResult<String> getContext(
			@RequestBody ContextDto contextDto) throws Exception {
		
		return new GetResult<String>(
				"success", 
				"contex created", 
				uprtclService.getContextId(contextDto));
	}
	
	@RequestMapping(path = "/ctxPersps", method = RequestMethod.GET)
	public GetResult<List<PerspectiveDto>> getContextPerspectives(
			@PathVariable("contextId") String contextId) throws Exception {
		
		return new GetResult<List<PerspectiveDto>>(
				"success", 
				"contex created", 
				uprtclService.getContextPerspectives(contextId));
	}
	
	@RequestMapping(path = "/persp", method = RequestMethod.POST)
	public PostResult createPerspective(
			@RequestBody List<PerspectiveDto> perspectiveDtos) throws Exception {
		
		List<String> perspectivesLinks = uprtclService.createPerspectives(perspectiveDtos, getLoggedUserId());
		
		return new PostResult(
				"success", 
				"perspective created", 
				perspectivesLinks);
	}
	
	@RequestMapping(path = "/persp/{perspectiveId}", method = RequestMethod.GET)
	public GetResult<PerspectiveDto> getPerspective(
			@PathVariable("perspectiveId") String perspectiveId) throws Exception {
		
		return new GetResult<PerspectiveDto>(
				"success", 
				"perspective created", 
				uprtclService.getPerspectiveDto(perspectiveId));
	}
	
	@RequestMapping(path = "/persp/{perspectiveId}", method = RequestMethod.PUT)
	public PostResult updatePerspective(
			@PathVariable("perspectiveId") String perspectiveId, 
			@RequestParam("headLink") String headLink) throws Exception {
		
		return new PostResult(
				"success", 
				"perspective created", 
				Arrays.asList(uprtclService.updatePerspective(perspectiveId, headLink)));
	}
	
	
	@RequestMapping(path = "/commit", method = RequestMethod.POST)
	public PostResult createCommit(
			@RequestBody List<CommitDto> commitDtos) throws Exception {
		
		List<String> commitLinks = uprtclService.createCommits(commitDtos, getLoggedUserId());
		
		return new PostResult(
				"success", 
				"commit created", 
				commitLinks);
	}
	
	@RequestMapping(path = "/commit/{commitId}", method = RequestMethod.GET)
	public GetResult<CommitDto> getCommit(
			@PathVariable("commitId") String commitId) throws Exception {
		
		return new GetResult<CommitDto>(
				"success", 
				"commit created", 
				uprtclService.getCommitDto(commitId));
	}
	
}	
