package coproject.cpweb.actions.json;

import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("UserLogout")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", 
    		params={
    		"ignoreHierarchy","false",
    		"includeProperties","userLoggedDto,"
    				+ "^userLoggedDto.*,"
    				+ "^fieldErrors.*"}),
    @Result(name="input", type="json", 
			params={
			"ignoreHierarchy","false",
			"includeProperties",""
					+ ",^fieldErrors.*"})
})
public class UserLogout extends ActionSupport implements SessionAware {
	
	private static final long serialVersionUID = 1L;
	
	/* Services  */
	DbServicesImp dbServices;
	
	public DbServicesImp getDbServices() {
		return dbServices;
	}

	public void setDbServices(DbServicesImp dbServices) {
		this.dbServices = dbServices;
	}
	
	/* Validate */
	public void validate() {
	}
	
	/* Execute */
	public String execute() throws Exception  {
    	
		userSession.remove("userLoggedDto");
		userSession.remove("activeProjects");
		
		// Success/Failure logic is moved to client JS
		return SUCCESS;
    }

	private Map<String,Object> userSession;
	
	public void setSession(Map<String, Object> session) {
		this.userSession = session;
	}
	

}
