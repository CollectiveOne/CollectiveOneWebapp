package coproject.cpweb.actions.json;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import coproject.cpweb.utils.db.entities.dtos.ActiveProject;
	
@Action("ActiveProjectChange")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json",params={ "ignoreHierarchy","false", "includeProperties","^resStatus.*, ^fieldErrors.*"}),
    @Result(name="input", type="json", params={ "ignoreHierarchy","false", "includeProperties","^fieldErrors.*"})
})
public class ActiveProjectsChange extends CpAction {
	
	private static final long serialVersionUID = 1L;
	
	/* Input Json  */
	private String projectName;
	private boolean active;
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	/* Validate */
	public void validate() {
		
	}
	
	/* Execute */
	@SuppressWarnings("unchecked")
	public String execute() throws Exception  {
    	
		List<ActiveProject> activeProjects = (List<ActiveProject>) getSession().get("activeProjects");
		
		resStatus.setSuccess(false);
		
		for(ActiveProject activeProject : activeProjects) {
			if(activeProject.getProjectName().equals(projectName)) {
				activeProject.setActive(active);
				if(active) { resStatus.setMsg(projectName+" is now active"); } 
				else { resStatus.setMsg(projectName+" is now deactivated"); }
				resStatus.setSuccess(true);
			}
		}
		
		if(!resStatus.getSuccess()) {
			resStatus.setMsg("problem setting "+projectName+" to active");
		}
		
		getSession().put("activeProjects",activeProjects);
		
		// Success/Failure logic is moved to client JS
		return SUCCESS;
    }


}
