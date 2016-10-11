package coproject.cpweb.actions.json;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import coproject.cpweb.utils.db.entities.dtos.GoalDto;

@Action("GoalGet")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","^goalDto.*,^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class GoalGet extends CpAction {
	
	private static final long serialVersionUID = 1L;
	
	/* Input parameters  */
	private String goalTag;
	private String projectName;
	
	public String getGoalTag() {
		return goalTag;
	}
	public void setGoalTag(String goalTag) {
		this.goalTag = goalTag;
	}
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	/* Output parameters  */
	private GoalDto goalDto = new GoalDto();
	public GoalDto getGoalDto() {
		return goalDto;
	}
	public void setGoalDto(GoalDto goalDto) {
		this.goalDto = goalDto;
	}
	
	/* Execute */
	public String execute() throws Exception  {
    	
		goalDto	= dbServices.goalGetDto(goalTag,projectName);
		
     	return SUCCESS;
    }
	

}
