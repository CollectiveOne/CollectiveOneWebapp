package coproject.cpweb.utils.db.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import coproject.cpweb.utils.db.entities.dtos.UserDto;

@Entity
@Table( name = "APP_USERS" )
public class User {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int id;
	
	private String username;
	private String email;
	private String password;
	private String firstname;
	private String lastname;
	private Timestamp joindate;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "USER_PROJECTSCONTRIBUTED")
	private List<Project> projectsContributed = new ArrayList<Project>();
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "USER_CBTIONSCREATED")
	private List<Cbtion> cbtionsCreated = new ArrayList<Cbtion>();
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "USER_CBTIONSACCEPTED")
	private List<Cbtion> cbtionsAccepted = new ArrayList<Cbtion>();
	
	// Dto
	public UserDto toDto() {
		UserDto dto = new UserDto();
		
		dto.setId(id);
		dto.setUsername(username);
		for(Project project:projectsContributed) {
			dto.getProjectsContributed().add(project.getName());
		}
		dto.setnCbtionsCreated(cbtionsCreated.size());
		dto.setnCbtionsAccepted(cbtionsAccepted.size());
		
		return dto;
	}
	
	
	// Getter-Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public Timestamp getJoindate() {
		return joindate;
	}
	public void setJoindate(Timestamp joindate) {
		this.joindate = joindate;
	}
	public List<Project> getProjectsContributed() {
		return projectsContributed;
	}
	public void setProjectsContributed(List<Project> projectsContributed) {
		this.projectsContributed = projectsContributed;
	}
	public List<Cbtion> getCbtionsCreated() {
		return cbtionsCreated;
	}
	public void setCbtionsCreated(List<Cbtion> cbtions) {
		this.cbtionsCreated = cbtions;
	}
	public List<Cbtion> getCbtionsAccepted() {
		return cbtionsAccepted;
	}
	public void setCbtionsAccepted(List<Cbtion> cbtionsAccepted) {
		this.cbtionsAccepted = cbtionsAccepted;
	}
	
}
