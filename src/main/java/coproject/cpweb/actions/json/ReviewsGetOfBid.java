package coproject.cpweb.actions.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.dtos.ReviewDto;
import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("ReviewsGetOfBid")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","^reviewDtos.*,^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class ReviewsGetOfBid extends ActionSupport{
	
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
	private int bidId;
	public int getBidId() {
		return bidId;
	}
	public void setBidId(int bidId) {
		this.bidId = bidId;
	}

	/* Output parameters  */
	private List<ReviewDto> reviewDtos = new ArrayList<ReviewDto>();
	public List<ReviewDto> getReviewDtos() {
		return reviewDtos;
	}
	public void setReviewDtos(List<ReviewDto> reviewDtos) {
		this.reviewDtos = reviewDtos;
	}

	/* Execute */
	public String execute() throws Exception  {
    	
		reviewDtos = dbServices.bidGetReviewsDtos(bidId);
		
     	return SUCCESS;
    }
	

}
