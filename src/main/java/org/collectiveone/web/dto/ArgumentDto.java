package org.collectiveone.web.dto;

public class ArgumentDto {
	private Long id;
	private String creatorUsername;
	private long creationDate;
	private String description;
	private Long decisionId;
	private String decisionDescription;
	private String tendency;
	private int nbackers;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCreatorUsername() {
		return creatorUsername;
	}
	public void setCreatorUsername(String creatorUsername) {
		this.creatorUsername = creatorUsername;
	}
	public long getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getDecisionId() {
		return decisionId;
	}
	public void setDecisionId(Long decisionId) {
		this.decisionId = decisionId;
	}
	public String getDecisionDescription() {
		return decisionDescription;
	}
	public void setDecisionDescription(String decisionDescription) {
		this.decisionDescription = decisionDescription;
	}
	public String getTendency() {
		return tendency;
	}
	public void setTendency(String tendency) {
		this.tendency = tendency;
	}
	public int getNbackers() {
		return nbackers;
	}
	public void setNbackers(int nbackers) {
		this.nbackers = nbackers;
	}
	
}
