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
	
	@Enumerated(EnumType.STRING)
	private StageAction action;
	
	@ManyToOne
	private Subcontext subcontext;
	
	public StageSubcontext() {
		super();
	}
	
	public StageSubcontext(Commit commit, StageAction action, Subcontext subcontext) {
		super();
		this.action = action;
		this.subcontext = subcontext;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public StageAction getAction() {
		return action;
	}

	public void setAction(StageAction action) {
		this.action = action;
	}

	public Subcontext getSubcontext() {
		return subcontext;
	}

	public void setSubcontext(Subcontext subcontext) {
		this.subcontext = subcontext;
	}

}
