package org.collectiveone.modules.assignations;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.collectiveone.modules.tokens.MemberTransfer;
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
	
	@Column(name = "evaluated_percent")
	private double evaluatedPercent;
	
	@Column(name = "assigned_percent")
	private double assignedPercent;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "state")
	private ReceiverState state;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private ReceiverType type;
	
	@OneToOne
	private MemberTransfer transfer;
	
	@Column(name = "revert_approval")
	private Boolean revertApproval; 
	

	public ReceiverDto toDto() {
		ReceiverDto dto = new ReceiverDto();
		
		dto.setId(id.toString());
		dto.setUser(user.toDto());
		dto.setPercent(assignedPercent);
		dto.setEvaluatedPercent(evaluatedPercent);
		dto.setState(state.toString());
		dto.setIsDonor(type == ReceiverType.DONOR);
		if(revertApproval != null) dto.setRevertApproval(revertApproval);
				
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
	
	public double getEvaluatedPercent() {
		return evaluatedPercent;
	}

	public void setEvaluatedPercent(double evaluatedPercent) {
		this.evaluatedPercent = evaluatedPercent;
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

	public ReceiverType getType() {
		return type;
	}

	public void setType(ReceiverType type) {
		this.type = type;
	}

	public MemberTransfer getTransfer() {
		return transfer;
	}

	public void setTransfer(MemberTransfer transfer) {
		this.transfer = transfer;
	}

	public Boolean getRevertApproval() {
		return revertApproval;
	}

	public void setRevertApproval(Boolean revertApproval) {
		this.revertApproval = revertApproval;
	}
	
	
}
