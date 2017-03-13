package org.collectiveone.web.dto;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

public class CbtionDto {
	
	private Long id;
	@NotEmpty
	private String projectName;
	private String creatorUsername;
	private Long creationDate;
	@NotEmpty
	private String title;
	@NotEmpty
	private String description;
	private String product;
	private double suggestedBid;
	private Integer relevance;
	private String state;
	private int nBids;
	private String contributorUsername;
	private String assigneeUsername;
	private double assigneePpoints;
	private List<String> parentGoalsTags;
	@NotEmpty
	private String goalTag;
	private double assignedPpoints;
	private DecisionDtoFull openDec;
	private DecisionDtoFull deleteDec;
	private int ncomments; 
	
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
	public Long getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public double getSuggestedBid() {
		return suggestedBid;
	}
	public void setSuggestedBid(double suggestedBid) {
		this.suggestedBid = suggestedBid;
	}
	public Integer getRelevance() {
		return relevance;
	}
	public void setRelevance(Integer relevance) {
		this.relevance = relevance;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getnBids() { // NO_UCD (unused code)
		return nBids;
	}
	public void setnBids(int nBids) {
		this.nBids = nBids;
	}
	public String getContributorUsername() {
		return contributorUsername;
	}
	public void setContributorUsername(String contributorUsername) {
		this.contributorUsername = contributorUsername;
	}
	public String getAssigneeUsername() {
		return assigneeUsername;
	}
	public void setAssigneeUsername(String assigneeUsername) {
		this.assigneeUsername = assigneeUsername;
	}
	public String getGoalTag() {
		return goalTag;
	}
	public List<String> getParentGoalsTags() {
		return parentGoalsTags;
	}
	public void setParentGoalsTags(List<String> parentGoalsTags) {
		this.parentGoalsTags = parentGoalsTags;
	}
	public void setGoalTag(String goalTag) {
		this.goalTag = goalTag.trim();
	}
	public double getAssignedPpoints() {
		return assignedPpoints;
	}
	public void setAssignedPpoints(double assignedPpoints) {
		this.assignedPpoints = assignedPpoints;
	}
	public double getAssigneePpoints() {
		return assigneePpoints;
	}
	public void setAssigneePpoints(double assigneePpoints) {
		this.assigneePpoints = assigneePpoints;
	}
	public DecisionDtoFull getOpenDec() {
		return openDec;
	}
	public void setOpenDec(DecisionDtoFull openDec) {
		this.openDec = openDec;
	}
	public DecisionDtoFull getDeleteDec() {
		return deleteDec;
	}
	public void setDeleteDec(DecisionDtoFull deleteDec) {
		this.deleteDec = deleteDec;
	}

	public int getNcomments() {
		return ncomments;
	}
	public void setNcomments(int ncomments) {
		this.ncomments = ncomments;
	}
	
}
