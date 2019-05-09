package org.collectiveone.modules.uprcl;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.GetResult;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.c1.data.dtos.LinkDto;
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
	
	
	@RequestMapping(path = "/p", method = RequestMethod.POST)
	public PostResult createPerspectiveWithParent(
			@RequestBody LinkDto linkDto) throws Exception {
		
		return new PostResult(
				"success", 
				"contex created", 
				uprtclService.createPerspective(linkDto, getLoggedUserId()).getId());
	}
	
	@RequestMapping(path = "/p/{perspectiveId}", method = RequestMethod.GET)
	public GetResult<PerspectiveDto> getPerspective(
			@PathVariable("perspectiveId") String perspectiveId) throws Exception {
		
		return new GetResult<PerspectiveDto>(
				"success", 
				"contex created", 
				uprtclService.getPerspective(perspectiveId, getLoggedUserId()));
	}
	
}	
