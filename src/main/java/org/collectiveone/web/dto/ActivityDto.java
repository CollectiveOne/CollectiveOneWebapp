package org.collectiveone.web.dto;

public class ActivityDto {
	private Long id;
	private long creationDate;
	private String event;
	private String eventPretty;
	private String projectName;
	private String type;
	private CbtionDto cbtionDto;
	private BidDto bidDto;
	private GoalDto goalDto;
	private DecisionDto decisionDto;
	private ArgumentDto argumentDto;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getEventPretty() {
		return eventPretty;
	}
	public void setEventPretty(String eventPretty) {
		this.eventPretty = eventPretty;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public CbtionDto getCbtionDto() {
		return cbtionDto;
	}
	public void setCbtionDto(CbtionDto cbtionDto) {
		this.cbtionDto = cbtionDto;
	}
	public BidDto getBidDto() {
		return bidDto;
	}
	public void setBidDto(BidDto bidDto) {
		this.bidDto = bidDto;
	}
	public GoalDto getGoalDto() {
		return goalDto;
	}
	public void setGoalDto(GoalDto goalDto) {
		this.goalDto = goalDto;
	}
	public DecisionDto getDecisionDto() {
		return decisionDto;
	}
	public void setDecisionDto(DecisionDto decisionDto) {
		this.decisionDto = decisionDto;
	}
	public ArgumentDto getArgumentDto() {
		return argumentDto;
	}
	public void setArgumentDto(ArgumentDto argumentDto) {
		this.argumentDto = argumentDto;
	}
	
}
