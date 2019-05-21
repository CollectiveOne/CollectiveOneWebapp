package org.collectiveone.modules.uprcl.dtos;

import java.util.ArrayList;
import java.util.List;

public class CommitDto {

	private String id;
	private String creatorId;
	private Long timestamp;
	private String message;
	private List<String> parentsIds = new ArrayList<String>();
	private String dataId;
	
	@Override
	public String toString() {
		return "       id: " + id + "\n" + 
			   "creatorId: " + creatorId + "\n" +
			   " dataLink: " + dataId;
	}
	
	public CommitDto() {
		super();
	}

	public CommitDto(String message, List<String> parentsIds, String dataId) {
		super();
		this.timestamp = System.currentTimeMillis();
		this.message = message;
		this.parentsIds = parentsIds;
		this.dataId = dataId;
	}

	public String getId() {
		return id;
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

	public List<String> getParentsIds() {
		return parentsIds;
	}

	public void setParentsIds(List<String> parentsIds) {
		this.parentsIds = parentsIds;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	
}
