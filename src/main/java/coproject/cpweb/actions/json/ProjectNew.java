package coproject.cpweb.actions.json;

import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.User;
import coproject.cpweb.utils.db.entities.dtos.ProjectDto;
import coproject.cpweb.utils.db.entities.dtos.UserDto;
import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("ProjectNew")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false",
    		"includeProperties","^fieldErrors.*,"
    				+ "res,"
    				+ "userLoggedDto,"
    				+ "^userLoggedDto.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*,res"})
})
public class ProjectNew extends ActionSupport implements SessionAware {
	
	private static final long serialVersionUID = 1L;
	
	/* Services  */
	DbServicesImp dbServices;
	
	public DbServicesImp getDbServices() {
		return dbServices;
	}

	public void setDbServices(DbServicesImp dbServices) {
		this.dbServices = dbServices;
	}
	
	/* Input Json  */
	private ProjectDto projectDto = new ProjectDto();
	
	public ProjectDto getProjectDto() {
		return projectDto;
	}
	public void setProjectDto(ProjectDto projectDto) {
		this.projectDto = projectDto;
	}
	
	/* Output Json  */
	boolean res;
	
	public boolean isRes() {
		return res;
	}
	public void setRes(boolean res) {
		this.res = res;
	}

	private UserDto userLoggedDto;
	
	public UserDto getUserLoggedDto() {
		return userLoggedDto;
	}

	public void setUserLoggedDto(UserDto userLoggedDto) {
		this.userLoggedDto = userLoggedDto;
	}
	
	public void validate() {
		
		addFieldError("project creation", "is currently disabled");
		
		if(projectDto.getName().contains(" ")) {
			addFieldError("project name", "cant include spaces");
		}
	}
	
	/* Execute */
	public String execute() throws Exception  {
    	
		UserDto userLoggedDtoses = (UserDto) userSession.get("userLoggedDto");
		User creator = dbServices.userGet(projectDto.getCreatorUsername());
		
		if(creator.getId() != userLoggedDtoses.getId()) {
			addFieldError("creator", " project creator is not logged in");
			return SUCCESS;
		}
		
		dbServices.projectCreate(projectDto);
		dbServices.projectStart(projectDto.getName());
		
		res = true;
		
		// updated logged user data with new projects and send it back as parameter
		userLoggedDto = dbServices.userGetDto(projectDto.getCreatorUsername());
		userSession.put("userLoggedDto",userLoggedDto);
		
		return SUCCESS;
    }

	private Map<String,Object> userSession;
	
	public void setSession(Map<String, Object> session) {
		this.userSession = session;
	}
	

}
