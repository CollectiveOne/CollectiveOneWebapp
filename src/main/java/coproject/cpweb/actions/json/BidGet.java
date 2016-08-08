package coproject.cpweb.actions.json;

import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.dtos.BidDto;
import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("BidGet")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false",
    		"includeProperties","^fieldErrors.*,"
    				+ "res,"
    				+ "bidDto,"
    				+ "^bidDto.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*,res"})
})
public class BidGet extends ActionSupport implements SessionAware {
	
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
	BidDto bidDto;
	public BidDto getBidDto() {
		return bidDto;
	}

	public void setBidDto(BidDto bidDto) {
		this.bidDto = bidDto;
	}

	public void validate() {
		
	}
	
	/* Execute */
	public String execute() throws Exception  {
    	
		bidDto = dbServices.bidGetDto(bidId);
		
		return SUCCESS;
    }

	@SuppressWarnings("unused")
	private Map<String,Object> userSession;
	
	public void setSession(Map<String, Object> session) {
		this.userSession = session;
	}
	

}
