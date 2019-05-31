package org.collectiveone.modules.c1.data.dtos;

public class LinkDto {
	
	private String link;
	private PositionDL position;
	
	public LinkDto() {
		super();
	}
	
	public LinkDto(String link) {
		super();
		this.link = link;
	}
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

	public PositionDL getPosition() {
		return position;
	}

	public void setPosition(PositionDL position) {
		this.position = position;
	}
	
	
}
