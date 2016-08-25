package coproject.cpweb.actions.views;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

@Action("GoalNewPage")
@ParentPackage("struts-default")
@Results({
    @Result(name="success", location="/views/GoalNewPage/GoalNewPage.jsp"),
})
public class GoalNewPage extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	public String execute() throws Exception  {
    	return SUCCESS;
    }
}
