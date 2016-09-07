package coproject.cpweb.actions.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.entities.CbtionState;
import coproject.cpweb.utils.db.entities.dtos.CbtionDto;
import coproject.cpweb.utils.db.services.CbtionDtoListRes;
import coproject.cpweb.utils.db.services.DbServicesImp;
import coproject.cpweb.utils.db.services.Filters;

@Action("CbtionListGet")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","^cbtionDtos.*,^resSet.*,^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class CbtionListGet extends ActionSupport{
	
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
	private List<CbtionDto> cbtionDtos = new ArrayList<CbtionDto>();
	
	public List<CbtionDto> getCbtionDtos() {
		return cbtionDtos;
	}
	public void setCbtionDtos(List<CbtionDto> cbtionDtos) {
		this.cbtionDtos = cbtionDtos;
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
			filters.stateNames.add(CbtionState.OPEN.toString());
			filters.stateNames.add(CbtionState.ASSIGNED.toString());
		}	
		
		CbtionDtoListRes cbtionsDtosRes = dbServices.cbtionDtoGetFiltered(filters,page,nPerPage);
		
		cbtionDtos = cbtionsDtosRes.getCbtionsDtos();
		resSet = cbtionsDtosRes.getResSet();
		
     	return SUCCESS;
    }
	

}
