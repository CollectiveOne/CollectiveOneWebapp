package org.collectiveone.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.collectiveone.web.dto.GoalDto;
import org.hibernate.annotations.Type;

@Entity
@Table( name = "GOAL" )
public class Goal {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private Project project;
	@ManyToOne
	private User creator;
	private Timestamp creationDate;
	private String goalTag;
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String description;
	private GoalState state;	
	@OneToOne
	private Decision createDec;
	@OneToOne
	private Decision deleteDec;
	@ManyToOne
	private Goal parent;
	@ManyToOne
	private Goal proposedParent;
	@OneToOne
	private Decision proposeParent;
	private GoalParentState parentState;
	
	
	public GoalDto toDto() {
		GoalDto dto = new GoalDto();
		
		dto.setId(id);
		dto.setProjectName(project.getName());
		dto.setCreatorUsername(creator.getUsername());
		dto.setCreationDate(creationDate);
		dto.setGoalTag(goalTag);
		dto.setDescription(description);
		dto.setState(state.toString());
		if(createDec != null) dto.setCreateDec(createDec.toDto());
		if(deleteDec != null) dto.setDeleteDec(deleteDec.toDto());
		if(parent != null) dto.setParentGoalTag(parent.getGoalTag());
		if(proposedParent != null) dto.setProposedParent(proposedParent.getGoalTag());
		if(proposeParent != null) dto.setProposeParent(proposeParent.toDto());
		if(parentState != null) dto.setParentState(parentState.toString());
		
		return dto;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
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
	public String getGoalTag() {
		return goalTag;
	}
	public void setGoalTag(String goalTag) {
		this.goalTag = goalTag;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public GoalState getState() {
		return state;
	}
	public void setState(GoalState state) {
		this.state = state;
	}
	public Decision getCreateDec() {
		return createDec;
	}
	public void setCreateDec(Decision createDec) {
		this.createDec = createDec;
	}
	public Decision getDeleteDec() {
		return deleteDec;
	}
	public void setDeleteDec(Decision deleteDec) {
		this.deleteDec = deleteDec;
	}
	public Goal getParent() {
		return parent;
	}
	public void setParent(Goal parent) {
		this.parent = parent;
	}
	public Goal getProposedParent() {
		return proposedParent;
	}
	public void setProposedParent(Goal proposedParent) {
		this.proposedParent = proposedParent;
	}
	public Decision getProposeParent() {
		return proposeParent;
	}
	public void setProposeParent(Decision proposeParent) {
		this.proposeParent = proposeParent;
	}
	public GoalParentState getParentState() {
		return parentState;
	}
	public void setParentState(GoalParentState parentState) {
		this.parentState = parentState;
	}
	
}
