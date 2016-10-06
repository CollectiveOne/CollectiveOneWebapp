package coproject.cpweb.actions.json;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import coproject.cpweb.utils.db.entities.User;
import coproject.cpweb.utils.db.entities.dtos.UserDto;

@Action("BidMarkDone")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false",
    		"includeProperties","^fieldErrors.*,"
    				+ "^resStatus.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*,res"})
})
public class BidMarkDone extends CpAction {
	
	private static final long serialVersionUID = 1L;
	
	/* Input Json  */
	private int bidId;
	private String description;
	
	public int getBidId() {
		return bidId;
	}
	public void setBidId(int bidId) {
		this.bidId = bidId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	/* Execute */
	public String execute() throws Exception  {
    	
		UserDto userLoggedDtoSes = (UserDto) getSession().get("userLoggedDto");
		User creator = dbServices.bidGetCreator(bidId);
		
		if(creator.getId() != userLoggedDtoSes.getId()) {
			addFieldError("creator", " bid creator is not logged in");
			return SUCCESS;
		}
			
		dbServices.bidMarkDone(bidId,description);
		resStatus = dbServices.getStatus();	
		
		return SUCCESS;
    }
	

}
