package org.collectiveone.modules.contexts;

import java.util.List;
import java.util.UUID;

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
	
	@ManyToOne
	private Perspective perspective;
	
	@ManyToOne
	private Commit previousCommit;
	
	@OneToMany(mappedBy="commit")
	private List<StageSubcontext> subcontextStaged;
	
	@OneToMany(mappedBy="commit")
	private List<StageCard> cardsStaged;
	
	

	public Commit(AppUser author, Perspective trail, Commit previousCommit) {
		super();
		this.author = author;
		this.perspective = trail;
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

	public Perspective getPerspective() {
		return perspective;
	}

	public void setPerspective(Perspective perspective) {
		this.perspective = perspective;
	}

	public Commit getPreviousCommit() {
		return previousCommit;
	}

	public void setPreviousCommit(Commit previousCommit) {
		this.previousCommit = previousCommit;
	}

	public List<StageSubcontext> getSubcontextStaged() {
		return subcontextStaged;
	}

	public void setSubcontextStaged(List<StageSubcontext> subcontextStaged) {
		this.subcontextStaged = subcontextStaged;
	}

	public List<StageCard> getCardsStaged() {
		return cardsStaged;
	}

	public void setCardsStaged(List<StageCard> cardsStaged) {
		this.cardsStaged = cardsStaged;
	}
	
	
	
}
