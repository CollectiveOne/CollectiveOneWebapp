package coproject.cpweb.actions.json;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("ArgumentIsBacked")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","isbacked,res,msg,^fieldErrors.*"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class ArgumentIsBacked extends ActionSupport {
	
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
	private int argId;
	public int getArgId() {
		return argId;
	}
	public void setArgId(int argId) {
		this.argId = argId;
	}
	
	private int userId;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/* Output parameters  */
	boolean isbacked;
	public boolean isIsbacked() {
		return isbacked;
	}
	public void setIsbacked(boolean isbacked) {
		this.isbacked = isbacked;
	}

	boolean res;
	public boolean isRes() {
		return res;
	}
	public void setRes(boolean res) {
		this.res = res;
	}
	String msg;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/* Execute */
	public String execute() throws Exception  {
		
		isbacked = dbServices.argumentIsBacked(argId,userId);
		res = true;
		
     	return SUCCESS;
    }

}
