package org.collectiveone.modules.uprcl.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.collectiveone.modules.ipld.IpldService;
import org.collectiveone.modules.uprcl.dtos.CommitDto;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "commits")
@JsonPropertyOrder({ "creatorId", "timestamp", "message", "parentsIds", "dataId" })
public class Commit {
	
	@Id
	@Column(name = "id", updatable = false, nullable = false, unique = true)
	@JsonIgnore
	private byte[] id;
	
	private String creatorId;
	
	private Timestamp timestamp;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String message;
	
	@ElementCollection
	private List<byte[]> parentsIds = new ArrayList<byte[]>();
	
	private byte[] dataId;
		
	public Commit() {
		super();
	}
	
	public byte[] computeId(io.ipfs.multihash.Multihash.Type t) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(this);
		return IpldService.hash(json, t);	
	}
	
	@JsonGetter("parentsIds")
	public String parentsIdsJson() throws Exception {
		List<String> ids = new ArrayList<String>();
		for (byte[] parentId : parentsIds) {
			ids.add(IpldService.decode(parentId));
		}
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(ids);
		return json;
	}
	
	@JsonGetter("dataId")
	public String dataIdJson() {
		return IpldService.decode(dataId);
	}
	
	@JsonGetter("timestamp")
	public String timestampJson() {
		return timestamp != null ? String.valueOf(timestamp.getTime()) : "0"; 
	}
	
	public CommitDto toDto() throws JsonProcessingException {
		
		CommitDto dto = new CommitDto();
		
		dto.setId(IpldService.decode(id));
		dto.setCreatorId(creatorId);
		dto.setTimestamp(timestamp.getTime());
		dto.setMessage(message);
		
		for (byte[] parent : parentsIds) {
			dto.getParentsIds().add(IpldService.decode(parent));
		}
		
		dto.setDataId(IpldService.decode(dataId));
		
		return dto;
	}
	
	@Override
	public String toString() {
		return "       id: " + id + "\n" + 
			   "creatorId: " + creatorId + "\n" +
			   " dataLink: " + IpldService.decode(dataId);
	}

	public byte[] getId() {
		return id;
	}

	public void setId(io.ipfs.multihash.Multihash.Type t) throws Exception {
		this.id = this.computeId(t);
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<byte[]> getParentsIds() {
		return parentsIds;
	}

	public void setParentsIds(List<byte[]> parentsIds) {
		this.parentsIds = parentsIds;
	}

	public byte[] getDataId() {
		return dataId;
	}

	public void setDataId(byte[] dataId) {
		this.dataId = dataId;
	}
	
}
