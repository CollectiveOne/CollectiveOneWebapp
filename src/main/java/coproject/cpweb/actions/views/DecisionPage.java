package coproject.cpweb.actions.views;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

@Action("DecisionPage")
@ParentPackage("struts-default")
@Results({
    @Result(name="success", location="/views/DecisionPage/DecisionPage.jsp"),
})
public class DecisionPage extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	private int decisionId;
	public int getDecisionId() {
		return decisionId;
	}

	public void setDecisionId(int decisionId) {
		this.decisionId = decisionId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String execute() throws Exception  {
    	return SUCCESS;
    }
}
