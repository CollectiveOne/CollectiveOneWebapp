package org.collectiveone.modules.c1.data.entities;

import java.security.MessageDigest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.bitcoinj.core.Base58;
import org.collectiveone.modules.c1.data.dtos.DataDto;
import org.collectiveone.modules.c1.data.enums.DataType;

import com.fasterxml.jackson.core.JsonProcessingException;

@Entity
@Table(name = "data")
public class Data {
	
	@Id
	@Column(name = "id", updatable = false, nullable = false, unique = true)
	private String id;

	@Enumerated(EnumType.STRING)
	private DataType type;
	
	@ManyToOne
	private TextData textData;
	
	@ManyToOne
	private NodeData nodeData;
	
	
	public String computeId() {
		try {
			MessageDigest digestInstance = MessageDigest.getInstance("SHA-256");
			
			String json = "";
					
			switch (type) {
				case TEXT:
					json = textData.toDto().getDataJson();
				break;
				
				case NODE:
					json = nodeData.toDto().getDataJson();
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
	
	public DataDto toDto() throws JsonProcessingException {
		
		DataDto dto = new DataDto();
		
		switch (type) {
		
		case TEXT:
			dto.setJsonData(textData.toDto().getDataJson());
		break;
		
		case NODE:
			dto.setJsonData(nodeData.toDto().getDataJson());
		break;
		
		}
		
		dto.setId(id);
		dto.setType(type);
		
		return dto;
	}
	
	@Override
	public String toString() {
		try {
			return "  id: " + id + "\n" + 
				   "type: " + type.toString() + "\n" +
				   "json: " + this.toDto().getJsonData();
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	public TextData getTextData() {
		return textData;
	}
	public void setTextData(TextData textContent) {
		this.textData = textContent;
	}
	public NodeData getNodeData() {
		return nodeData;
	}
	public void setNodeData(NodeData nodeContent) {
		this.nodeData = nodeContent;
	}
	
	
}
