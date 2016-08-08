package coproject.cpweb.actions.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("ProjectListGet")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","^projectList.*,^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class ProjectListGet extends ActionSupport{
	
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

	/* Output parameters  */
	private List<String> projectList = new ArrayList<String>();
	
	public List<String> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<String> projectList) {
		this.projectList = projectList;
	}

	/* Execute */
	public String execute() throws Exception  {
    	
		projectList = dbServices.projectGetList();
		
     	return SUCCESS;
    }
	

}
