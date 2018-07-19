package org.collectiveone.modules.tokens;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.model.ModelSection;
import org.collectiveone.modules.tokens.dto.TransferDto;
import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "model_section_transfers")
public class ModelSectionTransfer {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private ModelSection from;
	public ModelSection getFrom()
	{
		return this.from;
	}

	public void setFrom(ModelSection from)
	{
		this.from = from;
	}

	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<ModelSection> alsoInModelSection = new ArrayList<ModelSection>();

	
	
	@ManyToOne
	private ModelSection to;
	
	public ModelSection getTo()
	{
		return this.to;
	}

	public void setTo(ModelSection to)
	{
		this.to = to;
	}

	
	@ManyToOne
	private TokenType tokenType;
	
	@Column(name = "oder_date")
	private Timestamp orderDate;
	
	@ManyToOne
	private AppUser orderedBy;
	
	private double value;
	
	@Column ( name = "motive")
	private String motive;
	
	@Column ( name = "notes")
	private String notes;
	
	
	public TransferDto toDto() {
		TransferDto dto = new TransferDto();
		
		dto.setAssetId(tokenType.getId().toString());
		dto.setAssetName(tokenType.getName());
		dto.setSenderId(from.getId().toString());
		dto.setSenderName(from.getTitle());
		dto.setReceiverId(to.getId().toString());
		dto.setReceiverName(to.getTitle());
		if (orderDate != null) dto.setOrderDate(orderDate.getTime());
		if (orderedBy != null) dto.setOrderedBy(orderedBy.toDtoLight());
		dto.setMotive(motive);
		dto.setNotes(notes);
		dto.setValue(value);
		
		return dto;
	} 
	
		
	public UUID getId() {
		return id;
	}

	

	public TokenType getTokenType() {
		return tokenType;
	}

	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public Timestamp getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}
	
	public AppUser getOrderedBy() {
		return orderedBy;
	}

	public void setOrderedBy(AppUser orderedBy) {
		this.orderedBy = orderedBy;
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

    /**
     * @return List<ModelSection> return the alsoInModelSection
     */
    public List<ModelSection> getAlsoInModelSection() {
        return alsoInModelSection;
    }

    /**
     * @param alsoInModelSection the alsoInModelSection to set
     */
    public void setAlsoInModelSection(List<ModelSection> alsoInModelSection) {
        this.alsoInModelSection = alsoInModelSection;
    }

}
