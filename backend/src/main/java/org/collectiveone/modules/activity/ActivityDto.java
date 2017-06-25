package org.collectiveone.modules.activity;

import org.collectiveone.modules.initiatives.InitiativeDto;

public class ActivityDto {

	private String type;
	private InitiativeDto initiative;
	private InitiativeDto subInitiative;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public InitiativeDto getInitiative() {
		return initiative;
	}
	public void setInitiative(InitiativeDto initiative) {
		this.initiative = initiative;
	}
	public InitiativeDto getSubInitiative() {
		return subInitiative;
	}
	public void setSubInitiative(InitiativeDto subInitiative) {
		this.subInitiative = subInitiative;
	}
	
}
