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

@Action("BidsOfUserGet")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","^bidDOTs.*,^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class BidsOfUserGet extends ActionSupport{
	
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
	private int userId;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/* Output parameters  */
	private List<BidDto> bidDOTs = new ArrayList<BidDto>();
	
	public List<BidDto> getBidDOTs() {
		return bidDOTs;
	}
	public void setBidDOTs(List<BidDto> bidDOTs) {
		this.bidDOTs = bidDOTs;
	}

	/* Execute */
	public String execute() throws Exception  {
    	
		bidDOTs = dbServices.bidGetOfUserDto(userId);
		
     	return SUCCESS;
    }
	

}
