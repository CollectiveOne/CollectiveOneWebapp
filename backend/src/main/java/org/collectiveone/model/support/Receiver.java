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

import org.collectiveone.model.basic.Assignation;
import org.collectiveone.model.enums.ReceiverState;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table( name = "receivers" )
public class Receiver {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private Assignation assignation;
	
	@Column(name = "percent")
	private double percent;
	
	@Enumerated(EnumType.STRING)
	private ReceiverState state;

	
	
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

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public ReceiverState getState() {
		return state;
	}

	public void setState(ReceiverState state) {
		this.state = state;
	}
	
}
