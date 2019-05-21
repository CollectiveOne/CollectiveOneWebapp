package org.collectiveone.modules.uprcl.dtos;

public class PerspectiveDto {
	
	private String id;
	private String creatorId;
	private Long timestamp;
	private String contextId;
	private String name;
	private String headId;
	
	public PerspectiveDto() {
		super();
	}
	public PerspectiveDto(String contextId, String name, String headId) {
		super();
		this.timestamp = System.currentTimeMillis();
		this.contextId = contextId;
		this.name = name;
		this.headId = headId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
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
	public String getHeadId() {
		return headId;
	}
	public void setHeadId(String headId) {
		this.headId = headId;
	}
	
}
