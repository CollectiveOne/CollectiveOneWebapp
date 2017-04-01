package org.collectiveone.web.dto;

public class GoalEditionProposalDto {

	private Long id;
	private String proposerUsername;
	private String edition;
	private long creationDate;;
	private Long goalId;
	private String goalTag;
	private DecisionDtoFull acceptDec;
	private String state;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProposerUsername() {
		return proposerUsername;
	}
	public void setProposerUsername(String proposerUsername) {
		this.proposerUsername = proposerUsername;
	}
	public String getEdition() {
		return edition;
	}
	public void setEdition(String edition) {
		this.edition = edition;
	}
	public long getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}
	public Long getGoalId() {
		return goalId;
	}
	public void setGoalId(Long goalId) {
		this.goalId = goalId;
	}
	public String getGoalTag() {
		return goalTag;
	}
	public void setGoalTag(String goalTag) {
		this.goalTag = goalTag;
	}
	public DecisionDtoFull getAcceptDec() {
		return acceptDec;
	}
	public void setAcceptDec(DecisionDtoFull acceptDec) {
		this.acceptDec = acceptDec;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}

