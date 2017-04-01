package org.collectiveone.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.collectiveone.web.dto.GoalEditionProposalDto;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "EDITION_PROPOSALS")
public class GoalEditionProposal {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private User proposer;
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String edition;
	private Timestamp creationDate;;
	@ManyToOne
	private Goal goal;
	@OneToOne
	private Decision acceptDec;
	private ProposalState state;

	public GoalEditionProposalDto toDto() {
		GoalEditionProposalDto dto = new GoalEditionProposalDto();
		
		dto.setId(id);
		dto.setCreationDate(creationDate.getTime());
		dto.setProposerUsername(proposer.getUsername());
		dto.setEdition(edition);
		dto.setGoalId(goal.getId());
		dto.setGoalTag(goal.getGoalTag());
		dto.setState(state.toString());
		dto.setAcceptDec(acceptDec.toDto());
		
		return dto;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getProposer() {
		return proposer;
	}
	public void setProposer(User proposer) {
		this.proposer = proposer;
	}
	public String getEdition() {
		return edition;
	}
	public void setEdition(String edition) {
		this.edition = edition;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	public Goal getGoal() {
		return goal;
	}
	public void setGoal(Goal goal) {
		this.goal = goal;
	}
	public Decision getAcceptDec() {
		return acceptDec;
	}
	public void setAcceptDec(Decision acceptDec) {
		this.acceptDec = acceptDec;
	}
	public ProposalState getState() {
		return state;
	}
	public void setState(ProposalState state) {
		this.state = state;
	}
	
}
