package org.collectiveone.modules.uprcl.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "commits")
public class Commit {
	
	@Id
	@Column(name = "id", updatable = false, nullable = false, unique = true)
	private String id;
	
	private String creator;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String message;
	
	@OneToMany
	private List<Commit> parents = new ArrayList<Commit>();
	
	@ManyToOne
	private Content content;	
	
	public Commit() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Commit> getParents() {
		return parents;
	}

	public void setParents(List<Commit> parents) {
		this.parents = parents;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}
	
}
