package coproject.cpweb.utils.db.entities;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.ManyToOne;

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
	
	private double ppsTot;
	
	public ProjectDto toDto() {
		ProjectDto dto = new ProjectDto();
		
		dto.setId(id);
		dto.setName(name);
		dto.setCreatorUsername(creator.getUsername());
		dto.setCreationDate(creationDate);
		dto.setDescription(description);
		dto.setPpsTot(ppsTot);
		
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
	public double getPpsTot() {
		return ppsTot;
	}
	public void setPpsTot(double ppsTot) {
		this.ppsTot = ppsTot;
	}
	
}
