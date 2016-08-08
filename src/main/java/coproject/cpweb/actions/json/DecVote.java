package coproject.cpweb.actions.json;

import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.User;
import coproject.cpweb.utils.db.entities.dtos.UserDto;
import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("DecVote")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false",
    		"includeProperties","^fieldErrors.*,"
    				+ "res,"
    				+ "userLoggedDto,"
    				+ "^userLoggedDto.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*,res"})
})
public class DecVote extends ActionSupport implements SessionAware {
	
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
	private int vote;
	private int decId;

	public int getVote() {
		return vote;
	}
	public void setVote(int vote) {
		this.vote = vote;
	}
	public int getDecId() {
		return decId;
	}
	public void setDecId(int decId) {
		this.decId = decId;
	}

	/* Output Json  */
	String res;
	public String getRes() {
		return res;
	}
	public void setRes(String res) {
		this.res = res;
	}

	/* Validate */
	public void validate() {
		
	}
	
	/* Execute */
	public String execute() throws Exception  {
    	
		UserDto userLoggedDtoses = (UserDto) userSession.get("userLoggedDto");
		
		if(userLoggedDtoses == null) {
			addFieldError("userlogin", " error");
		} else {
			User userLogged = dbServices.userGet(userLoggedDtoses.getId());
			res = dbServices.thesisOfDecSave(userLogged, vote, decId);
		}
		
		return SUCCESS;
    }

	private Map<String,Object> userSession;
	
	public void setSession(Map<String, Object> session) {
		this.userSession = session;
	}
	

}
