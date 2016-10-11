package coproject.cpweb.actions.json;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import coproject.cpweb.utils.db.entities.dtos.UserDto;

@Action("UserLoggedGet")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json",params={"ignoreHierarchy","false","includeProperties","userLoggedDto,^userLoggedDto.*,^fieldErrors.*"}),
    @Result(name="input", type="json",params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class UserLoggedGet extends CpAction {
	
	private static final long serialVersionUID = 1L;
	
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
    	
		userLoggedDto = (UserDto) getSession().get("userLoggedDto"); 
		
		// Success/Failure logic is moved to client JS
		return SUCCESS;
    }

}
