package org.collectiveone.modules.assignations.dto;

import java.util.ArrayList;
import java.util.List;

public class InitiativeAssignationsDto {

	private String initiativeId;
	private String initiativeName;
	private List<AssignationDto> assignations = new ArrayList<AssignationDto>();
	private List<AssignationDto> subinitiativesAssignations = new ArrayList<AssignationDto>();
	
	
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
	public List<AssignationDto> getAssignations() {
		return assignations;
	}
	public void setAssignations(List<AssignationDto> assignations) {
		this.assignations = assignations;
	}
	public List<AssignationDto> getSubinitiativesAssignations() {
		return subinitiativesAssignations;
	}
	public void setSubinitiativesAssignations(List<AssignationDto> subinitiativesAssignations) {
		this.subinitiativesAssignations = subinitiativesAssignations;
	}
	
	
}
