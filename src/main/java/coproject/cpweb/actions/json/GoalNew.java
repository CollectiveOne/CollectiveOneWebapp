package coproject.cpweb.actions.json;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import coproject.cpweb.utils.db.entities.User;
import coproject.cpweb.utils.db.entities.dtos.GoalDto;
import coproject.cpweb.utils.db.entities.dtos.UserDto;

@Action("GoalNew")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false",
    		"includeProperties","^fieldErrors.*,"
    				+ "res,"
    				+ "^goalDto.*"
    				}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*,res"})
})
public class GoalNew extends CpAction {
	
	private static final long serialVersionUID = 1L;
	
	/* Input Json  */
	private GoalDto goalDtoIn = new GoalDto();
	public GoalDto getGoalDtoIn() {
		return goalDtoIn;
	}
	public void setGoalDtoIn(GoalDto goalDtoIn) {
		this.goalDtoIn = goalDtoIn;
	}

	/* Output Json  */
	private GoalDto goalDto = new GoalDto();
	public GoalDto getGoalDto() {
		return goalDto;
	}
	public void setGoalDto(GoalDto goalDto) {
		this.goalDto = goalDto;
	}

	public void validate() {
		if(goalDtoIn.getGoalTag().contains(" ")) {
			addFieldError("goalTag", "cant include spaces");
		}
	}
	
	/* Execute */
	public String execute() throws Exception  {
    	
		UserDto userLoggedDtoses = (UserDto) getSession().get("userLoggedDto");
		User creator = dbServices.userGet(goalDtoIn.getCreatorUsername());
		
		if(creator.getId() != userLoggedDtoses.getId()) {
			addFieldError("creator", " project creator is not logged in");
			return SUCCESS;
		}
			
		// create
		int goalId = dbServices.goalCreate(goalDtoIn);
		
		// load and return
		goalDto = dbServices.goalGetDto(goalId);
	
		return SUCCESS;
    }

}
