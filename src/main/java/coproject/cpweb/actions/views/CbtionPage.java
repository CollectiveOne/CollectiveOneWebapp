package coproject.cpweb.actions.views;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

@Action("CbtionPage")
@ParentPackage("struts-default")
@Results({
    @Result(name="success", location="/views/CbtionPage/CbtionPage.jsp"),
})
public class CbtionPage extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	private int cbtionId;
	
	public int getCbtionId() {
		return cbtionId;
	}
	public void setCbtionId(int cbtionId) {
		this.cbtionId = cbtionId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String execute() throws Exception  {
    	return SUCCESS;
    }
}
