package org.collectiveone.modules.c1.data.dtos;

import org.collectiveone.modules.uprcl.dtos.PerspectiveDto;

import com.fasterxml.jackson.annotation.JsonGetter;

public class LinkDto {
	
	protected PerspectiveDto parent;
	protected PerspectiveDto perspective;
	
	@JsonGetter("parent")
	public String parentForJsonGetter() {
		return parent.getId();
	}
	
	@JsonGetter("perspective")
	public String perspectiveForJsonGetter() {
		return perspective.getId();
	}
		
	public PerspectiveDto getParent() {
		return parent;
	}
	public void setParent(PerspectiveDto parent) {
		this.parent = parent;
	}
	public PerspectiveDto getPerspective() {
		return perspective;
	}
	public void setPerspective(PerspectiveDto perspective) {
		this.perspective = perspective;
	}
	
}
