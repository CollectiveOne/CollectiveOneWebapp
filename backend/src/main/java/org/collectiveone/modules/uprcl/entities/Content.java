package org.collectiveone.modules.uprcl.entities;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.bitcoinj.core.Base58;
import org.collectiveone.modules.uprcl.dtos.ContentDto;
import org.hibernate.annotations.SortNatural;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "content")
@JsonPropertyOrder({ "data", "links" })
public class Content {
	
	@Id
	@Column(name = "id", updatable = false, nullable = false, unique = true)
	@JsonIgnore
	private String id;
	
	@ManyToOne
	private Data data;
	
	@OneToMany
	@MapKey(name="id")
	@SortNatural
	private SortedMap<String, Link> links = new TreeMap<String, Link>();
	
	public Content() {
		super();
	}
	
	public String computeId() {
		try {
			MessageDigest digestInstance = MessageDigest.getInstance("SHA-256");
			ObjectMapper objectMapper = new ObjectMapper();
			
			String json = objectMapper.writeValueAsString(this);
			byte[] hash = digestInstance.digest(json.getBytes());
			
			return Base58.encode(hash);	
		} catch (Exception e) {
			// TODO
		}
		return null;
	}
	
	public ContentDto toDto() {
		ContentDto dto = new ContentDto();
		
		dto.setId(id);
		dto.setData(data.toDto());
		
		for (Map.Entry<String, Link> linkEntry : links.entrySet()) {
			dto.getLinks().put(linkEntry.getKey(), linkEntry.getValue().toDto());
		}
		
		return dto;
	}
	
	@JsonGetter("data")
	public String getDataId() {
		return data.getId(); 
	}
	
	@JsonGetter("links")
	public String getParentsArray() {
		return Arrays.toString(links.keySet().toArray()); 
	}

	public String getId() {
		return id;
	}

	public void setId() {
		this.id = this.computeId();
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public SortedMap<String, Link> getLinks() {
		return links;
	}

	public void setLinks(SortedMap<String, Link> links) {
		this.links = links;
	}

}
