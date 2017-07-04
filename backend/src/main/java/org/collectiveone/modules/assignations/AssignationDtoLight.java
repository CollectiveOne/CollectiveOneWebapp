package org.collectiveone.modules.assignations;

import java.util.ArrayList;
import java.util.List;

public class AssignationDtoLight {
	
	private String id;
	private String type;
	private String motive;
	private String notes;
	private String state;
	private String initiativeId;
	private String initiativeName;
	private List<BillDto> assets = new ArrayList<BillDto>();
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMotive() {
		return motive;
	}
	public void setMotive(String motive) {
		this.motive = motive;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
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
	public List<BillDto> getAssets() {
		return assets;
	}
	public void setAssets(List<BillDto> assets) {
		this.assets = assets;
	}
	
	
}
