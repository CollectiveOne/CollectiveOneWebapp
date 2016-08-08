package coproject.cpweb.actions.views;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

@Action("UserPage")
@ParentPackage("struts-default")
@Results({
    @Result(name="success", location="/views/UserPage/UserPage.jsp")
})
public class UserPage extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String execute() throws Exception  {
    	return SUCCESS;
    }
}
