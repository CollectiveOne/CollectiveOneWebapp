package org.collectiveone.modules.c1.data.dtos;

import org.collectiveone.modules.c1.data.DataType;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DataDto {
	
	@JsonIgnore
	private String id;
	
	@JsonIgnore
	private DataType type;
	
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
}
