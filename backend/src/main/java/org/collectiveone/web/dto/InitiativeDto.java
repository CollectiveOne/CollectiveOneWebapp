package org.collectiveone.web.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class InitiativeDto {
	
	private String id;
	private String name;
	private String driver;
	private Timestamp creationDate;
	private String creatorC1Id;
	private String creatorNickname;
	private String tokenId;
	private String tokenName;
	private double remainingTokens = 0.0;
	private double totalExistingTokens = 0.0;
	private double totalAssignedToOtherInitiatives = 0.0;
	private double totalAssignedToUsers = 0.0;
	
	private List<InitiativeDto> subInitiatives = new ArrayList<InitiativeDto>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	public String getCreatorC1Id() {
		return creatorC1Id;
	}
	public void setCreatorC1Id(String creatorC1Id) {
		this.creatorC1Id = creatorC1Id;
	}
	public String getCreatorNickname() {
		return creatorNickname;
	}
	public void setCreatorNickname(String creatorNickname) {
		this.creatorNickname = creatorNickname;
	}
	public List<InitiativeDto> getSubInitiatives() {
		return subInitiatives;
	}
	public void setSubInitiatives(List<InitiativeDto> subInitiatives) {
		this.subInitiatives = subInitiatives;
	}
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public String getTokenName() {
		return tokenName;
	}
	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}
	public double getRemainingTokens() {
		return remainingTokens;
	}
	public void setRemainingTokens(double remainingTokens) {
		this.remainingTokens = remainingTokens;
	}
	public double getTotalExistingTokens() {
		return totalExistingTokens;
	}
	public void setTotalExistingTokens(double totalExistingTokens) {
		this.totalExistingTokens = totalExistingTokens;
	}
	public double getTotalAssignedToOtherInitiatives() {
		return totalAssignedToOtherInitiatives;
	}
	public void setTotalAssignedToOtherInitiatives(double totalAssignedToOtherInitiatives) {
		this.totalAssignedToOtherInitiatives = totalAssignedToOtherInitiatives;
	}
	public double getTotalAssignedToUsers() {
		return totalAssignedToUsers;
	}
	public void setTotalAssignedToUsers(double totalAssignedToUsers) {
		this.totalAssignedToUsers = totalAssignedToUsers;
	}
	
}
