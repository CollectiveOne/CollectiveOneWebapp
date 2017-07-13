package org.collectiveone.modules.initiatives;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.collectiveone.modules.tokens.AssetsDto;
import org.collectiveone.modules.users.AppUserDto;

public class InitiativeDto {
	
	private String id;
	private String name;
	private String driver;
	private Timestamp creationDate;
	private AppUserDto creator;
		
	private String ownAssetsId;
	private List<AssetsDto> assets = new ArrayList<AssetsDto>();
	private List<InitiativeDto> subInitiatives = new ArrayList<InitiativeDto>();
	private List<InitiativeDto> parents = new ArrayList<InitiativeDto>();
	private InitiativeMembersDto initiativeMembers;
	private MemberDto loggedMember;
	
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
	public AppUserDto getCreator() {
		return creator;
	}
	public void setCreator(AppUserDto creator) {
		this.creator = creator;
	}
	public List<InitiativeDto> getSubInitiatives() {
		return subInitiatives;
	}
	public void setSubInitiatives(List<InitiativeDto> subInitiatives) {
		this.subInitiatives = subInitiatives;
	}
	public List<InitiativeDto> getParents() {
		return parents;
	}
	public void setParents(List<InitiativeDto> parents) {
		this.parents = parents;
	}
	public String getOwnAssetsId() {
		return ownAssetsId;
	}
	public void setOwnAssetsId(String ownAssetsId) {
		this.ownAssetsId = ownAssetsId;
	}
	public List<AssetsDto> getAssets() {
		return assets;
	}
	public void setAssets(List<AssetsDto> assets) {
		this.assets = assets;
	}
	public InitiativeMembersDto getInitiativeMembers() {
		return initiativeMembers;
	}
	public void setInitiativeMembers(InitiativeMembersDto members) {
		this.initiativeMembers = members;
	}
	public MemberDto getLoggedMember() {
		return loggedMember;
	}
	public void setLoggedMember(MemberDto loggedMember) {
		this.loggedMember = loggedMember;
	}
	
}
