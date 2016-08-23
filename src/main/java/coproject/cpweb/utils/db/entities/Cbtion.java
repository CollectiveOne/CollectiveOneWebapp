package coproject.cpweb.utils.db.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import coproject.cpweb.utils.db.entities.Project;
import coproject.cpweb.utils.db.entities.User;
import coproject.cpweb.utils.db.entities.dtos.CbtionDto;

@Entity
@Table( name = "CBTIONS" )
public class Cbtion {
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int id;
	@ManyToOne
	private Project project;
	@ManyToOne
	private User creator;
	private Timestamp creationDate;
	private String title;
	private String description;
	private String product;
	private Integer relevance;
	private CbtionState state;	
	@OneToMany(cascade=CascadeType.ALL)
	private List<Bid> bids = new ArrayList<Bid>();
	@ManyToOne
	private User contributor;
	private double assignedPpoints;
	@OneToOne
	private Decision create;
	@OneToOne
	private Decision close;
	
	@ManyToMany
	private List<Goal> goals = new ArrayList<Goal>();
	
	public CbtionDto toDto() {
		CbtionDto dto = new CbtionDto();
		
		dto.setTitle(title);
		dto.setProjectName(project.getName());
		dto.setId(id);
		dto.setDescription(description);
		dto.setProduct(product);
		dto.setCreatorUsername(creator.getUsername());
		dto.setCreationDate(creationDate);
		dto.setRelevance(relevance);
		dto.setState(state.toString());
		if(bids != null) dto.setnBids(bids.size());
		if(contributor != null) dto.setContributorUsername(contributor.getUsername());
		dto.setAssignedPpoints(assignedPpoints);
		
		return dto;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public Integer getRelevance() {
		return relevance;
	}
	public void setRelevance(Integer relevance) {
		this.relevance = relevance;
	}
	public CbtionState getState() {
		return state;
	}
	public void setState(CbtionState state) {
		this.state = state;
	}
	public User getContributor() {
		return contributor;
	}
	public void setContributor(User contributor) {
		this.contributor = contributor;
	}
	public List<Bid> getBids() {
		return bids;
	}
	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}
	public double getAssignedPpoints() {
		return assignedPpoints;
	}
	public void setAssignedPpoints(double assignedPpoints) {
		this.assignedPpoints = assignedPpoints;
	}
	public Decision getCreate() {
		return create;
	}
	public void setCreate(Decision create) {
		this.create = create;
	}
	public Decision getClose() {
		return close;
	}
	public void setClose(Decision close) {
		this.close = close;
	}
	public List<Goal> getGoals() {
		return goals;
	}
	public void setGoals(List<Goal> goals) {
		this.goals = goals;
	}
	
}
