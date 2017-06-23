package org.collectiveone.modules.initiatives;

import java.util.ArrayList;
import java.util.List;

public class InitiativeMembersDto {

	private String initiativeId;
	private String initiativeName;
	private List<MemberDto> members = new ArrayList<MemberDto>();
	private List<InitiativeMembersDto> subinitiativesMembers = new ArrayList<InitiativeMembersDto>();
	
	
	public String getInitiativeId() {
		return initiativeId;
	}
	public void setInitiativeId(String initiativeId) {
		this.initiativeId = initiativeId;
	}
	public String getInitiativeName() {
		return initiativeName;
	}
	public void setInitiativeName(String initiativeName) {
		this.initiativeName = initiativeName;
	}
	public List<MemberDto> getMembers() {
		return members;
	}
	public void setMembers(List<MemberDto> members) {
		this.members = members;
	}
	public List<InitiativeMembersDto> getSubinitiativesMembers() {
		return subinitiativesMembers;
	}
	public void setSubinitiativesMembers(List<InitiativeMembersDto> subinitiativesMembers) {
		this.subinitiativesMembers = subinitiativesMembers;
	}
	
}
