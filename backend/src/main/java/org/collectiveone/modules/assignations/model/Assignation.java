package org.collectiveone.modules.assignations.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.collectiveone.modules.assignations.dto.AssignationDto;
import org.collectiveone.modules.assignations.dto.AssignationDtoLight;
import org.collectiveone.modules.assignations.enums.AssignationState;
import org.collectiveone.modules.assignations.enums.AssignationType;
import org.collectiveone.modules.initiatives.model.Initiative;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

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
	
	@Column(name = "motive")
	private String motive;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column(name = "notes")
	private String notes;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private AssignationType type;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "state")
	private AssignationState state;
	
	@Column(name = "max_closure_date")
	private Timestamp maxClosureDate;
	
	@Column(name = "min_closure_date")
	private Timestamp minClosureDate;
	
	@OneToMany(mappedBy="assignation", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Bill> bills = new ArrayList<Bill>();
	
	@OneToMany(mappedBy="assignation", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Receiver> receivers = new ArrayList<Receiver>();
	
	@OneToMany(mappedBy="assignation", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Evaluator> evaluators = new ArrayList<Evaluator>();

	
	public AssignationDtoLight toDtoLight() {
		AssignationDtoLight dto = new AssignationDtoLight();
		
		dto.setId(id.toString());
		dto.setType(type.toString());
		dto.setMotive(motive);
		dto.setNotes(notes);
		dto.setState(state.toString());
		
		for(Bill bill : bills) {
			dto.getAssets().add(bill.toDto());
		}
		
		return dto;
	}
	
	public AssignationDto toDto() {
		AssignationDto dto = new AssignationDto();
		
		dto.setId(id.toString());
		dto.setType(type.toString());
		dto.setMotive(motive);
		dto.setNotes(notes);
		dto.setState(state.toString());
		
		for(Bill bill : bills) {
			dto.getAssets().add(bill.toDto());
		}
		
		for(Receiver receiver : receivers) {
			dto.getReceivers().add(receiver.toDto());
		}
		
		if(type == AssignationType.PEER_REVIEWED) {
			for(Evaluator evaluator : evaluators) {
				dto.getEvaluators().add(evaluator.toDto());
			}
		}
		
		return dto;
	}
	
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
	
	public String getMotive() {
		return motive;
	}

	public void setMotive(String motive) {
		this.motive = motive;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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
	
	public Timestamp getMaxClosureDate() {
		return maxClosureDate;
	}

	public void setMaxClosureDate(Timestamp maxClosureDate) {
		this.maxClosureDate = maxClosureDate;
	}

	public Timestamp getMinClosureDate() {
		return minClosureDate;
	}

	public void setMinClosureDate(Timestamp minClosureDate) {
		this.minClosureDate = minClosureDate;
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
