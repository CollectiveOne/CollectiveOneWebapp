package coproject.cpweb.actions.views;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

@Action("GoalPage")
@ParentPackage("struts-default")
@Results({
    @Result(name="success", location="/views/GoalPage/GoalPage.jsp"),
})
public class GoalPage extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	private String goalTag;
	public String getGoalTag() {
		return goalTag;
	}
	public void setGoalTag(String goalTag) {
		this.goalTag = goalTag;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String execute() throws Exception  {
    	return SUCCESS;
    }
}
