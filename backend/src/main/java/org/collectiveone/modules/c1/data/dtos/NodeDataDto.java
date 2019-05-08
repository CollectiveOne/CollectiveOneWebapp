package org.collectiveone.modules.c1.data.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonPropertyOrder({"text", "links"})
public class NodeDataDto implements JsonDataIf {
	
	private String text;
	private List<PositionedLinkDto> links;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<PositionedLinkDto> getLinks() {
		return links;
	}

	public void setLinks(List<PositionedLinkDto> links) {
		this.links = links;
	}

	@Override
	public String getDataJson() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(this);
	}


}
