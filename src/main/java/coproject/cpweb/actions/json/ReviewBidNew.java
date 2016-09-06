package coproject.cpweb.actions.json;

import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.User;
import coproject.cpweb.utils.db.entities.dtos.ReviewDto;
import coproject.cpweb.utils.db.entities.dtos.UserDto;
import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("ReviewBidNew")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false",
    		"includeProperties","^fieldErrors.*,"
    				+ "res,msg"
    				}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*,res"})
})
public class ReviewBidNew extends ActionSupport implements SessionAware {
	
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
	private ReviewDto reviewDto = new ReviewDto();
	public ReviewDto getReviewDto() {
		return reviewDto;
	}
	public void setReviewDto(ReviewDto reviewDto) {
		this.reviewDto = reviewDto;
	}
	
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
	String msg;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void validate() {
	}
	
	/* Execute */
	public String execute() throws Exception  {
    	
		UserDto userLoggedDtoSes = (UserDto) userSession.get("userLoggedDto");
		User creator = dbServices.userGet(reviewDto.getCreatorUsername());
		
		if(creator.getId() != userLoggedDtoSes.getId()) {
			addFieldError("creator", " review creator is not logged in");
			return SUCCESS;
		}
			
		msg = dbServices.reviewBidCreate(reviewDto, bidId);
		res = true;
		
		return SUCCESS;
    }

	private Map<String,Object> userSession;
	
	public void setSession(Map<String, Object> session) {
		this.userSession = session;
	}
	

}
