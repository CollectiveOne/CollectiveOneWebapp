package coproject.cpweb.utils.db.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import coproject.cpweb.utils.db.entities.dtos.ProjectDto;

@Entity
@Table( name = "PROJECTS" )
public class Project {
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int id;
	
	private String name;
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String description;
	private Timestamp creationDate;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private User creator;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "PROJECT_CBTIONSACCEPTED")
	private List<Cbtion> cbtionsAccepted = new ArrayList<Cbtion>();
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "PROJECT_CONTRIBUTORS")
	private List<User> contributors = new ArrayList<User>();
	
	public ProjectDto toDto() {
		ProjectDto dto = new ProjectDto();
		
		dto.setId(id);
		dto.setName(name);
		dto.setCreatorUsername(creator.getUsername());
		dto.setCreationDate(creationDate);
		dto.setDescription(description);
		dto.setnCbtionsAccepted(cbtionsAccepted.size());
		dto.setnContributors(contributors.size());
		
		return dto;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	public List<Cbtion> getCbtionsAccepted() {
		return cbtionsAccepted;
	}
	public void setCbtionsAccepted(List<Cbtion> cbtionsAccepted) {
		this.cbtionsAccepted = cbtionsAccepted;
	}
	public List<User> getContributors() {
		return contributors;
	}
	public void setContributors(List<User> contributors) {
		this.contributors = contributors;
	}
}
