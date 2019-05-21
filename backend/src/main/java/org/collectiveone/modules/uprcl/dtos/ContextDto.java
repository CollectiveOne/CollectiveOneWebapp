package org.collectiveone.modules.uprcl.dtos;

import java.util.ArrayList;
import java.util.List;

public class ContextDto {
	private String id;
	private String creatorId;
	private Long timestamp;
	private Long nonce;
	private List<PerspectiveDto> perspectives = new ArrayList<PerspectiveDto>();
	
	
	@Override
	public String toString() {
		return "     id: " + id + "\n" + 
			   "creator: " + creatorId;
	}

	public ContextDto() {
		super();
		this.timestamp = System.currentTimeMillis();
		this.nonce = 0L;
	}
	
	public ContextDto(String id) {
		super();
		this.id = id;
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
	public void setCreatorId(String creator) {
		this.creatorId = creator;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public Long getNonce() {
		return nonce;
	}
	public void setNonce(Long nonce) {
		this.nonce = nonce;
	}
	public List<PerspectiveDto> getPerspectives() {
		return perspectives;
	}
	public void setPerspectives(List<PerspectiveDto> perspectives) {
		this.perspectives = perspectives;
	}
	
	
}
