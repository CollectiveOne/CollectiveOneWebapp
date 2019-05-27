package org.collectiveone.modules.uprtcl.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.collectiveone.modules.ipld.IpldService;
import org.collectiveone.modules.uprtcl.dtos.ContextDto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.ipfs.multibase.Multibase.Base;

@Entity
@Table(name = "contexts")
@JsonPropertyOrder({ "creatorId", "timestamp", "nonce" })
public class Context {
	
	@Id
	@Column(name = "id", updatable = false, nullable = false, unique = true)
	@JsonIgnore
	private byte[] id;
	
	private String creatorId;
	
	private Timestamp timestamp;
	
	private Long nonce;
	
	public Context() {
		super();
	}
	
	@JsonGetter("timestamp")
	public String timestampJson() {
		return timestamp != null ? String.valueOf(timestamp.getTime()) : "0"; 
	}
	
	@JsonGetter("nonce")
	public String nonceJson() {
		return nonce != null ? nonce.toString() : "0"; 
	}
	
	public Context(String creator, Timestamp timestamp, Long nonce) {
		super();
		this.creatorId = creator;
		this.timestamp = timestamp;
		this.nonce = nonce;
	}
	
	public byte[] computeId(io.ipfs.multihash.Multihash.Type t) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(this);
		return IpldService.hash(json, t);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Context other = (Context) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public ContextDto toDto() {
		ContextDto dto = new ContextDto();
		
		dto.setId(IpldService.decode(id));
		dto.setCreatorId(creatorId);
		dto.setNonce(nonce);
		dto.setTimestamp(timestamp.getTime());
		
		return dto;
	}
	
	
	@Override
	public String toString() {
		return "     id: " + IpldService.decode(id) + "\n" +
			   "creator: " + creatorId;
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

	public Long getNonce() {
		return nonce;
	}

	public void setNonce(Long nonce) {
		this.nonce = nonce;
	}
	
}
