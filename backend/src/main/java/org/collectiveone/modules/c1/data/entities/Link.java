package org.collectiveone.modules.c1.data.entities;

import javax.persistence.Embeddable;

@Embeddable
public class Link {
	
	private byte[] link;
	
	private byte[] before;
	
	private byte[] after;

	public byte[] getLink() {
		return link;
	}

	public void setLink(byte[] link) {
		this.link = link;
	}

	public byte[] getBefore() {
		return before;
	}

	public void setBefore(byte[] before) {
		this.before = before;
	}

	public byte[] getAfter() {
		return after;
	}

	public void setAfter(byte[] after) {
		this.after = after;
	}
	
}
