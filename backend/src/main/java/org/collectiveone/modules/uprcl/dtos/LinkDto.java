package org.collectiveone.modules.uprcl.dtos;

import org.collectiveone.modules.c1.data.PositionType;
import org.collectiveone.modules.uprcl.entities.LinkType;

public class LinkDto {

	private String id;
	private LinkType type;
	private PerspectiveDto perspective;
	private PositionType positionType;
	private PerspectiveDto before;
	private PerspectiveDto after;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public LinkType getType() {
		return type;
	}
	public void setType(LinkType type) {
		this.type = type;
	}
	public PerspectiveDto getPerspective() {
		return perspective;
	}
	public void setPerspective(PerspectiveDto perspective) {
		this.perspective = perspective;
	}
	public PositionType getPositionType() {
		return positionType;
	}
	public void setPositionType(PositionType positionType) {
		this.positionType = positionType;
	}
	public PerspectiveDto getBefore() {
		return before;
	}
	public void setBefore(PerspectiveDto before) {
		this.before = before;
	}
	public PerspectiveDto getAfter() {
		return after;
	}
	public void setAfter(PerspectiveDto after) {
		this.after = after;
	}
	
}
