package coproject.cpweb.utils.db.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import coproject.cpweb.utils.db.entities.User;
import coproject.cpweb.utils.db.entities.dtos.BidDto;

@Entity
@Table( name = "BIDS" )
public class Bid {
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int id;
	@ManyToOne
	private Cbtion cbtion = new Cbtion();
	@ManyToOne
	private User creator = new User();
	private double ppoints;
	private Timestamp creationDate;
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String description;
	private Timestamp deliveryDate;
	private BidState state;
	@OneToOne
	private Decision assign;
	@OneToOne
	private Decision accept;
	
	private BidDoneState doneState;
	private Timestamp doneDate;
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String doneDescription;
	
	@OneToMany
	private List<Review> reviews = new ArrayList<Review>();
	
	public BidDto toDto() {
		BidDto dto = new BidDto();
		
		dto.setId(id);
		if(cbtion != null) { 
			dto.setCbtionId(cbtion.getId());
			dto.setCbtionTitle(cbtion.getTitle());
		}
		
		if(creator != null) dto.setCreatorDto(creator.toDto());
		dto.setPpoints(ppoints);
		if(creationDate != null) dto.setCreationDate(creationDate.getTime());
		if(description != null) dto.setDescription(description);
		if(deliveryDate != null) dto.setDeliveryDate(deliveryDate.getTime());
		if(state != null) dto.setState(state.toString());
		if(assign != null) dto.setAssignDec(assign.toDto());
		if(accept != null) dto.setAcceptDec(accept.toDto());
		if(doneState != null) dto.setDoneState(doneState.toString());
		if(doneDate != null) dto.setDoneDate(doneDate.getTime());
		if(doneDescription != null) dto.setDoneDescription(doneDescription);		
		
		return dto;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Cbtion getCbtion() {
		return cbtion;
	}
	public void setCbtion(Cbtion cbtion) {
		this.cbtion = cbtion;
	}
	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}
	public double getPpoints() {
		return ppoints;
	}
	public void setPpoints(double ppoints) {
		this.ppoints = ppoints;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Timestamp deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public BidState getState() {
		return state;
	}
	public void setState(BidState state) {
		this.state = state;
	}
	public Decision getAssign() {
		return assign;
	}
	public void setAssign(Decision assign) {
		this.assign = assign;
	}
	public Decision getAccept() {
		return accept;
	}
	public void setAccept(Decision accept) {
		this.accept = accept;
	}
	public List<Review> getReviews() {
		return reviews;
	}
	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
	public BidDoneState getDoneState() {
		return doneState;
	}
	public void setDoneState(BidDoneState doneState) {
		this.doneState = doneState;
	}
	public Timestamp getDoneDate() {
		return doneDate;
	}
	public void setDoneDate(Timestamp doneDate) {
		this.doneDate = doneDate;
	}
	public String getDoneDescription() {
		return doneDescription;
	}
	public void setDoneDescription(String doneDescription) {
		this.doneDescription = doneDescription;
	}
	
}

