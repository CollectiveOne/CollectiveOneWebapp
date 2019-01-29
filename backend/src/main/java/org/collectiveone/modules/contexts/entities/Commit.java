package org.collectiveone.modules.contexts.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "commits")
public class Commit {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private AppUser author;
	
	@OneToMany
	private List<Commit> parents;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) 	
	private List<StageElement> elementsStaged = new ArrayList<StageElement>();
	
	
	public Commit() {
		super();
	}
	
	public Commit(AppUser author) {
		super();
		this.author = author;
	}


	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public AppUser getAuthor() {
		return author;
	}

	public void setAuthor(AppUser author) {
		this.author = author;
	}
	
	public List<Commit> getParents() {
		return parents;
	}

	public void setParents(List<Commit> parents) {
		this.parents = parents;
	}

	public List<StageElement> getElementsStaged() {
		return elementsStaged;
	}

	public void setElementsStaged(List<StageElement> elementsStaged) {
		this.elementsStaged = elementsStaged;
	}
	
}
