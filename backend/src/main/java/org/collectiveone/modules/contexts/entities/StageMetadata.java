package org.collectiveone.modules.contexts.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "stages_metadata")
public class StageMetadata {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private ContextMetadata contextMetadata;
	
	public StageMetadata() {
		super();
	}
	
	public StageMetadata(Commit _commit, ContextMetadata _metadata) {
		super();
		contextMetadata = _metadata;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public ContextMetadata getContextMetadata() {
		return contextMetadata;
	}

	public void setContextMetadata(ContextMetadata contextMetadata) {
		this.contextMetadata = contextMetadata;
	}

	
}
