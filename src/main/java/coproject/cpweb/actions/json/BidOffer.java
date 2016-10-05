package coproject.cpweb.actions.json;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import coproject.cpweb.utils.db.entities.Bid;
import coproject.cpweb.utils.db.entities.User;
import coproject.cpweb.utils.db.entities.dtos.OfferDto;
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
	private OfferDto offerDto = new OfferDto();
	public OfferDto getOfferDto() {
		return offerDto;
	}

	public void setOfferDto(OfferDto offerDto) {
		this.offerDto = offerDto;
	}

	public void validate() {
		if(offerDto.getDeliveryDate() == 0) {
			addFieldError("delivery date", " needed");
		}
	}
	
	/* Execute */
	public String execute() throws Exception  {
    	
		UserDto userLoggedDtoSes = (UserDto) getSession().get("userLoggedDto");
		User creator = dbServices.userGet(offerDto.getCreatorUsername());
		
		if(creator.getId() != userLoggedDtoSes.getId()) {
			addFieldError("creator", " project creator is not logged in");
			return SUCCESS;
		}
			
		Bid bid = dbServices.bidGet(offerDto.getBidId());
				
		dbServices.bidFromConsideringToOffered(bid.getId(), offerDto.getPpoints(), offerDto.getDeliveryDate());
		resStatus = dbServices.getStatus();
		
		return SUCCESS;
    }
	

}
