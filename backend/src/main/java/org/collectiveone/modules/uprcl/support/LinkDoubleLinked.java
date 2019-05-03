package org.collectiveone.modules.uprcl.support;

import org.collectiveone.modules.uprcl.entities.LinkType;

public class LinkDoubleLinked {
	
	private LinkType type;
	private String perspectiveId;
	private PositionDoubleLinkedList position;
	
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
	public PositionDoubleLinkedList getPosition() {
		return position;
	}
	public void setPosition(PositionDoubleLinkedList position) {
		this.position = position;
	}

}
