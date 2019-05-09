package org.collectiveone.modules.c1.data.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface JsonDataIf {
	
	@JsonIgnore
	public String getDataJson() throws JsonProcessingException;
}
