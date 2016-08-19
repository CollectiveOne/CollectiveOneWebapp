package coproject.cpweb.actions.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;

import coproject.cpweb.utils.db.services.DbServicesImp;

@Action("UserNew")
@ParentPackage("json-data")
@Results({
    @Result(name="success", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*,res"}),
    @Result(name="input", type="json", params={"ignoreHierarchy","false","includeProperties","^fieldErrors.*"})
})
public class UserNew extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	
	/* Services  */
	DbServicesImp dbServices;
	
	public DbServicesImp getDbServices() {
		return dbServices;
	}

	public void setDbServices(DbServicesImp dbServices) {
		this.dbServices = dbServices;
	}
	
	/* Input Json  */
	private String username;
	private String password;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	/* Output res  */
	private String res;
	public String getRes() {
		return res;
	}
	public void setRes(String res) {
		this.res = res;
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#validate()
	 */
	public void validate() {
		if(username.contains(" ")) {
			addFieldError("username", "cant include spaces");
		}
		
		if(password.contains(" ")) {
			addFieldError("password", "cant include spaces");
		}
		
		if(!username.equals(username.toLowerCase())) {
			addFieldError("username", "must be lower case only");
		}
		
		if(username.length() < 6){
			addFieldError("username", "must be at least 6 charachers long");
		}
		
		if(password.length() < 8){
			addFieldError("password", "must be at least 8 charachers long");
		}
		
		/* allow only a given set of users */
		Properties prop = new Properties();
		InputStream input = null;
		 
		try {
			input = getClass().getResourceAsStream("login.properties");
			prop.load(input);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String[] aprovedUsers = prop.getProperty("aprovedUsers").split(",");
		if(!Arrays.asList(aprovedUsers).contains(username)) {
			addFieldError("username", "signup not aproved");
		}
		
	}
	
	/* Execute */
	public String execute() throws Exception  {
    	
		res = dbServices.userCreate(username, password);

		return SUCCESS;
    }
	

}
