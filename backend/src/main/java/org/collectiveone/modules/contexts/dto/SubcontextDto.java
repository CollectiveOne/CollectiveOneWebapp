package org.collectiveone.modules.contexts.dto;

import org.collectiveone.modules.users.AppUserDto;

public class SubcontextDto {

	private String id;
	private PerspectiveDto onPerspective;
	private PerspectiveDto perspective;
	private AppUserDto adder;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public PerspectiveDto getOnPerspective() {
		return onPerspective;
	}
	public void setOnPerspective(PerspectiveDto onPerspective) {
		this.onPerspective = onPerspective;
	}
	public PerspectiveDto getPerspective() {
		return perspective;
	}
	public void setPerspective(PerspectiveDto perspective) {
		this.perspective = perspective;
	}
	public AppUserDto getAdder() {
		return adder;
	}
	public void setAdder(AppUserDto adder) {
		this.adder = adder;
	}
		
}
