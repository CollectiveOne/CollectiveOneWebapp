package org.collectiveone.modules.uprcl;

import org.collectiveone.common.BaseController;
import org.collectiveone.common.dto.PostResult;
import org.collectiveone.modules.c1.data.dtos.LinkDto;
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
	 * Content 
	 * - (required) perspectiveDto JSON (see https://github.com/uprtcl/spec/blob/master/schema.gql) 
	 * 		 
	 * */
	@RequestMapping(path = "/p", method = RequestMethod.POST)
	public PostResult createPerspectiveWithParent(
			@RequestBody LinkDto linkDto) throws Exception {
		
		return new PostResult(
				"success", 
				"contex created", 
				uprtclService.createPerspective(linkDto, getLoggedUserId()).getId());
	}
	
}	
