package org.collectiveone.modules.contexts.entitiesRedundant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.collectiveone.modules.contexts.entities.CardInP;
import org.collectiveone.modules.contexts.entities.ContextMetadata;
import org.collectiveone.modules.contexts.entities.Perspective;
import org.collectiveone.modules.contexts.entities.Subcontext;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "perspective_cache")
public class PerspectiveCache {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@OneToOne
	private Perspective perspective;

	@ManyToOne
	private ContextMetadata metadata;
	
	@OneToMany
	private List<CardInP> cardsInP = new ArrayList<CardInP>();
	
	@OneToMany
	private List<Subcontext> subcontexts = new ArrayList<Subcontext>();
	
	public PerspectiveCache() {
		super();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Perspective getPerspective() {
		return perspective;
	}

	public void setPerspective(Perspective perspective) {
		this.perspective = perspective;
	}

	public ContextMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(ContextMetadata metadata) {
		this.metadata = metadata;
	}

	public List<CardInP> getCardsInP() {
		return cardsInP;
	}

	public void setCardsInP(List<CardInP> cardsInP) {
		this.cardsInP = cardsInP;
	}

	public List<Subcontext> getSubcontexts() {
		return subcontexts;
	}

	public void setSubcontexts(List<Subcontext> subcontexts) {
		this.subcontexts = subcontexts;
	}
	
	
}
