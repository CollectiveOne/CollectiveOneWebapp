package org.collectiveone.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.web.dto.ActivityDto;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ACTIVITY")
public class Activity {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Long id;
	private Timestamp creationDate;
	private String event;
	@ManyToOne
	private Project project;
	
	private ActivityType type; 
	@ManyToOne
	private Cbtion cbtion;
	@ManyToOne
	private Bid bid;
	@ManyToOne
	private Goal goal;
	@ManyToOne
	private Decision decision;
	@ManyToOne
	private Argument argument;
	
	public Activity() {
	}
	
	public Activity(String event, Timestamp date, Project project) {
		this.event = event;
		this.creationDate = date;
		this.project = project;
	}
	
	public ActivityDto toDto() {
		ActivityDto dto = new ActivityDto();
		
		dto.setId(id);
		dto.setProjectName(project.getName());
		dto.setCreationDate(creationDate.getTime());
		dto.setEvent(event);
		dto.setType(type.toString());
		if(cbtion != null) dto.setCbtionDto(cbtion.toDto());
		if(bid != null) dto.setBidDto(bid.toDto());
		if(goal != null) dto.setGoalDto(goal.toDto());
		if(decision != null) dto.setDecisionDto(decision.toDto());
		if(argument != null) dto.setArgumentDto(argument.toDto());
		
		return dto;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public ActivityType getType() {
		return type;
	}
	public void setType(ActivityType type) {
		this.type = type;
	}
	public Cbtion getCbtion() {
		return cbtion;
	}
	public void setCbtion(Cbtion cbtion) {
		this.cbtion = cbtion;
	}
	public Bid getBid() {
		return bid;
	}
	public void setBid(Bid bid) {
		this.bid = bid;
	}
	public Goal getGoal() {
		return goal;
	}
	public void setGoal(Goal goal) {
		this.goal = goal;
	}
	public Decision getDecision() {
		return decision;
	}
	public void setDecision(Decision decision) {
		this.decision = decision;
	}
	public Argument getArgument() {
		return argument;
	}
	public void setArgument(Argument argument) {
		this.argument = argument;
	}
	
}
