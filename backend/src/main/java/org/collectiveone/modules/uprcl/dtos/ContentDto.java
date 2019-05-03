package org.collectiveone.modules.uprcl.dtos;

import java.util.SortedMap;
import java.util.TreeMap;

public class ContentDto {

	private String id;
	private DataDto data;
	private SortedMap<String, LinkDto> links = new TreeMap<String, LinkDto>();
	
	public ContentDto() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DataDto getData() {
		return data;
	}

	public void setData(DataDto data) {
		this.data = data;
	}

	public SortedMap<String, LinkDto> getLinks() {
		return links;
	}

	public void setLinks(SortedMap<String, LinkDto> links) {
		this.links = links;
	}
	
}
