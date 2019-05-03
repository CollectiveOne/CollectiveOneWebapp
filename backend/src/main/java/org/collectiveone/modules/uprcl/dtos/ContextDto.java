package org.collectiveone.modules.uprcl.dtos;

import java.util.ArrayList;
import java.util.List;

public class ContextDto {
	private String id;
	private String creator;
	private Long timestamp;
	private Long nonce;
	private List<PerspectiveDto> perspectives = new ArrayList<PerspectiveDto>();
	
	public ContextDto() {
		super();
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
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
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
