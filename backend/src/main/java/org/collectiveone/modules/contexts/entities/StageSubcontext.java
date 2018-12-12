package org.collectiveone.modules.contexts.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "stages_contexts")
public class StageSubcontext {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private Commit commit;
	
	@Enumerated(EnumType.STRING)
	private StageAction action;
	
	@ManyToOne
	private Perspective perspective;
	
	@ManyToOne
	private Perspective beforePerspective;
		
	@ManyToOne
	private Perspective afterPerspective;
	
	public StageSubcontext() {
		super();
	}

	public StageSubcontext(Commit commit, StageAction action, Perspective perspective) {
		super();
		this.commit = commit;
		this.action = action;
		this.perspective = perspective;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Commit getCommit() {
		return commit;
	}

	public void setCommit(Commit commit) {
		this.commit = commit;
	}

	public StageAction getAction() {
		return action;
	}

	public void setAction(StageAction action) {
		this.action = action;
	}

	public Perspective getPerspective() {
		return perspective;
	}

	public void setPerspective(Perspective perspective) {
		this.perspective = perspective;
	}

}
