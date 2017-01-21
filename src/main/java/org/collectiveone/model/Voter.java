package org.collectiveone.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.web.dto.VoterDto;

@Entity
@Table(name = "VOTERS")
public class Voter {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private DecisionRealm realm;
	@ManyToOne
	private User voterUser;
	private double maxWeight;
	private double scale;
	
	public VoterDto toDto() {
		VoterDto dto = new VoterDto();
		dto.setUsername(voterUser.getUsername());
		dto.setMaxWeight(maxWeight);
		dto.setActualWeight(getActualWeight());
		dto.setScale(scale);
		
		return dto;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public DecisionRealm getRealm() {
		return realm;
	}
	public void setRealm(DecisionRealm realm) {
		this.realm = realm;
	}
	public User getVoterUser() {
		return voterUser;
	}
	public void setVoterUser(User voterUser) {
		this.voterUser = voterUser;
	}
	public double getMaxWeight() {
		return maxWeight;
	}
	public void setMaxWeight(double maxWeight) {
		this.maxWeight = maxWeight;
	}
	public double getScale() {
		return scale;
	}
	public void setScale(double scale) {
		this.scale = scale;
	}
	public double getActualWeight() {
		return maxWeight*scale;
	}
	
}

