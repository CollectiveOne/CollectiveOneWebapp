package org.collectiveone.modules.assignations;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.users.AppUser;
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
	
	@ManyToOne
	private AppUser user;
	
	@Column(name = "assignedPercent")
	private double assignedPercent;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "state")
	private ReceiverState state;

	public ReceiverDto toDto() {
		ReceiverDto dto = new ReceiverDto();
		
		dto.setId(id.toString());
		dto.setUser(user.toDto());
		dto.setPercent(assignedPercent);
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

	public double getAssignedPercent() {
		return assignedPercent;
	}

	public void setAssignedPercent(double percent) {
		this.assignedPercent = percent;
	}

	public ReceiverState getState() {
		return state;
	}

	public void setState(ReceiverState state) {
		this.state = state;
	}
	
}
