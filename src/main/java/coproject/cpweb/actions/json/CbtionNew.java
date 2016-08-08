package coproject.cpweb.actions.json;

import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.User;
import coproject.cpweb.utils.db.entities.dtos.CbtionDto;
import coproject.cpweb.utils.db.entities.dtos.UserDto;
import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("CbtionNew")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false",
    		"includeProperties","^fieldErrors.*,"
    				+ "res,"
    				+ "^cbtionDto.*"
    				}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*,res"})
})
public class CbtionNew extends ActionSupport implements SessionAware {
	
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
    	
		UserDto userLoggedDtoses = (UserDto) userSession.get("userLoggedDto");
		User creator = dbServices.userGet(cbtionDtoIn.getCreatorUsername());
		
		if(creator.getId() != userLoggedDtoses.getId()) {
			addFieldError("creator", " project creator is not logged in");
			return SUCCESS;
		}
			
		// create
		int cbtionId = dbServices.cbtionCreate(cbtionDtoIn);
		
		// load and return
		cbtionDto = dbServices.cbtionGetDto(cbtionId);
	
		return SUCCESS;
    }

	private Map<String,Object> userSession;
	
	public void setSession(Map<String, Object> session) {
		this.userSession = session;
	}
	

}
