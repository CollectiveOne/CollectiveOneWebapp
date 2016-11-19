package org.collectiveone.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "PROMOTERS")
@SequenceGenerator(name="promoters_seq", initialValue=1, allocationSize=100)
public class Promoter {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="promoters_seq")
	private Long id;
	@ManyToOne
	private User user;
	private Timestamp creationDate;
	private boolean promoteUp;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	public boolean getPromoteUp() {
		return promoteUp;
	}
	public void setPromoteUp(boolean promoteUp) {
		this.promoteUp = promoteUp;
	}
	
	
}
