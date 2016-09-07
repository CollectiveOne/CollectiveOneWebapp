package coproject.cpweb.actions.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.GoalState;
import coproject.cpweb.utils.db.entities.dtos.GoalDto;
import coproject.cpweb.utils.db.services.DbServicesImp;
import coproject.cpweb.utils.db.services.Filters;
import coproject.cpweb.utils.db.services.GoalDtoListRes;

@Action("GoalListGet")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","^goalDtos.*,^resSet.*,^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class GoalListGet extends ActionSupport{
	
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
	private List<GoalDto> goalDtos = new ArrayList<GoalDto>();
	
	public List<GoalDto> getGoalDtos() {
		return goalDtos;
	}
	public void setGoalDtos(List<GoalDto> goalDtos) {
		this.goalDtos = goalDtos;
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
		
		Filters filters = new Filters();
		filters.projectNames = projectNames;
		filters.stateNames = stateNames;
		
		/* by default show only open and assigned cbtions */
		if(filters.stateNames.size() == 0) {
			filters.stateNames.add(GoalState.PROPOSED.toString());
			filters.stateNames.add(GoalState.ACCEPTED.toString());
		}	
		
		GoalDtoListRes goalsDtosRes = dbServices.goalDtoGetFiltered(filters,page,nPerPage);
		
		goalDtos = goalsDtosRes.getGoalDtos();
		resSet = goalsDtosRes.getResSet();
		
     	return SUCCESS;
    }
	

}
