package org.collectiveone.model;

import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.collectiveone.web.dto.ProjectDto;
import org.hibernate.annotations.Type;

@Entity
@Table( name = "PROJECTS" )
public class Project {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private boolean enabled;
	
	private String name;
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String description;
	private Timestamp creationDate;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private User creator;
	
	/* aggregated data */
	private double ppsTot;
	
	@OneToMany(mappedBy="project")
	@OrderBy("pps DESC")
	private Set<Contributor> contributors = new LinkedHashSet<Contributor>();
	
	@ManyToMany(mappedBy="projectsStarred")
	private List<User> usersThatStarred;
	
	@ManyToMany(mappedBy="projectsWatched")
	private List<User> usersThatWatched;
	
	
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
	public boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
	public Set<Contributor> getContributors() {
		return contributors;
	}
	public void setContributors(Set<Contributor> contributors) {
		this.contributors = contributors;
	}
	public List<User> getUsersThatStarred() {
		return usersThatStarred;
	}
	public void setUsersThatStarred(List<User> usersThatStarred) {
		this.usersThatStarred = usersThatStarred;
	}
	public List<User> getUsersThatWatched() {
		return usersThatWatched;
	}
	public void setUsersThatWatched(List<User> usersThatWatched) {
		this.usersThatWatched = usersThatWatched;
	}
	
}
