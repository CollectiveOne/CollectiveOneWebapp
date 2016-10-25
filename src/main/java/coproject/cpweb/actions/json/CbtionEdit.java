package coproject.cpweb.actions.json;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import coproject.cpweb.utils.db.entities.User;
import coproject.cpweb.utils.db.entities.dtos.CbtionDto;
import coproject.cpweb.utils.db.entities.dtos.UserDto;

@Action("CbtionEdit")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false",
    		"includeProperties","^fieldErrors.*,"
    				+ "^cbtionDto.*,"
    				+ "^resStatus.*,"
    				}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*,res"})
})
public class CbtionEdit extends CpAction {
	
	private static final long serialVersionUID = 1L;
	
	/* Input Json  */
	private CbtionDto cbtionDtoIn = new CbtionDto();
	public CbtionDto getCbtionDtoIn() {
		return cbtionDtoIn;
	}
	public void setCbtionDtoIn(CbtionDto cbtionDto) {
		this.cbtionDtoIn = cbtionDto;
	}

	/* Output Json  */
	private CbtionDto cbtionDto = new CbtionDto();
	public CbtionDto getCbtionDto() {
		return cbtionDto;
	}
	public void setCbtionDto(CbtionDto cbtionDto) {
		this.cbtionDto = cbtionDto;
	}

	public void validate() {
		
	}
	
	/* Execute */
	public String execute() throws Exception  {
    	
		UserDto userLoggedDtoses = (UserDto) getSession().get("userLoggedDto");
		User creator = dbServices.cbtionGetCreator(cbtionDtoIn.getId());
		
		if(creator.getId() != userLoggedDtoses.getId()) {
			addFieldError("creator", " cbtion creator is not logged in");
			return SUCCESS;
		}
			
		// create
		int cbtionId = dbServices.cbtionEdit(cbtionDtoIn);
		resStatus = dbServices.getStatus();		
		
		// load and return
		cbtionDto = dbServices.cbtionGetDto(cbtionId);
	
		return SUCCESS;
    }

}
