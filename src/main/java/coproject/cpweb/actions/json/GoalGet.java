package coproject.cpweb.actions.json;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.dtos.GoalDto;
import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("GoalGet")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","^goalDto.*,^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class GoalGet extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	
	/* Services  */
	DbServicesImp dbServices;
	
	public DbServicesImp getDbServices() {
		return dbServices;
	}
	public void setDbServices(DbServicesImp dbServices) {
		this.dbServices = dbServices;
	}
	
	/* Input parameters  */
	private String goalTag;
	public String getGoalTag() {
		return goalTag;
	}
	public void setGoalTag(String goalTag) {
		this.goalTag = goalTag;
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
    	
		goalDto	= dbServices.goalGetDto(goalTag);
		
     	return SUCCESS;
    }
	

}
