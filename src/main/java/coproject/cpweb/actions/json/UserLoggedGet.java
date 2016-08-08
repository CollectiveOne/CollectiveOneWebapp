package coproject.cpweb.actions.json;

import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.dtos.UserDto;
import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("UserLoggedGet")
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
public class UserLoggedGet extends ActionSupport implements SessionAware {
	
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
	private UserDto userLoggedDto;
	
	public UserDto getUserLoggedDto() {
		return userLoggedDto;
	}

	public void setUserLoggedDto(UserDto userLoggedDto) {
		this.userLoggedDto = userLoggedDto;
	}

	/* Validate */
	public void validate() {
		
	}
	
	/* Execute */
	public String execute() throws Exception  {
    	
		userLoggedDto = (UserDto) userSession.get("userLoggedDto"); 
		
		// Success/Failure logic is moved to client JS
		return SUCCESS;
    }

	private Map<String,Object> userSession;
	
	public void setSession(Map<String, Object> session) {
		this.userSession = session;
	}
	

}
