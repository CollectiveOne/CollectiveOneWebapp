package org.collectiveone.modules.c1.data.dtos;

public class LinkDto {
	
	private String link;
	private String before;
	private String after;
	
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
	public String getBefore() {
		return before;
	}
	public void setBefore(String before) {
		this.before = before;
	}
	public String getAfter() {
		return after;
	}
	public void setAfter(String after) {
		this.after = after;
	}
	
}
