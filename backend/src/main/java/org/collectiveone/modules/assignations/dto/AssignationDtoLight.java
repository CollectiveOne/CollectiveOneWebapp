package org.collectiveone.modules.assignations.dto;

import java.util.ArrayList;
import java.util.List;

public class AssignationDtoLight {
	
	private String id;
	private String type;
	private String motive;
	private String notes;
	private String state;
	private String modelSectionId;
	private String modelSectionName;
	private Long creationDate;
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
	public String getModelSectionId() {
		return modelSectionId;
	}
	public void setModelSectionId(String modelSectionId) {
		this.modelSectionId = modelSectionId;
	}
	public String getModelSectionName() {
		return modelSectionId;
	}
	public void setModelSectionName(String modelSectionName) {
		this.modelSectionName = modelSectionName;
	}
	public Long getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}
	public List<BillDto> getAssets() {
		return assets;
	}
	public void setAssets(List<BillDto> assets) {
		this.assets = assets;
	}
	
	
}
