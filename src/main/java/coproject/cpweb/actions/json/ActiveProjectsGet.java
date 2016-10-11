package coproject.cpweb.actions.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import coproject.cpweb.utils.db.entities.dtos.ActiveProject;
	
@Action("ActiveProjectsGet")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json",params={ "ignoreHierarchy","false", "includeProperties","activeProjects, ^activeProjects.*, ^fieldErrors.*"}),
    @Result(name="input", type="json", params={ "ignoreHierarchy","false", "includeProperties","^fieldErrors.*"})
})
public class ActiveProjectsGet extends CpAction {
	
	private static final long serialVersionUID = 1L;
	
	/* Input Json  */
	private List<ActiveProject> activeProjects;
	public List<ActiveProject> getActiveProjects() {
		return activeProjects;
	}
	public void setActiveProjects(List<ActiveProject> activeProjects) {
		this.activeProjects = activeProjects;
	}

	/* Validate */
	public void validate() {
		
	}
	
	/* Execute */
	@SuppressWarnings("unchecked")
	public String execute() throws Exception  {
    	
		activeProjects = (List<ActiveProject>) getSession().get("activeProjects");
		
		boolean rebuildActiveProjects = false;
		
		if(activeProjects == null) { rebuildActiveProjects = true;
		} else { if(activeProjects.size() == 0) { rebuildActiveProjects = true; } }
		
		if(rebuildActiveProjects) {
			/* fill active projects with all projects hosted and set to non active */
			activeProjects = new ArrayList<ActiveProject>();
			
			List<String> projectNames = dbServices.projectGetList();
			for(String projectName : projectNames) {
				ActiveProject activeProject = new ActiveProject();
				activeProject.setProjectName(projectName);
				activeProject.setActive(false);
				
				activeProjects.add(activeProject);
			}
			getSession().put("activeProjects",activeProjects);
		}
		
		// Success/Failure logic is moved to client JS
		return SUCCESS;
    }


}
