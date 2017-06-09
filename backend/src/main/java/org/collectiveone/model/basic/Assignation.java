package org.collectiveone.model.basic;

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

import org.collectiveone.model.enums.AssignationState;
import org.collectiveone.model.enums.AssignationType;
import org.collectiveone.model.support.Bill;
import org.collectiveone.model.support.Evaluator;
import org.collectiveone.model.support.Receiver;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table( name = "assignations" )
public class Assignation {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private Initiative initiative;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private AssignationType type;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "state")
	private AssignationState state;
	
	@OneToMany
	private List<Bill> bills = new ArrayList<Bill>();
	
	@OneToMany
	private List<Receiver> receivers = new ArrayList<Receiver>();
	
	@OneToMany
	private List<Evaluator> evaluators = new ArrayList<Evaluator>();

	
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

	public AssignationType getType() {
		return type;
	}

	public void setType(AssignationType type) {
		this.type = type;
	}

	public AssignationState getState() {
		return state;
	}

	public void setState(AssignationState state) {
		this.state = state;
	}

	public List<Bill> getBills() {
		return bills;
	}

	public void setBills(List<Bill> bills) {
		this.bills = bills;
	}

	public List<Receiver> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<Receiver> receivers) {
		this.receivers = receivers;
	}

	public List<Evaluator> getEvaluators() {
		return evaluators;
	}

	public void setEvaluators(List<Evaluator> evaluators) {
		this.evaluators = evaluators;
	}
	
	
}
