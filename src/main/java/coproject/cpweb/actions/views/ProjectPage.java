package coproject.cpweb.actions.views;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

@Action("ProjectPage")
@ParentPackage("struts-default")
@Results({
    @Result(name="success", location="/views/ProjectPage/ProjectPage.jsp"),
})
public class ProjectPage extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	private String projectName;
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String execute() throws Exception  {
    	return SUCCESS;
    }
}
