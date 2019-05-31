package org.collectiveone.modules.c1.data.dtos;

public class PositionDL {
	
	private String before;
	private String after;
	
	public PositionDL() {
		super();
	}
	public PositionDL(String before, String after) {
		super();
		this.before = before;
		this.after = after;
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
