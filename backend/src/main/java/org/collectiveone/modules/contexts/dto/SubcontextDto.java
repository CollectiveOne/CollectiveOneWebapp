package org.collectiveone.modules.contexts.dto;

import org.collectiveone.modules.users.AppUserDto;

public class SubcontextDto {

	private String id;
	private String onPerspectiveId;
	private String perspectiveId;
	private AppUserDto adder;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOnPerspectiveId() {
		return onPerspectiveId;
	}
	public void setOnPerspectiveId(String onPerspectiveId) {
		this.onPerspectiveId = onPerspectiveId;
	}
	public String getPerspectiveId() {
		return perspectiveId;
	}
	public void setPerspectiveId(String perspectiveId) {
		this.perspectiveId = perspectiveId;
	}
	public AppUserDto getAdder() {
		return adder;
	}
	public void setAdder(AppUserDto adder) {
		this.adder = adder;
	}
		
}
