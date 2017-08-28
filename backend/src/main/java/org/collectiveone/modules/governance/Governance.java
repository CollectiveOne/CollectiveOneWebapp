package org.collectiveone.modules.governance;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.collectiveone.modules.initiatives.Initiative;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity(name = "decision_realms")
public class Governance {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@OneToOne
	private Initiative initiative;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private GovernanceType type;

	@OneToMany(mappedBy = "governance")
	private List<DecisionMaker> decisionMakers = new ArrayList<DecisionMaker>();

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

	public GovernanceType getType() {
		return type;
	}

	public void setType(GovernanceType type) {
		this.type = type;
	}

	public List<DecisionMaker> getDecisionMakers() {
		return decisionMakers;
	}

	public void setDecisionMakers(List<DecisionMaker> decisionMakers) {
		this.decisionMakers = decisionMakers;
	}

}
