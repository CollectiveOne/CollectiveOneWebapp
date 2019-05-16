package org.collectiveone.modules.uprcl;

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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/1")
public class UprtclController extends BaseController { 
	
	@Autowired
	private UprtclService uprtclService;
		
	@RequestMapping(path = "/ctx", method = RequestMethod.POST)
	public PostResult createContext(
			@RequestBody ContextDto contextDto) throws Exception {
		
		String contextId = uprtclService.createContext(contextDto, getLoggedUserId()).getId();
		
		return new PostResult(
				"success", 
				"contex created", 
				contextId);
	}
	
	@RequestMapping(path = "/ctx/{contextId}", method = RequestMethod.GET)
	public GetResult<ContextDto> getContext(
			@PathVariable("contextId") String contextId) throws Exception {
		
		return new GetResult<ContextDto>(
				"success", 
				"contex created", 
				uprtclService.getContextDto(contextId));
	}
	
	@RequestMapping(path = "/persp", method = RequestMethod.POST)
	public PostResult createPerspective(
			@RequestBody PerspectiveDto perspectiveDto) throws Exception {
		
		String perspectiveId = uprtclService.createPerspective(perspectiveDto, getLoggedUserId()).getId();
		
		return new PostResult(
				"success", 
				"perspective created", 
				uprtclService.getLocalLink(perspectiveId).toString());
	}
	
	@RequestMapping(path = "/persp/{perspectiveId}", method = RequestMethod.GET)
	public GetResult<PerspectiveDto> getPerspective(
			@PathVariable("perspectiveId") String perspectiveId) throws Exception {
		
		return new GetResult<PerspectiveDto>(
				"success", 
				"perspective created", 
				uprtclService.getPerspectiveDto(perspectiveId));
	}
	
	@RequestMapping(path = "/commit", method = RequestMethod.POST)
	public PostResult createCommit(
			@RequestBody CommitDto commitDto) throws Exception {
		
		String commitId = uprtclService.createCommit(commitDto, getLoggedUserId()).getId();
		
		return new PostResult(
				"success", 
				"commit created", 
				uprtclService.getLocalLink(commitId).toString());
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
