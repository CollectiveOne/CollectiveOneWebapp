package org.collectiveone.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.collectiveone.web.dto.ArgumentDto;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "ARGUMENTS")
@SequenceGenerator(name="arguments_seq", initialValue=1, allocationSize=100)
public class Argument {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="arguments_seq")
	private Long id;
	@ManyToOne
	private User creator;
	private Timestamp creationDate;
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String description;
	@ManyToOne
	private Decision decision;
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "ARGUMENT_BACKERS")
	private List<User> backers = new ArrayList<User>();
	private ArgumentTendency tendency;
	
	public ArgumentDto toDto() {
		ArgumentDto dto = new ArgumentDto();
		
		dto.setId(id);
		if(creator != null) dto.setCreatorUsername(creator.getUsername());
		if(creationDate != null) dto.setCreationDate(creationDate.getTime());
		if(description != null) dto.setDescription(description);
		if(decision != null) {
			dto.setDecisionId(decision.getId());
			dto.setDecisionDescription(decision.getDescription());
		}
		if(backers != null) dto.setNbackers(backers.size());
		else dto.setNbackers(0);
		
		return dto;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	public Decision getDecision() {
		return decision;
	}
	public void setDecision(Decision decision) {
		this.decision = decision;
	}
	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}
	public List<User> getBackers() {
		return backers;
	}
	public void setBackers(List<User> backers) {
		this.backers = backers;
	}
	public ArgumentTendency getTendency() {
		return tendency;
	}
	public void setTendency(ArgumentTendency tendency) {
		this.tendency = tendency;
	}
	
}
