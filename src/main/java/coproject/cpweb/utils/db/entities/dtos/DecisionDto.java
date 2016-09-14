package coproject.cpweb.utils.db.entities.dtos;

public class DecisionDto {
	
	protected int id;
	protected String description;
	protected long creationDate;
	protected long openDate;
	protected long actualVerdictDate;
	protected String fromState;
	protected String toState;
	protected String projectName;
	protected String creatorUsername;
	
	protected String type;
	protected int cbtionId;
	protected String cbtionTitle;
	protected int goalId;
	protected String goalTag;
	protected int bidId;
	protected String bidCreatorUsername;
	
	protected int nVoters;
	protected double ppsTot;
	protected double verdictHours;
	protected int verdict;
	protected String state;
	protected int nVotesCasted;
	protected double ppsCum;
	protected double pest;
	protected double stability;
	protected double clarity;
	protected double log_l1l0;
	protected double elapsedFactor;
		
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}
	public long getOpenDate() {
		return openDate;
	}
	public void setOpenDate(long openDate) {
		this.openDate = openDate;
	}
	public long getActualVerdictDate() {
		return actualVerdictDate;
	}
	public void setActualVerdictDate(long actualVerdictDate) {
		this.actualVerdictDate = actualVerdictDate;
	}
	public String getFromState() {
		return fromState;
	}
	public void setFromState(String fromState) {
		this.fromState = fromState;
	}
	public String getToState() {
		return toState;
	}
	public void setToState(String toState) {
		this.toState = toState;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getCreatorUsername() {
		return creatorUsername;
	}
	public void setCreatorUsername(String creatorUsername) {
		this.creatorUsername = creatorUsername;
	}
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getCbtionId() {
		return cbtionId;
	}
	public void setCbtionId(int cbtionId) {
		this.cbtionId = cbtionId;
	}
	public String getCbtionTitle() {
		return cbtionTitle;
	}
	public void setCbtionTitle(String cbtionTitle) {
		this.cbtionTitle = cbtionTitle;
	}
	public int getGoalId() {
		return goalId;
	}
	public void setGoalId(int goalId) {
		this.goalId = goalId;
	}
	public String getGoalTag() {
		return goalTag;
	}
	public void setGoalTag(String goalTag) {
		this.goalTag = goalTag;
	}
	public int getBidId() {
		return bidId;
	}
	public void setBidId(int bidId) {
		this.bidId = bidId;
	}
	public String getBidCreatorUsername() {
		return bidCreatorUsername;
	}
	public void setBidCreatorUsername(String bidCreatorUsername) {
		this.bidCreatorUsername = bidCreatorUsername;
	}
	
	
	public int getnVoters() {
		return nVoters;
	}
	public void setnVoters(int nVoters) {
		this.nVoters = nVoters;
	}
	public double getPpsTot() {
		return ppsTot;
	}
	public void setPpsTot(double ppsTot) {
		this.ppsTot = ppsTot;
	}
	public double getVerdictHours() {
		return verdictHours;
	}
	public void setVerdictHours(double verdictHours) {
		this.verdictHours = verdictHours;
	}
	public int getVerdict() {
		return verdict;
	}
	public void setVerdict(int verdict) {
		this.verdict = verdict;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getnVotesCasted() {
		return nVotesCasted;
	}
	public void setnVotesCasted(int nVotesCasted) {
		this.nVotesCasted = nVotesCasted;
	}
	public double getPpsCum() {
		return ppsCum;
	}
	public void setPpsCum(double ppsCum) {
		this.ppsCum = ppsCum;
	}
	public double getPest() {
		return pest;
	}
	public void setPest(double pest) {
		this.pest = pest;
	}
	public double getStability() {
		return stability;
	}
	public void setStability(double stability) {
		this.stability = stability;
	}
	public double getClarity() {
		return clarity;
	}
	public void setClarity(double clarity) {
		this.clarity = clarity;
	}
	public double getLog_l1l0() {
		return log_l1l0;
	}
	public void setLog_l1l0(double log_l1l0) {
		this.log_l1l0 = log_l1l0;
	}
	public double getElapsedFactor() {
		return elapsedFactor;
	}
	public void setElapsedFactor(double elapsedFactor) {
		this.elapsedFactor = elapsedFactor;
	}
	
	
}

