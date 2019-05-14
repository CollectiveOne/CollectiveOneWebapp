package org.collectiveone.modules.c1.data.dtos;

import org.collectiveone.modules.uprcl.entities.PerspectiveType;

public class PerspectiveDtoLight {
	
	private PerspectiveType type;
	private String contextId;
	private String name;
	private String commitLink;
	
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
	public String getCommitLink() {
		return commitLink;
	}
	public void setCommitLink(String commitLink) {
		this.commitLink = commitLink;
	}
	public PerspectiveType getType() {
		return type;
	}
	public void setType(PerspectiveType type) {
		this.type = type;
	}
	
	
}
