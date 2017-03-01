package org.collectiveone.web.controllers.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.collectiveone.services.ProjectServiceImp;
import org.collectiveone.web.dto.ActiveProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;


@RestController
@RequestMapping("/rest/session")
@SessionAttributes("activeProjects")
public class SessionController {
	
	@Autowired
	ProjectServiceImp projectService;
	
	@ModelAttribute("activeProjects")
	public List<ActiveProject> getActiveProjects () {
		
		List<ActiveProject> activeProjects = new ArrayList<ActiveProject>();
		List<String> projectNames = projectService.getFeaturedList();
		
		for(String projectName : projectNames) {
			ActiveProject activeProject = new ActiveProject();
			activeProject.setProjectName(projectName);
			activeProject.setActive(false);
			
			activeProjects.add(activeProject);
		}
		
        return activeProjects;
    }
	
	@RequestMapping(value="/activeProjectsGet", method = RequestMethod.POST)
	public List<ActiveProject> activeProjectsGet(@ModelAttribute("activeProjects") ArrayList<ActiveProject> activeProjects, HttpSession session) {
		return activeProjects;
	}
	
	@RequestMapping(value="/activeProjectChange", method = RequestMethod.POST)
	public List<ActiveProject> activeProjectsChange(
			@ModelAttribute("activeProjects") ArrayList<ActiveProject> activeProjects,
			@RequestBody ActiveProject activeProjectInput) {
		
		// TODO: when debugging, it seems that this method is being called five times per request!
		
		/* change the state of that active project in the activeProjects list in session*/
		for(ActiveProject activeProject : activeProjects) {
			if(activeProject.getProjectName().equals(activeProjectInput.getProjectName())) {
				activeProject.setActive(activeProjectInput.getActive());
			}
		}
		
		return activeProjects;
	}

}
