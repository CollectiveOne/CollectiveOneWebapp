package coproject.cpweb.actions.json;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.dtos.ArgumentDto;
import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("ArgumentGet")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","^argumentDto.*,^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class ArgumentGet extends ActionSupport{
	
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
	private ArgumentDto argumentDto = new ArgumentDto();
	public ArgumentDto getArgumentDto() {
		return argumentDto;
	}
	public void setArgumentDto(ArgumentDto argumentDto) {
		this.argumentDto = argumentDto;
	}

	/* Execute */
	public String execute() throws Exception  {
    	
		argumentDto = dbServices.argumentGetDto(id);
		
     	return SUCCESS;
    }
	

}
