package coproject.cpweb.actions.json;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import coproject.cpweb.utils.db.entities.User;
import coproject.cpweb.utils.db.entities.dtos.BidDto;
import coproject.cpweb.utils.db.entities.dtos.UserDto;

@Action("BidNew")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false",
    		"includeProperties","^fieldErrors.*,"
    				+ "^resStatus.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*,res"})
})
public class BidNew extends CpAction {
	
	private static final long serialVersionUID = 1L;
	
	/* Input Json  */
	private BidDto bidDto = new BidDto();
	public BidDto getBidDto() {
		return bidDto;
	}
	public void setBidDto(BidDto bidDto) {
		this.bidDto = bidDto;
	}
	
	private boolean offer;
	public boolean getOffer() {
		return offer;
	}
	public void setOffer(boolean offer) {
		this.offer = offer;
	}
	public void validate() {
		if(offer) { if(bidDto.getDeliveryDate() == 0) {
				addFieldError("delivery date", " needed");
			}
		}
	}
	
	/* Execute */
	public String execute() throws Exception  {
    	
		UserDto userLoggedDtoSes = (UserDto) getSession().get("userLoggedDto");
		User creator = dbServices.userGet(bidDto.getCreatorDto().getUsername());
		
		if(creator.getId() != userLoggedDtoSes.getId()) {
			addFieldError("creator", " project creator is not logged in");
			return SUCCESS;
		}
			
		int bidId = dbServices.bidCreate(bidDto);
		
		/* if pps offered and delivery date are provided switch state to OFFERED */
		if(dbServices.getStatus().getSuccess()) {
			if(offer) {
				bidDto.setId(bidId);
				dbServices.bidFromConsideringToOffered(bidDto);
			}	
		}		
		
		resStatus = dbServices.getStatus();
		
		// updated logged user data with new projects
		UserDto userLoggedDto = dbServices.userGetDto(creator.getUsername());
		getSession().put("userLoggedDto",userLoggedDto);
		
		return SUCCESS;
    }
	

}
