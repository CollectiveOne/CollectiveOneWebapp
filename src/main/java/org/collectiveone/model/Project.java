package org.collectiveone.model;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.collectiveone.web.dto.ProjectDto;
import org.hibernate.annotations.Type;

@Entity
@Table( name = "PROJECTS" )
@SequenceGenerator(name="projects_seq", initialValue=1, allocationSize=100)
public class Project {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="projects_seq")
	private Long id;
	
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
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
