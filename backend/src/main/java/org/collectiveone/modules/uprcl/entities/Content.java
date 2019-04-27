package org.collectiveone.modules.uprcl.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.collectiveone.modules.uprcl.entities.ext.OrderedLink;

@Entity
@Table(name = "content")
public class Content {
	
	@Id
	@Column(name = "id", updatable = false, nullable = false, unique = true)
	private String id;
	
	@ManyToOne
	private Data data;
	
	@OneToMany
	private Set<OrderedLink> links;
	
	public Content() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public Set<OrderedLink> getLinks() {
		return links;
	}

	public void setLinks(Set<OrderedLink> links) {
		this.links = links;
	}
	
}
