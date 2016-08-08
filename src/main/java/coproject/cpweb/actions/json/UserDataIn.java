package coproject.cpweb.actions.json;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.dtos.ProjectDto;
import coproject.cpweb.utils.db.entities.dtos.UserDto;
import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("UserDataIn")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties",
    		"ppoints,"
    		+ "userDto,"
    		+ "^userDto.*,"
    		+ "projectDto,"
    		+ "^projectDto.*,"
    		+ "^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class UserDataIn extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	
	/* Services  */
	DbServicesImp dbServices;
	
	public DbServicesImp getDbServices() {
		return dbServices;
	}

	public void setDbServices(DbServicesImp dbServices) {
		this.dbServices = dbServices;
	}
	
	/* Input parameters  */
	private String username;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	private String projectName;
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/* Output parameters  */
	private UserDto userDto;
	private ProjectDto projectDto;
	private double ppoints;
	
	public double getPpoints() {
		return ppoints;
	}
	public void setPpoints(double ppoints) {
		this.ppoints = ppoints;
	}
	public UserDto getUserDto() {
		return userDto;
	}
	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}
	public ProjectDto getProjectDto() {
		return projectDto;
	}
	public void setProjectDto(ProjectDto projectDto) {
		this.projectDto = projectDto;
	}

	/* Execute */
	public String execute() throws Exception  {
    	
		userDto = dbServices.userGetDto(username);
		projectDto = dbServices.projectGetDto(projectName);
		ppoints = dbServices.userPpointsInProjectGet(userDto.getId(), projectDto.getId()) ;
		
     	return SUCCESS;
    }
	

}
