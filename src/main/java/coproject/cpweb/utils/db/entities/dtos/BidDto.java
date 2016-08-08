package coproject.cpweb.utils.db.entities.dtos;

import java.sql.Timestamp;

import coproject.cpweb.utils.db.entities.Bid;
import coproject.cpweb.utils.db.entities.Cbtion;
import coproject.cpweb.utils.db.entities.User;

public class BidDto {
	private int id;
	private int cbtionId;
	private UserDto creatorDto;
	private double ppoints;
	private Timestamp creationDate;
	private String description;
	private String state;
	private DecisionDto assignDec;
	private DecisionDto acceptDec;
	
	public Bid toBid(User creator, Cbtion cbtion) {
		Bid bid = new Bid();
		
		bid.setId(id);
		bid.setCbtion(cbtion);
		bid.setCreator(creator);
		bid.setPpoints(ppoints);
		bid.setCreationDate(creationDate);
		bid.setDescription(description);
		
		return bid;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCbtionId() {
		return cbtionId;
	}
	public void setCbtionId(int cbtionId) {
		this.cbtionId = cbtionId;
	}
	public UserDto getCreatorDto() {
		return creatorDto;
	}

	public void setCreatorDto(UserDto creatorDto) {
		this.creatorDto = creatorDto;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public DecisionDto getAssignDec() {
		return assignDec;
	}
	public void setAssignDec(DecisionDto assignDec) {
		this.assignDec = assignDec;
	}
	public DecisionDto getAcceptDec() {
		return acceptDec;
	}
	public void setAcceptDec(DecisionDto acceptDec) {
		this.acceptDec = acceptDec;
	}

}
