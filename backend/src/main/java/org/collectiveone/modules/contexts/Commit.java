package org.collectiveone.modules.contexts;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
	
	@ManyToOne
	private Perspective trail;
	
	@ManyToOne
	private Commit previousCommit;
	
	

	public Commit(AppUser author, Perspective trail, Commit previousCommit) {
		super();
		this.author = author;
		this.trail = trail;
		this.previousCommit = previousCommit;
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

	public Perspective getTrail() {
		return trail;
	}

	public void setTrail(Perspective trail) {
		this.trail = trail;
	}

	public Commit getPreviousCommit() {
		return previousCommit;
	}

	public void setPreviousCommit(Commit previousCommit) {
		this.previousCommit = previousCommit;
	}
	
}
