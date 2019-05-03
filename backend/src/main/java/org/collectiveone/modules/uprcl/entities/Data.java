package org.collectiveone.modules.uprcl.entities;

import java.security.MessageDigest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.bitcoinj.core.Base58;
import org.collectiveone.modules.c1.data.DataType;
import org.collectiveone.modules.c1.data.TextContent;

import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "data")
public class Data {
	
	@Id
	@Column(name = "id", updatable = false, nullable = false, unique = true)
	private String id;

	@Enumerated(EnumType.STRING)
	private DataType type;
	
	@ManyToOne
	private TextContent textContent;
	
	
	public String computeId() {
		try {
			MessageDigest digestInstance = MessageDigest.getInstance("SHA-256");
			ObjectMapper objectMapper = new ObjectMapper();
			
			String json = "";
					
			switch (type) {
				case TEXT:
					json = objectMapper.writeValueAsString(textContent);
				break;
					
				default: 
					throw new Exception("unexpected type: " + type.toString());
			}
			
			byte[] hash = digestInstance.digest(json.getBytes());
			
			return Base58.encode(hash);	
				
		} catch (Exception e) {
			// TODO
		}
		return null;
	}
	
	public String getId() {
		return id;
	}

	public void setId() {
		this.id = this.computeId();
	}

	public DataType getType() {
		return type;
	}
	public void setType(DataType type) {
		this.type = type;
	}
	public TextContent getTextContent() {
		return textContent;
	}
	public void setTextContent(TextContent textContent) {
		this.textContent = textContent;
	}
	
}
