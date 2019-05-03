package org.collectiveone.modules.uprcl.dtos;

import java.util.SortedMap;
import java.util.TreeMap;

public class CommitDto {

	private String id;
	private String creator;
	private String message;
	private Long nonce;
	private SortedMap<String, CommitDto> parents = new TreeMap<String, CommitDto>();
	private ContentDto content;
	
	public CommitDto() {
		super();
	}
	
	public CommitDto(String message) {
		super();
		this.message = message;
	}
	
	public String getId() {
		return id;
	}
	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
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
	
	public Long getNonce() {
		return nonce;
	}

	public void setNonce(Long nonce) {
		this.nonce = nonce;
	}

	public SortedMap<String, CommitDto> getParents() {
		return parents;
	}

	public void setParents(SortedMap<String, CommitDto> parents) {
		this.parents = parents;
	}

	public ContentDto getContent() {
		return content;
	}

	public void setContent(ContentDto content) {
		this.content = content;
	}
	
}
