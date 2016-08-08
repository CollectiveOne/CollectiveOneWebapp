package coproject.cpweb.actions.json;

import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.BidState;
import coproject.cpweb.utils.db.entities.User;
import coproject.cpweb.utils.db.entities.dtos.UserDto;
import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("BidDone")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false",
    		"includeProperties","^fieldErrors.*,"
    				+ "res"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*,res"})
})
public class BidDone extends ActionSupport implements SessionAware {
	
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
	private int bidId;
	public int getBidId() {
		return bidId;
	}
	public void setBidId(int bidId) {
		this.bidId = bidId;
	}

	/* Output Json  */
	boolean res;
	
	public boolean isRes() {
		return res;
	}
	public void setRes(boolean res) {
		this.res = res;
	}

	public void validate() {
		
	}
	
	/* Execute */
	public String execute() throws Exception  {
    	
		UserDto userLoggedDtoSes = (UserDto) userSession.get("userLoggedDto");
		User creator = dbServices.bidGetCreator(bidId);
		
		if(creator.getId() != userLoggedDtoSes.getId()) {
			addFieldError("creator", " bid creator is not logged in");
			return SUCCESS;
		}
			
		dbServices.bidSaveState(bidId,BidState.DONE);
		
		res = true;

		return SUCCESS;
    }

	private Map<String,Object> userSession;
	
	public void setSession(Map<String, Object> session) {
		this.userSession = session;
	}
	

}
