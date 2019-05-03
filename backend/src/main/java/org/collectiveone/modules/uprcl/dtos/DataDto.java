package org.collectiveone.modules.uprcl.dtos;

import org.collectiveone.modules.c1.data.DataType;
import org.collectiveone.modules.c1.data.TextContentDto;

public class DataDto {
	
	private String id;
	private DataType type;
	private TextContentDto textContent;
	
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
	public TextContentDto getTextContent() {
		return textContent;
	}
	public void setTextContent(TextContentDto textContent) {
		this.textContent = textContent;
	}
	
}
