package coproject.cpweb.actions.json;

import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.dtos.ThesisDto;
import coproject.cpweb.utils.db.entities.dtos.UserDto;
import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("DecGetVote")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false",
    		"includeProperties","^fieldErrors.*,"
    				+ "thesisDto,"
    				+ "^thesisDto.*,"
    				+ "userLoggedDto,"
    				+ "^userLoggedDto.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*,res"})
})
public class DecGetVote extends ActionSupport implements SessionAware {
	
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
	private int decId;
	private int voterUserId;

	public int getDecId() {
		return decId;
	}
	public void setDecId(int decId) {
		this.decId = decId;
	}
	public int getVoterUserId() {
		return voterUserId;
	}
	public void setVoterUserId(int voterUserId) {
		this.voterUserId = voterUserId;
	}

	/* Output Json  */
	ThesisDto thesisDto;
	public ThesisDto getThesisDto() {
		return thesisDto;
	}
	public void setThesisDto(ThesisDto thesisDto) {
		this.thesisDto = thesisDto;
	}

	/* Validate */
	public void validate() {
		
	}
	
	/* Execute */
	public String execute() throws Exception  {
    	
		UserDto userLoggedDtoses = (UserDto) userSession.get("userLoggedDto");
		
		if(userLoggedDtoses == null) {
			addFieldError("userlogin", " cant get your vote if not logged in");
		} else {
			if(userLoggedDtoses.getId() == voterUserId) {
				/* only inform of the votes of the logged user */
				thesisDto = dbServices.thesisOfUser(decId, userLoggedDtoses.getId());
			}
		}
		return SUCCESS;
    }

	private Map<String,Object> userSession;
	
	public void setSession(Map<String, Object> session) {
		this.userSession = session;
	}
	

}
