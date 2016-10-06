package coproject.cpweb.actions.json;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import coproject.cpweb.utils.db.entities.User;
import coproject.cpweb.utils.db.entities.dtos.BidDto;
import coproject.cpweb.utils.db.entities.dtos.UserDto;

@Action("BidOffer")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false",
    		"includeProperties","^fieldErrors.*,"
    				+ "^resStatus.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*,res"})
})
public class BidOffer extends CpAction {
	
	private static final long serialVersionUID = 1L;
	
	/* Input Json  */
	private BidDto bidDto = new BidDto();
	public BidDto getBidDto() {
		return bidDto;
	}
	public void setBidDto(BidDto bidDto) {
		this.bidDto = bidDto;
	}

	public void validate() {
		if(bidDto.getDeliveryDate() == 0) {
			addFieldError("delivery date", " needed");
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
			
		dbServices.bidFromConsideringToOffered(bidDto);
		resStatus = dbServices.getStatus();	
		
		return SUCCESS;
    }
	

}
