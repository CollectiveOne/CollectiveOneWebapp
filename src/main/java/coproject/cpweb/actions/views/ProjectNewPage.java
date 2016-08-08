package coproject.cpweb.actions.views;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

@Action("ProjectNewPage")
@ParentPackage("struts-default")
@Results({
    @Result(name="success", location="/views/ProjectNewPage/ProjectNewPage.jsp"),
})
public class ProjectNewPage extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	public String execute() throws Exception  {
    	return SUCCESS;
    }
}
