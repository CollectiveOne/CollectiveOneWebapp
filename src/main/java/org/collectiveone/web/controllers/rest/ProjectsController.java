package org.collectiveone.web.controllers.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.collectiveone.model.Project;
import org.collectiveone.services.DbServicesImp;
import org.collectiveone.web.dto.ProjectContributorsDto;
import org.collectiveone.web.dto.ProjectDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/rest/projects")
public class ProjectsController {
	
	@Autowired
	DbServicesImp dbServices;
	
	@RequestMapping(value="/get/{projectName}", method = RequestMethod.POST)
	public @ResponseBody ProjectDto get(@PathVariable("projectName") String projectName) {
		return dbServices.projectGetDto(projectName);
	}
	
	@RequestMapping(value="/getContributors/{projectName}", method = RequestMethod.POST)
	public @ResponseBody ProjectContributorsDto getContributors(@PathVariable("projectName") String projectName) {
			
		Project project = dbServices.projectGet(projectName);
		
		ProjectContributorsDto projectContributorsDto = new ProjectContributorsDto();
		projectContributorsDto.setUsernamesAndPps(dbServices.projectContributorsAndPpsGet(project.getId()));
		projectContributorsDto.setPpsTot(project.getPpsTot());
		
		return projectContributorsDto;
	}
	
	@RequestMapping("/getList")
	public Map<String,Object> getList() {
		
		List<String> projectList = dbServices.projectGetList();
		
		Map<String,Object> map = new HashMap<>();
		map.put("projectList", projectList);
		
		return map;
	}
}
