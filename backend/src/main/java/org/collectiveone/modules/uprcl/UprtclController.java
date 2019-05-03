package org.collectiveone.modules.uprcl;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.uprcl.dtos.PerspectiveDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/1")
public class UprtclController extends BaseController { 
	
	@Autowired
	private UprtclService uprtclService;
	
	
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
	@RequestMapping(path = "/p", method = RequestMethod.POST)
	public PostResult createPerspective(
			@RequestBody PerspectiveDto perspectiveDto) throws Exception {
		
		return new PostResult(
				"success", 
				"contex created", 
				uprtclService.createPerspective(perspectiveDto, getLoggedUserId()).getId());
	}
	
}	
