package coproject.cpweb.utils.db.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import coproject.cpweb.utils.db.entities.dtos.GoalDto;

@Entity
@Table( name = "GOAL" )
public class Goal {
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int id;
	@ManyToOne
	private Project project;
	@ManyToOne
	private User creator;
	private Timestamp creationDate;
	private String goalTag;
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String description;
	private GoalState state;	
	@OneToOne
	private Decision createDec;
	@OneToOne
	private Decision deleteDec;
	
	public GoalDto toDto() {
		GoalDto dto = new GoalDto();
		
		dto.setId(id);
		dto.setProjectName(project.getName());
		dto.setCreatorUsername(creator.getUsername());
		dto.setCreationDate(creationDate);
		dto.setGoalTag(goalTag);
		dto.setDescription(description);
		dto.setState(state.toString());
		if(createDec != null) dto.setCreateDec(createDec.ToDto());
		if(deleteDec != null) dto.setDeleteDec(deleteDec.ToDto());
		
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

	public String getGoalTag() {
		return goalTag;
	}

	public void setGoalTag(String goalTag) {
		this.goalTag = goalTag;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public GoalState getState() {
		return state;
	}

	public void setState(GoalState state) {
		this.state = state;
	}

	public Decision getCreateDec() {
		return createDec;
	}

	public void setCreateDec(Decision createDec) {
		this.createDec = createDec;
	}

	public Decision getDeleteDec() {
		return deleteDec;
	}

	public void setDeleteDec(Decision deleteDec) {
		this.deleteDec = deleteDec;
	}
}
