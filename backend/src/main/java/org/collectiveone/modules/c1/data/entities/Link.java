package org.collectiveone.modules.c1.data.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Link {
	
	@Embedded
	@AttributeOverrides({
        @AttributeOverride(name="network", column=@Column(name="link_network")),
        @AttributeOverride(name="networkAddress", column=@Column(name="link_network_address")),
        @AttributeOverride(name="element", column=@Column(name="link_element"))
    })
	private ExternalLink link;
	
	@Embedded
	@AttributeOverrides({
        @AttributeOverride(name="network", column=@Column(name="before_network")),
        @AttributeOverride(name="networkAddress", column=@Column(name="before_network_address")),
        @AttributeOverride(name="element", column=@Column(name="before_element"))
    })
	private ExternalLink before;
	
	@Embedded
	@AttributeOverrides({
        @AttributeOverride(name="network", column=@Column(name="after_network")),
        @AttributeOverride(name="networkAddress", column=@Column(name="after_network_address")),
        @AttributeOverride(name="element", column=@Column(name="after_element"))
    })
	private ExternalLink after;
	
	public ExternalLink getLink() {
		return link;
	}
	public void setLink(ExternalLink link) {
		this.link = link;
	}
	public ExternalLink getBefore() {
		return before;
	}
	public void setBefore(ExternalLink before) {
		this.before = before;
	}
	public ExternalLink getAfter() {
		return after;
	}
	public void setAfter(ExternalLink after) {
		this.after = after;
	}
	
}
