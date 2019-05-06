package org.collectiveone.modules.uprcl.support;

import org.collectiveone.modules.uprcl.entities.LinkType;

public class LinkProxy {
	private LinkType type;
	private String perspectiveId;
	
	public LinkType getType() {
		return type;
	}
	public void setType(LinkType type) {
		this.type = type;
	}
	public String getPerspectiveId() {
		return perspectiveId;
	}
	public void setPerspectiveId(String perspectiveId) {
		this.perspectiveId = perspectiveId;
	}
	
}
