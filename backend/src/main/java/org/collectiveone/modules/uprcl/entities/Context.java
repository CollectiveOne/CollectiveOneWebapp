package org.collectiveone.modules.uprcl.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.collectiveone.modules.ipld.Ipld;
import org.collectiveone.modules.uprcl.dtos.ContextDto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.ipfs.multibase.Multibase.Base;

@Entity
@Table(name = "contexts")
@JsonPropertyOrder({ "creator", "timestamp", "nonce" })
public class Context {
	
	@Id
	@Column(name = "id", updatable = false, nullable = false, unique = true)
	@JsonIgnore
	private String id;
	
	private String creator;
	
	private Timestamp timestamp;
	
	private Long nonce;
	
	public Context() {
		super();
	}
	
	public Context(String creator, Timestamp timestamp, Long nonce) {
		super();
		this.creator = creator;
		this.timestamp = timestamp;
		this.nonce = nonce;
	}
	
	public String computeId(io.ipfs.multihash.Multihash.Type t, Base b) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(this);
		return Ipld.hash(json, t, b);
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
		
		dto.setId(id);
		dto.setCreatorId(creator);
		dto.setNonce(nonce);
		dto.setTimestamp(timestamp.getTime());
		
		return dto;
	}
	
	
	@Override
	public String toString() {
		return "     id: " + id + "\n" +
			   "creator: " + creator;
	}
	
	public String getId() {
		return id;
	}

	public void setId(io.ipfs.multihash.Multihash.Type t, Base b) throws Exception {
		this.id = this.computeId(t, b);
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
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
