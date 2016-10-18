package coproject.cpweb.actions.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.dtos.DecisionDto;
import coproject.cpweb.utils.db.services.DbServicesImp;
import coproject.cpweb.utils.db.services.DecisionDtoListRes;
import coproject.cpweb.utils.db.services.Filters;

@Action("DecisionListGet")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","^decisionDtos.*,^resSet.*,^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class DecisionListGet extends CpAction {
	
	private static final long serialVersionUID = 1L;
	
	/* Input parameters  */
	private Filters filters = new Filters();
	public Filters getFilters() {
		return filters;
	}
	public void setFilters(Filters filters) {
		this.filters = filters;
	}

	/* Output parameters  */
	private List<DecisionDto> decisionDtos = new ArrayList<DecisionDto>();
	
	public List<DecisionDto> getDecisionDtos() {
		return decisionDtos;
	}
	public void setDecisionDtos(List<DecisionDto> decisionDtos) {
		this.decisionDtos = decisionDtos;
	}

	private int[] resSet;
	public int[] getResSet() {
		return resSet;
	}
	public void setResSet(int[] resSet) {
		this.resSet = resSet;
	}

	/* Execute */
	public String execute() throws Exception  {
    	
		if(filters.getPage() == 0) filters.setPage(1);
		if(filters.getNperpage() == 0) filters.setNperpage(15);
		
		DecisionDtoListRes decisionsDtosRes = dbServices.decisionDtoGetFiltered(filters);
		
		decisionDtos = decisionsDtosRes.getDecisionDtos();
		resSet = decisionsDtosRes.getResSet();
		
     	return SUCCESS;
    }
	

}
