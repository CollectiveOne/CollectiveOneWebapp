package org.collectiveone.modules.c1.data.dtos;

import org.collectiveone.modules.c1.data.DataType;

public class DataDto {
	
	private String id;
	private DataType type;
	private String jsonData;
	
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
	public String getJsonData() {
		return jsonData;
	}
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}
	
}
