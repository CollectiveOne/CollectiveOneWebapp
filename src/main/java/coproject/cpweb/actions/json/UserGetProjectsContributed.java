package coproject.cpweb.actions.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import coproject.cpweb.utils.db.entities.dtos.ProjectContributedDto;

@Action("UserGetProjectsContributed")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","^projectsContributedDtos.*,^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class UserGetProjectsContributed extends CpAction{
	
	private static final long serialVersionUID = 1L;
	
	
	/* Input parameters  */
	private String username;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	/* Output parameters  */
	private List<ProjectContributedDto> projectsContributedDtos = new ArrayList<ProjectContributedDto>();
	public List<ProjectContributedDto> getProjectsContributedDtos() {
		return projectsContributedDtos;
	}
	public void setProjectsContributedDtos(List<ProjectContributedDto> projectsContributedDtos) {
		this.projectsContributedDtos = projectsContributedDtos;
	}

	/* Execute */
	public String execute() throws Exception  {
    	
		projectsContributedDtos = dbServices.userProjectsContributedAndPpsGet(username);
		
     	return SUCCESS;
    }
	

}
