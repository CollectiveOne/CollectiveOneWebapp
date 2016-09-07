package coproject.cpweb.actions.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

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
	private Filters filters = new Filters();
	public Filters getFilters() {
		return filters;
	}
	public void setFilters(Filters filters) {
		this.filters = filters;
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
    	
		if(filters.getPage() == 0) filters.setPage(1);
		if(filters.getNperpage() == 0) filters.setNperpage(15);
		
		GoalDtoListRes goalsDtosRes = dbServices.goalDtoGetFiltered(filters);
		
		goalDtos = goalsDtosRes.getGoalDtos();
		resSet = goalsDtosRes.getResSet();
		
     	return SUCCESS;
		
    }
	

}
