package coproject.cpweb.utils.db.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import coproject.cpweb.utils.db.entities.User;
import coproject.cpweb.utils.db.entities.dtos.ThesisDto;

@Entity
@Table( name = "THESES" )
public class Thesis {
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int id;
	private int value;
	private Timestamp castDate;
	@ManyToOne
	private User author;
	private double weight;
	
	@ManyToOne
	private Decision decision;
	
	
	public ThesisDto toDto() {
		ThesisDto dto = new ThesisDto();
		
		dto.setId(id);
		dto.setAuthorDto(this.getAuthor().toDto());
		dto.setCastDate(castDate);
		dto.setValue(value);
		dto.setWeight(weight);
		
		return dto;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
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

