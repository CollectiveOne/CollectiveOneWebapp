package coproject.cpweb.actions.json;

import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.User;
import coproject.cpweb.utils.db.entities.dtos.UserDto;
import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("UserLogin")
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
public class UserLogin extends ActionSupport implements SessionAware {
	
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
	private User user = new User();
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	private UserDto userLoggedDto;
	
	public UserDto getUserLoggedDto() {
		return userLoggedDto;
	}

	public void setUserLoggedDto(UserDto userLoggedDto) {
		this.userLoggedDto = userLoggedDto;
	}

	/* Validate */
	public void validate() {
		
		if(user.getUsername().length() <= 0){
			addFieldError("user", "cant be empty");
		}
		
		if(user.getUsername().length() > 20){
			addFieldError("user", "too large (max 20 characters)");
		}
		
		if(user.getPassword().length() <= 0){
			addFieldError("password", "cant be empty");
		}
	}
	
	/* Execute */
	public String execute() throws Exception  {
    	
		if(!user.getUsername().equals("coprojects")) {
			userLoggedDto = dbServices.userLoginDto(user.getUsername(), user.getPassword() );
			
			if(userLoggedDto!=null) {
				userSession.put("userLoggedDto",userLoggedDto);
			} else {
				addFieldError("userlogin", " error");
			}
		}
		
		// Success/Failure logic is moved to client JS
		return SUCCESS;
    }

	private Map<String,Object> userSession;
	
	public void setSession(Map<String, Object> session) {
		this.userSession = session;
	}
	

}
