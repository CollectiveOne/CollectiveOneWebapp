package coproject.cpweb.actions.json;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.dtos.ProjectDto;
import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("ProjectGet")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","^projectDto.*,^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class ProjectGet extends ActionSupport{
	
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
	private String projectName;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	/* Output parameters  */
	private ProjectDto projectDto = new ProjectDto();

	public ProjectDto getProjectDto() {
		return projectDto;
	}

	public void setProjectDto(ProjectDto projectDto) {
		this.projectDto = projectDto;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/* Execute */
	public String execute() throws Exception  {
    	
		projectDto	= dbServices.projectGetDto(projectName);
		
     	return SUCCESS;
    }
	

}
