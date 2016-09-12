package coproject.cpweb.actions.json;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.dtos.CbtionDto;
import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("CbtionGet")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","^cbtionDto.*,^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class CbtionGet extends ActionSupport {
	
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
	private int id;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	/* Output parameters  */
	private CbtionDto cbtionDto = new CbtionDto();
	
	public CbtionDto getCbtionDto() {
		return cbtionDto;
	}
	public void setCbtionDto(CbtionDto cbtionDto) {
		this.cbtionDto = cbtionDto;
	}

	/* Execute */
	public String execute() throws Exception  {
    	
		cbtionDto	= dbServices.cbtionGetDto(id);
		
     	return SUCCESS;
    }
	

}
