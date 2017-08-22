package org.collectiveone.modules.model;

import java.util.ArrayList;
import java.util.List;

public class ModelDto {

	private String initiativeId;
	private List<ModelViewDto> views = new ArrayList<ModelViewDto>();
	
	public String getInitiativeId() {
		return initiativeId;
	}
	public void setInitiativeId(String initiativeId) {
		this.initiativeId = initiativeId;
	}
	public List<ModelViewDto> getViews() {
		return views;
	}
	public void setViews(List<ModelViewDto> views) {
		this.views = views;
	}
	
}
