package coproject.cpweb.actions.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.dtos.BidDto;
import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("BidsOfCbtionGet")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","^bidDtos.*,^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class BidsOfCbtionGet extends ActionSupport{
	
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
	private int cbtionId;
	
	public int getCbtionId() {
		return cbtionId;
	}
	public void setCbtionId(int cbtionId) {
		this.cbtionId = cbtionId;
	}

	/* Output parameters  */
	private List<BidDto> bidDtos = new ArrayList<BidDto>();
	
	public List<BidDto> getBidDtos() {
		return bidDtos;
	}
	public void setBidDtos(List<BidDto> bidDtos) {
		this.bidDtos = bidDtos;
	}

	/* Execute */
	public String execute() throws Exception  {
    	
		bidDtos = dbServices.bidGetOfCbtionDto(cbtionId);
		
     	return SUCCESS;
    }
	

}
