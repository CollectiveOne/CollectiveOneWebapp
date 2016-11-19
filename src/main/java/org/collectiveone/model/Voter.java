package org.collectiveone.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "VOTERS")
@SequenceGenerator(name="voters_seq", initialValue=1, allocationSize=100)
public class Voter {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="voters_seq")
	private Long id;
	@ManyToOne
	private User voterUser;
	private double weight;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getVoterUser() {
		return voterUser;
	}
	public void setVoterUser(User voterUser) {
		this.voterUser = voterUser;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
}

