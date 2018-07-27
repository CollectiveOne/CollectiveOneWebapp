package org.collectiveone.modules.assignations;

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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.collectiveone.modules.assignations.dto.AssignationDto;
import org.collectiveone.modules.assignations.dto.AssignationDtoLight;
import org.collectiveone.modules.assignations.enums.AssignationState;
import org.collectiveone.modules.assignations.enums.AssignationType;
import org.collectiveone.modules.model.ModelSection;
import org.collectiveone.modules.users.AppUser;
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
	private ModelSection modelSection;
	
	@ManyToMany(cascade = CascadeType.ALL)

	private List<ModelSection> alsoInModelSection = new ArrayList<ModelSection>();

	@Column(name = "motive", length = 55)
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
	
	@Column(name = "creation_date")
	private Timestamp creationDate;
	
	@ManyToOne
	private AppUser creator;
	
	@OneToMany(mappedBy="assignation", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Bill> bills = new ArrayList<Bill>();
	
	@OneToMany(mappedBy="assignation", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Receiver> receivers = new ArrayList<Receiver>();
	
	@OneToMany(mappedBy="assignation", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Evaluator> evaluators = new ArrayList<Evaluator>();
	
	@OneToOne(cascade = CascadeType.ALL)
	private AssignationConfig config;

	
	public AssignationDtoLight toDtoLight() {
		AssignationDtoLight dto = new AssignationDtoLight();
		
		dto.setId(id.toString());
		dto.setType(type.toString());
		dto.setMotive(motive);
		dto.setNotes(notes);
		dto.setModelsectionid(modelSection.getId().toString());
		dto.setModelsectionName(modelSection.getTitle());
		dto.setCreationDate(creationDate.getTime());
		
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

		dto.setModelsectionid(modelSection.getId().toString());
		dto.setModelsectionName(modelSection.getTitle());
		
		dto.setCreationDate(creationDate.getTime());
		dto.setConfig(config.toDto());
		
		for(Bill bill : bills) {
			dto.getAssets().add(bill.toDto());
		}
		
		for(Receiver receiver : receivers) {
			dto.getReceivers().add(receiver.toDto());
		}
		
		return dto;
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public ModelSection getModelSection() {
		return modelSection;
	}

	public void setModelSection(ModelSection modelSection) {
		this.modelSection = modelSection;
	}
	
	public List<ModelSection> getAlsoInModelSections() {

		return alsoInModelSection;
	}

	public void setAlsoInInitiatives(List<ModelSection> alsoInModelSections) {
		this.alsoInModelSection = alsoInModelSection;

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

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public void setState(AssignationState state) {
		this.state = state;
	}
	
	public AppUser getCreator() {
		return creator;
	}

	public void setCreator(AppUser creator) {
		this.creator = creator;
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

	public AssignationConfig getConfig() {
		return config;
	}

	public void setConfig(AssignationConfig config) {
		this.config = config;
	}
	
}
