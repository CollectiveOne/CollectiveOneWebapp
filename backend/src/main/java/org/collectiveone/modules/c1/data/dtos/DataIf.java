package org.collectiveone.modules.c1.data.dtos;

import org.collectiveone.modules.c1.data.DataType;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface DataIf {
	
	public String getId();
	public void setId(String id);
	public DataType getType();
	public void setType(DataType type);
	public String getDataJson() throws JsonProcessingException;
	
}
