package org.collectiveone.session;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.collectiveone.model.User;
import org.collectiveone.services.UserServiceImp;
import org.collectiveone.web.dto.ActiveProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class LoggedUserListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> { // NO_UCD (unused code)

	@Autowired
	UserServiceImp userService;
	
	public static HttpSession session() {
	    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    return attr.getRequest().getSession(true); // true == allow create
	}
		
    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event)
    {
        @SuppressWarnings("unchecked")
		List<ActiveProject> activeProjects = (List<ActiveProject>) session().getAttribute("activeProjects");
		
        if(activeProjects == null) activeProjects = new ArrayList<ActiveProject>();
        activeProjects.clear();
        
        User logged = userService.get(event.getAuthentication().getName());
        List<String> projectsStarredNames = userService.getProjectsStarredNames(logged.getId());
        
        for(String projectName : projectsStarredNames) {
        	activeProjects.add(new ActiveProject(projectName, false)); 
        }
        
        session().setAttribute("activeProjects",activeProjects);
    }
}




