package coproject.cpweb.actions.json;

import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.User;
import coproject.cpweb.utils.db.entities.dtos.BidDto;
import coproject.cpweb.utils.db.entities.dtos.UserDto;
import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("BidNew")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false",
    		"includeProperties","^fieldErrors.*,"
    				+ "res,msg,"
    				+ "userLoggedDto,"
    				+ "^userLoggedDto.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*,res"})
})
public class BidNew extends ActionSupport implements SessionAware {
	
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
	private BidDto bidDto = new BidDto();
	
	public BidDto getBidDto() {
		return bidDto;
	}
	public void setBidDto(BidDto bidDto) {
		this.bidDto = bidDto;
	}

	/* Output Json  */
	boolean res;
	public boolean isRes() {
		return res;
	}
	public void setRes(boolean res) {
		this.res = res;
	}
	String msg;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	private UserDto userLoggedDto;
	
	public UserDto getUserLoggedDto() {
		return userLoggedDto;
	}

	public void setUserLoggedDto(UserDto userLoggedDto) {
		this.userLoggedDto = userLoggedDto;
	}
	
	public void validate() {
		
	}
	
	/* Execute */
	public String execute() throws Exception  {
    	
		UserDto userLoggedDtoSes = (UserDto) userSession.get("userLoggedDto");
		User creator = dbServices.userGet(bidDto.getCreatorDto().getUsername());
		
		if(creator.getId() != userLoggedDtoSes.getId()) {
			addFieldError("creator", " project creator is not logged in");
			return SUCCESS;
		}
			
		msg = dbServices.bidCreate(bidDto);
		res = true;
		
		// updated logged user data with new projects and send it back as parameter
		userLoggedDto = dbServices.userGetDto(creator.getUsername());
		userSession.put("userLoggedDto",userLoggedDto);
		
		return SUCCESS;
    }

	private Map<String,Object> userSession;
	
	public void setSession(Map<String, Object> session) {
		this.userSession = session;
	}
	

}
