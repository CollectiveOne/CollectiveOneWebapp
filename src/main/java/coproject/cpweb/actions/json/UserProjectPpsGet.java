package coproject.cpweb.actions.json;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import coproject.cpweb.utils.db.entities.dtos.ProjectContributedDto;

@Action("UserProjectPpsGet")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","^projectContributedDto.*,^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class UserProjectPpsGet extends CpAction{
	
	private static final long serialVersionUID = 1L;
		
	/* Input parameters  */
	private String username;
	private String projectName;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/* Output parameters  */
	private ProjectContributedDto projectContributedDto = new ProjectContributedDto();
	public ProjectContributedDto getProjectContributedDto() {
		return projectContributedDto;
	}
	public void setProjectContributedDto(ProjectContributedDto projectContributedDto) {
		this.projectContributedDto = projectContributedDto;
	}
	/* Execute */
	public String execute() throws Exception  {
    	
		projectContributedDto = dbServices.userProjectPpsGet(username,projectName);
		
     	return SUCCESS;
    }
	

}
