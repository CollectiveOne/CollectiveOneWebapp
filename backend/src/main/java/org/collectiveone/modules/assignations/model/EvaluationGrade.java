package org.collectiveone.modules.assignations.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.assignations.dto.EvaluationGradeDto;
import org.collectiveone.modules.assignations.enums.EvaluationGradeState;
import org.collectiveone.modules.assignations.enums.EvaluationGradeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "evaluation_grades")
public class EvaluationGrade {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private Assignation assignation;
	
	@ManyToOne
	private Evaluator evaluator;
	
	@ManyToOne
	private Receiver receiver;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private EvaluationGradeType type;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "state")
	private EvaluationGradeState state;
	
	@Column(name = "percent")
	private double percent;
		

	public EvaluationGradeDto toDto() {
		EvaluationGradeDto dto = new EvaluationGradeDto();
		
		dto.setId(id.toString());
		if(evaluator != null) dto.setEvaluatorUser(evaluator.getUser().toDto());
		if(receiver != null) dto.setReceiverUser(receiver.getUser().toDto());
		if(type != null) dto.setType(type.toString());
		if(state != null) dto.setState(state.toString());
		dto.setPercent(percent);
		
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

	public Evaluator getEvaluator() {
		return evaluator;
	}

	public void setEvaluator(Evaluator evaluator) {
		this.evaluator = evaluator;
	}

	public Receiver getReceiver() {
		return receiver;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}
	
	public EvaluationGradeState getState() {
		return state;
	}

	public void setState(EvaluationGradeState state) {
		this.state = state;
	}

	public EvaluationGradeType getType() {
		return type;
	}

	public void setType(EvaluationGradeType type) {
		this.type = type;
	}

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}
	
}
