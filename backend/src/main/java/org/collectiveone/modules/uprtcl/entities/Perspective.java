package org.collectiveone.modules.uprtcl.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.bitcoinj.core.Base58;
import org.collectiveone.modules.ipld.IpldService;
import org.collectiveone.modules.uprtcl.dtos.PerspectiveDto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.ipfs.multihash.Multihash.Type;

@Entity
@Table(name = "perspectives")
@JsonPropertyOrder({ "creatorId", "origin", "timestamp", "contextId", "name", "headId" })
public class Perspective {

	@Id
	@Column(name = "id", updatable = false, nullable = false, unique = true)
	@JsonIgnore
	private byte[] id;

	private String origin;
	
	private String creatorId;

	private Timestamp timestamp;
	
	private byte[] contextId;
	
	private String name;
	
	private byte[] headId;
	
	
	/* working commits are uncommitted changes, there cab be zero or more per user and perspective. */
	@OneToMany
	@JsonIgnore
	private List<Commit> workingCommits = new ArrayList<Commit>();

	@JsonGetter("timestamp")
	public String timestampJson() {
		return timestamp != null ? String.valueOf(timestamp.getTime()) : "0"; 
	}
	
	@JsonGetter("contextId")
	public String contextIdJson() {
		return IpldService.decode(contextId);
	}
	
	@JsonGetter("name")
	public String nameJson() {
		return name != null ? name : ""; 
	}
	
	@JsonGetter("headId")
	public String headIdJson() {
		return IpldService.decode(headId);
	}
	
	public byte[] computeId(io.ipfs.multihash.Multihash.Type t) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(this);	
		return IpldService.hash(json, t);
	}
	
	public PerspectiveDto toDto() throws Exception {
		PerspectiveDto dto = new PerspectiveDto();
		
		dto.setId(Base58.encode(id));
		dto.setOrigin(origin);
		dto.setCreatorId(creatorId);
		dto.setTimestamp(timestamp.getTime());
		dto.setContextId(IpldService.decode(contextId));
		dto.setName(name);
		dto.setHeadId(IpldService.decode(headId));
		
		return dto;
	}
	
	@Override
	public String toString() {
		return "     name: " + name + "\n" + 
			   "       id: " + IpldService.decode(id) + "\n" + 
			   "contextId: " + IpldService.decode(contextId) + "\n" +
			   " headLink: " + IpldService.decode(headId);
	}
	
	public byte[] getId() {
		return id;
	}
	
	public void setId(Type t) throws Exception {
		this.id = this.computeId(t);
	}
	
	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public byte[] getContextId() {
		return contextId;
	}

	public void setContextId(byte[] contextId) {
		this.contextId = contextId;
	}

	public List<Commit> getWorkingCommits() {
		return workingCommits;
	}

	public void setWorkingCommits(List<Commit> workingCommits) {
		this.workingCommits = workingCommits;
	}

	public byte[] getHeadId() {
		return headId;
	}

	public void setHeadId(byte[] headId) {
		this.headId = headId;
	}

}
