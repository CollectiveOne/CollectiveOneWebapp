package coproject.cpweb.actions.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.DecisionState;
import coproject.cpweb.utils.db.entities.dtos.DecisionDto;
import coproject.cpweb.utils.db.services.DbServicesImp;
import coproject.cpweb.utils.db.services.DecisionDtoListRes;
import coproject.cpweb.utils.db.services.DecisionFilters;

@Action("DecisionListGet")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","^decisionDtos.*,^resSet.*,^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class DecisionListGet extends ActionSupport{
	
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
	private List<String> projectNames = new ArrayList<String>();

	public List<String> getProjectNames() {
		return projectNames;
	}
	public void setProjectNames(List<String> projectNames) {
		this.projectNames = projectNames;
	}
	
	public List<String> stateNames = new ArrayList<String>();
	
	public List<String> getStateNames() {
		return stateNames;
	}
	public void setStateNames(List<String> stateNames) {
		this.stateNames = stateNames;
	}

	private int page;
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}

	private int nPerPage;
	
	public int getnPerPage() {
		return nPerPage;
	}

	public void setnPerPage(int nPerPage) {
		this.nPerPage = nPerPage;
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
    	
		if(page == 0) page = 1;
		if(nPerPage == 0) nPerPage = 15;
		
		DecisionFilters filters = new DecisionFilters();
		filters.projectNames = projectNames;
		filters.stateNames = stateNames;
		
		/* by default show only open and assigned cbtions */
		if(filters.stateNames.size() == 0) {
			filters.stateNames.add(DecisionState.IDLE.toString());
			filters.stateNames.add(DecisionState.OPEN.toString());
		}	
		
		DecisionDtoListRes decisionsDtosRes = dbServices.decisionDtoGetFiltered(filters,page,nPerPage);
		
		decisionDtos = decisionsDtosRes.getDecisionDtos();
		resSet = decisionsDtosRes.getResSet();
		
     	return SUCCESS;
    }
	

}
