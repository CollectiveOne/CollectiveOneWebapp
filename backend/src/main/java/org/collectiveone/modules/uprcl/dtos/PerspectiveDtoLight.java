package org.collectiveone.modules.uprcl.dtos;

import org.collectiveone.modules.uprcl.entities.PerspectiveType;

public class PerspectiveDtoLight {
	
	private String id;
	private PerspectiveType type;
	private String contextId;
	private String name;
	private String headLink;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContextId() {
		return contextId;
	}
	public void setContextId(String contextId) {
		this.contextId = contextId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHeadLink() {
		return headLink;
	}
	public void setHeadLink(String headLink) {
		this.headLink = headLink;
	}
	public PerspectiveType getType() {
		return type;
	}
	public void setType(PerspectiveType type) {
		this.type = type;
	}
	
	
}
