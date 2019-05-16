package org.collectiveone.modules.uprcl.dtos;

import java.util.ArrayList;
import java.util.List;

public class CommitDto {

	private String id;
	private String creatorId;
	private Long timestamp;
	private String message;
	private List<String> parentsLinks = new ArrayList<String>();
	private String dataLink;
	
	@Override
	public String toString() {
		return "       id: " + id + "\n" + 
			   "creatorId: " + creatorId + "\n" +
			   " dataLink: " + dataLink;
	}
	
	public CommitDto() {
		super();
	}

	public CommitDto(String message, List<String> parentsLinks, String dataLink) {
		super();
		this.timestamp = System.currentTimeMillis();
		this.message = message;
		this.parentsLinks = parentsLinks;
		this.dataLink = dataLink;
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

	public List<String> getParentsLinks() {
		return parentsLinks;
	}

	public void setParentsLinks(List<String> parentsLinks) {
		this.parentsLinks = parentsLinks;
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
	
	public String getDataLink() {
		return dataLink;
	}

	public void setDataLink(String dataLink) {
		this.dataLink = dataLink;
	}

}
