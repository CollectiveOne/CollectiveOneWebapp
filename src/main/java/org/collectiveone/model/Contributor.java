package org.collectiveone.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CONTRIBUTORS")
public class Contributor {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private Project project;
	@ManyToOne
	private User contributorUser;
	private double pps;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public User getContributorUser() {
		return contributorUser;
	}
	public void setContributorUser(User contributorUser) {
		this.contributorUser = contributorUser;
	}
	public double getPps() {
		return pps;
	}
	public void setPps(double pps) {
		this.pps = pps;
	}
	
}

