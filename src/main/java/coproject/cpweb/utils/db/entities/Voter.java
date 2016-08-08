package coproject.cpweb.utils.db.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "VOTERS")
public class Voter {
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@ManyToOne
	private User voterUser;
	private double weight;
	
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

