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
	
	private AssetsDto ownTokens;
	private List<AssetsDto> otherAssets = new ArrayList<AssetsDto>();
	private List<InitiativeDto> subInitiatives = new ArrayList<InitiativeDto>();
	private List<ContributorDto> contributors = new ArrayList<ContributorDto>();
	
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
	public AssetsDto getOwnTokens() {
		return ownTokens;
	}
	public void setOwnTokens(AssetsDto ownTokens) {
		this.ownTokens = ownTokens;
	}
	public List<AssetsDto> getOtherAssets() {
		return otherAssets;
	}
	public void setOtherAssets(List<AssetsDto> otherAssets) {
		this.otherAssets = otherAssets;
	}
	public List<ContributorDto> getContributors() {
		return contributors;
	}
	public void setContributors(List<ContributorDto> contributors) {
		this.contributors = contributors;
	}
	
}
