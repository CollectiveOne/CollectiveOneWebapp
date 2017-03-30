package org.collectiveone.web.dto;

import java.sql.Timestamp;

public class ThesisDto {
	private Long id;
	private int value;
	private Timestamp castDate;
	private String authorUsername;
	private double weight;
	private Long decisionId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public Timestamp getCastDate() {
		return castDate;
	}
	public void setCastDate(Timestamp castDate) {
		this.castDate = castDate;
	}
	public String getAuthorUsername() {
		return authorUsername;
	}
	public void setAuthorUsername(String authorUsername) {
		this.authorUsername = authorUsername;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public Long getDecisionId() {
		return decisionId;
	}
	public void setDecisionId(Long decisionId) {
		this.decisionId = decisionId;
	}
	
	
}
