package org.collectiveone.model.support;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.collectiveone.model.basic.Initiative;
import org.collectiveone.model.enums.InitiativeRelationshipType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table( name = "initiatives_relationships" )
public class InitiativeRelationship {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private Initiative initiative;
	
	@ManyToOne
	private Initiative ofInitiative;
	
	@Enumerated(EnumType.STRING)
	private InitiativeRelationshipType type;
	
	@OneToMany(mappedBy = "relationship")
	private List<InitiativeTransfer> tokensTransfers = new ArrayList<InitiativeTransfer>();

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Initiative getInitiative() {
		return initiative;
	}

	public void setInitiative(Initiative initiative) {
		this.initiative = initiative;
	}

	public Initiative getOfInitiative() {
		return ofInitiative;
	}

	public void setOfInitiative(Initiative ofInitiative) {
		this.ofInitiative = ofInitiative;
	}

	public InitiativeRelationshipType getType() {
		return type;
	}

	public void setType(InitiativeRelationshipType type) {
		this.type = type;
	}

	public List<InitiativeTransfer> getTokensTransfers() {
		return tokensTransfers;
	}

	public void setTokensTransfers(List<InitiativeTransfer> tokensTransfers) {
		this.tokensTransfers = tokensTransfers;
	}
	
}
