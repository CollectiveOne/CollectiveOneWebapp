package coproject.cpweb.actions.json;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import coproject.cpweb.utils.db.entities.dtos.UserDto;

@Action("GoalProposeParent")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false",
    		"includeProperties","^fieldErrors.*,"
    				+ "^resStatus.*"
    				}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*,res"})
})
public class GoalProposeParent extends CpAction {
	
	private static final long serialVersionUID = 1L;
	
	/* Input */
	private int goalId;
	private String parentTag;
	
	public int getGoalId() {
		return goalId;
	}
	public void setGoalId(int goalId) {
		this.goalId = goalId;
	}
	public String getParentTag() {
		return parentTag;
	}
	public void setParentTag(String parentTag) {
		this.parentTag = parentTag;
	}
	
	/* Output */

	public void validate() {
	}
	
	/* Execute */
	public String execute() throws Exception  {
    	
		UserDto userLoggedDtoses = (UserDto) getSession().get("userLoggedDto");
		
		if(userLoggedDtoses != null) {
			dbServices.goalProposeParent(goalId, parentTag);
			resStatus = dbServices.getStatus(); 
		}
	
		return SUCCESS;
    }

}
