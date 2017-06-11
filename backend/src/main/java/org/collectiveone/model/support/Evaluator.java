package org.collectiveone.model.support;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.model.basic.AppUser;
import org.collectiveone.model.basic.Assignation;
import org.collectiveone.model.enums.EvaluatorState;
import org.collectiveone.web.dto.EvaluatorDto;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table( name = "evaluators" )
public class Evaluator {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private Assignation assignation;
	
	@ManyToOne
	private AppUser user;
	
	@Column(name = "weight")
	private double weight;
	
	@Enumerated(EnumType.STRING)
	private EvaluatorState state;

	
	public EvaluatorDto toDto() {
		EvaluatorDto dto = new EvaluatorDto();
		
		dto.setId(id.toString());
		dto.setUser(user.toDto());
		dto.setWeight(weight);
		dto.setState(state.toString());
		
		return dto;
	}
	
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Assignation getAssignation() {
		return assignation;
	}

	public void setAssignation(Assignation assignation) {
		this.assignation = assignation;
	}
	
	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public EvaluatorState getState() {
		return state;
	}

	public void setState(EvaluatorState state) {
		this.state = state;
	}

}
