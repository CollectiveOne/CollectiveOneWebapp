package coproject.cpweb.actions.json;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.dtos.DecisionDto;
import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("DecisionGet")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","^decisionDto.*,^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class DecisionGet extends CpAction {
	
	private static final long serialVersionUID = 1L;
		
	/* Input parameters  */
	private int id;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	/* Output parameters  */
	private DecisionDto decisionDto = new DecisionDto();
	public DecisionDto getDecisionDto() {
		return decisionDto;
	}
	public void setDecisionDto(DecisionDto decisionDto) {
		this.decisionDto = decisionDto;
	}

	/* Execute */
	public String execute() throws Exception  {
    	
		decisionDto	= dbServices.decisionGetDto(id);
		
     	return SUCCESS;
    }
	

}
