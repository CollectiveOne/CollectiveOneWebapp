package org.collectiveone.web.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

public class GoalDto {
	
	private Long id;
	@NotEmpty
	private String projectName;
	private String creatorUsername;
	private Timestamp creationDate;
	@NotEmpty
	@Pattern(regexp="^((?!\\s).)*$", message="may not contain white spaces")
	private String goalTag;
	private String parentGoalTag;
	@NotEmpty
	private String description;
	
	private List<String> parentGoalsTags = new ArrayList<String>();
	private List<String> subGoalsTags = new ArrayList<String>();
	private String state;
	private DecisionDtoFull createDec;
	private DecisionDtoFull deleteDec;
	private String parentState;
	private String proposedParent;
	private DecisionDtoFull proposeParent;
	
	private String attachedState;
	private double currentBudget;
	private DecisionDtoFull increaseBudgetDec;
	private double ppsToIncrease;
	private DecisionDtoFull reattachDec;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getCreatorUsername() {
		return creatorUsername;
	}
	public void setCreatorUsername(String creatorUsername) {
		this.creatorUsername = creatorUsername;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	public String getParentGoalTag() {
		return parentGoalTag;
	}
	public void setParentGoalTag(String parentGoalTag) {
		this.parentGoalTag = parentGoalTag.trim();
	}
	public String getGoalTag() {
		return goalTag;
	}
	public void setGoalTag(String goalTag) {
		this.goalTag = goalTag.trim();
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public DecisionDtoFull getCreateDec() {
		return createDec;
	}
	public void setCreateDec(DecisionDtoFull createDec) {
		this.createDec = createDec;
	}
	public DecisionDtoFull getDeleteDec() {
		return deleteDec;
	}
	public void setDeleteDec(DecisionDtoFull deleteDec) {
		this.deleteDec = deleteDec;
	}
	public List<String> getParentGoalsTags() {
		return parentGoalsTags;
	}
	public void setParentGoalsTags(List<String> parentGoalsTags) {
		this.parentGoalsTags = parentGoalsTags;
	}
	public List<String> getSubGoalsTags() {
		return subGoalsTags;
	}
	public void setSubGoalsTags(List<String> subGoalsTags) {
		this.subGoalsTags = subGoalsTags;
	}
	public String getParentState() {
		return parentState;
	}
	public void setParentState(String parentState) {
		this.parentState = parentState;
	}
	public String getProposedParent() {
		return proposedParent;
	}
	public void setProposedParent(String proposedParent) {
		this.proposedParent = proposedParent;
	}
	public DecisionDtoFull getProposeParent() {
		return proposeParent;
	}
	public void setProposeParent(DecisionDtoFull proposeParent) {
		this.proposeParent = proposeParent;
	}
	public String getAttachedState() {
		return attachedState;
	}
	public void setAttachedState(String attachedState) {
		this.attachedState = attachedState;
	}
	public double getCurrentBudget() {
		return currentBudget;
	}
	public void setCurrentBudget(double currentBudget) {
		this.currentBudget = currentBudget;
	}
	public DecisionDtoFull getIncreaseBudgetDec() {
		return increaseBudgetDec;
	}
	public void setIncreaseBudgetDec(DecisionDtoFull increaseBudgetDec) {
		this.increaseBudgetDec = increaseBudgetDec;
	}
	public double getPpsToIncrease() {
		return ppsToIncrease;
	}
	public void setPpsToIncrease(double ppsToIncrease) {
		this.ppsToIncrease = ppsToIncrease;
	}
	public DecisionDtoFull getReattachDec() {
		return reattachDec;
	}
	public void setReattachDec(DecisionDtoFull reattachDec) {
		this.reattachDec = reattachDec;
	}
	
}

