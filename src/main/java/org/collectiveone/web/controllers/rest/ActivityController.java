package org.collectiveone.web.controllers.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.collectiveone.services.ActivityServiceImp;
import org.collectiveone.web.dto.ActivityDto;
import org.collectiveone.web.dto.ActivityDtoListRes;
import org.collectiveone.web.dto.Filters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/rest/activity")
public class ActivityController {
	
	@Autowired
	ActivityServiceImp activityService;
	
	@RequestMapping(value="/getList", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getList(@RequestBody Filters filters) {
		if(filters.getPage() == 0) filters.setPage(1);
		if(filters.getNperpage() == 0) filters.setNperpage(15);
		
		ActivityDtoListRes actDtosRes = activityService.getFilteredDto(filters);
		
		List<ActivityDto> actDtos = actDtosRes.getActivityDtos();
		int[] resSet = actDtosRes.getResSet();
		
		Map<String,Object> map = new HashMap<>();
		
		map.put("activityDtos", actDtos);
		map.put("resSet", resSet);
		
		return map;
	}
	
}
