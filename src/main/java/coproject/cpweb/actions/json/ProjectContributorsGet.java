package coproject.cpweb.actions.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import coproject.cpweb.utils.db.entities.Project;
import coproject.cpweb.utils.db.entities.dtos.UsernameAndPps;

@Action("ProjectContributorsGet")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","^usernamesAndPps.*,ppsTot,^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class ProjectContributorsGet extends CpAction {
	
	private static final long serialVersionUID = 1L;
	
	/* Input parameters  */
	private String projectName;
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/* Output parameters  */
	private List<UsernameAndPps> usernamesAndPps = new ArrayList<UsernameAndPps>();
	private double ppsTot;
	
	public List<UsernameAndPps> getUsernamesAndPps() {
		return usernamesAndPps;
	}
	public void setUsernamesAndPps(List<UsernameAndPps> usernamesAndPps) {
		this.usernamesAndPps = usernamesAndPps;
	}
	public double getPpsTot() {
		return ppsTot;
	}
	public void setPpsTot(double ppsTot) {
		this.ppsTot = ppsTot;
	}
	
	/* Execute */
	public String execute() throws Exception  {
		
		Project project = dbServices.projectGet(projectName);
		
		usernamesAndPps = dbServices.projectContributorsAndPpsGet(project.getId());
		ppsTot = project.getPpsTot();
		
		return SUCCESS;
    }

}
