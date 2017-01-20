package org.collectiveone.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.web.dto.ThesisDto;

@Entity
@Table( name = "THESES" )
public class Thesis {
	/* Theses are actually votes, but are called theses to 
	 * as in the future, perhaps, votes would be more complex
	 * and be more like theses*/
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private int value;
	private Timestamp castDate;
	@ManyToOne
	private User author;
	/* weight is stored with the thesis to avoid having to
	 * look for it every time as it is used in several places 
	 * in the decision algorithm.*/
	private double weight;
	
	@ManyToOne
	private Decision decision;
	
	
	public ThesisDto toDto() {
		ThesisDto dto = new ThesisDto();
		
		dto.setId(id);
		if(author != null) dto.setAuthorUsername(author.getUsername());
		dto.setCastDate(castDate);
		dto.setValue(value);
		dto.setWeight(weight);
		if(decision != null) dto.setDecisionId(decision.getId());
		
		return dto;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int thesis) {
		this.value = thesis;
	}
	public Timestamp getCastDate() {
		return castDate;
	}
	public void setCastDate(Timestamp castDate) {
		this.castDate = castDate;
	}
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public Decision getDecision() {
		return decision;
	}
	public void setDecision(Decision decision) {
		this.decision = decision;
	}
	
}

