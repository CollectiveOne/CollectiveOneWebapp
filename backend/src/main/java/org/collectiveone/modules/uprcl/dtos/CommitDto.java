package org.collectiveone.modules.uprcl.dtos;

import java.util.SortedMap;
import java.util.TreeMap;

import org.collectiveone.modules.c1.data.dtos.DataDto;

public class CommitDto {

	private String id;
	private String creator;
	private String message;
	private Long nonce;
	private SortedMap<String, CommitDto> parents = new TreeMap<String, CommitDto>();
	private DataDto data;
	
	
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

	public DataDto getData() {
		return data;
	}

	public void setData(DataDto data) {
		this.data = data;
	}
	
}
