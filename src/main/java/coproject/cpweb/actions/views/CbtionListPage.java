package coproject.cpweb.actions.views;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

@Action("CbtionListPage")
@ParentPackage("struts-default")
@Results({
    @Result(name="success", location="/views/CbtionListPage/CbtionListPage.jsp"),
})
public class CbtionListPage extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private String projectFilter;
	
	public String getProjectFilter() {
		return projectFilter;
	}

	public void setProjectFilter(String projectFilter) {
		this.projectFilter = projectFilter;
	}
	
	public String execute() throws Exception  {
    	return SUCCESS;
    }
}
