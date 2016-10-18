package coproject.cpweb.actions.json;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import coproject.cpweb.utils.db.entities.User;
import coproject.cpweb.utils.db.entities.dtos.DecisionDto;
import coproject.cpweb.utils.db.entities.dtos.UserDto;
import coproject.cpweb.utils.db.services.DbServicesImp;

/**
 * @author pepo
 *
 */
@Action("DecisionNew")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false",
    		"includeProperties","^fieldErrors.*,"
    				+ "^decisionDto.*,"
    				+ "^resStatus.*"
    				}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*,res"})
})
public class DecisionNew extends CpAction {
	
	private static final long serialVersionUID = 1L;
	
	/* Services  */
	DbServicesImp dbServices;
	
	public DbServicesImp getDbServices() {
		return dbServices;
	}

	public void setDbServices(DbServicesImp dbServices) {
		this.dbServices = dbServices;
	}
	
	/* Input Json  */
	private DecisionDto decisionDtoIn = new DecisionDto();
	public DecisionDto getDecisionDtoIn() {
		return decisionDtoIn;
	}

	public void setDecisionDtoIn(DecisionDto decisionDtoIn) {
		this.decisionDtoIn = decisionDtoIn;
	}

	/* Output Data*/
	private DecisionDto decisionDto = new DecisionDto();
	public DecisionDto getDecisionDto() {
		return decisionDto;
	}
	public void setDecisionDto(DecisionDto decisionDto) {
		this.decisionDto = decisionDto;
	}

	/* Execute */
	public String execute() throws Exception  {
    	
		UserDto userLoggedDtoses = (UserDto) getSession().get("userLoggedDto");
		User creator = dbServices.userGet(decisionDtoIn.getCreatorUsername());
		
		if(creator.getId() != userLoggedDtoses.getId()) {
			addFieldError("creator", " decision creator is not logged in");
			return SUCCESS;
		}
			
		// create
		int id = dbServices.decisionCreate(decisionDtoIn);
		
		decisionDto = dbServices.decisionGetDto(id);
		resStatus = dbServices.getStatus();
		
		return SUCCESS;
    }

}
