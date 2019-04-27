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
import org.collectiveone.modules.uprcl.entities.ext.DataType;
import org.collectiveone.modules.uprcl.entities.ext.TextContent;

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
			
			switch (type) {
				case TEXT:
					return Base58.encode(digestInstance.digest(textContent.getText().getBytes()));
					
				default: 
					throw new Exception("unexpected type: " + type.toString());
			}	
		} catch (Exception e) {
			// TODO
		}
		return null;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
