package coproject.cpweb.actions.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.ArgumentTendency;
import coproject.cpweb.utils.db.entities.dtos.ArgumentDto;
import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("ArgumentsGetOfDecision")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","^argumentYesDtos.*,^argumentNoDtos.*,^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class ArgumentsGetOfDecision extends ActionSupport{
	
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
	private int decisionId;
	public int getDecisionId() {
		return decisionId;
	}
	public void setDecisionId(int decisionId) {
		this.decisionId = decisionId;
	}

	/* Output parameters  */
	private List<ArgumentDto> argumentYesDtos = new ArrayList<ArgumentDto>();
	private List<ArgumentDto> argumentNoDtos = new ArrayList<ArgumentDto>();
	public List<ArgumentDto> getArgumentYesDtos() {
		return argumentYesDtos;
	}
	public void setArgumentYesDtos(List<ArgumentDto> argumentYesDtos) {
		this.argumentYesDtos = argumentYesDtos;
	}
	public List<ArgumentDto> getArgumentNoDtos() {
		return argumentNoDtos;
	}
	public void setArgumentNoDtos(List<ArgumentDto> argumentNoDtos) {
		this.argumentNoDtos = argumentNoDtos;
	}
	
		/* Execute */
	public String execute() throws Exception  {
    	
		argumentYesDtos = dbServices.argumentGetOfDecisionDto(decisionId, ArgumentTendency.FORYES);
		argumentNoDtos = dbServices.argumentGetOfDecisionDto(decisionId, ArgumentTendency.FORNO);
		
     	return SUCCESS;
    }
	

}
