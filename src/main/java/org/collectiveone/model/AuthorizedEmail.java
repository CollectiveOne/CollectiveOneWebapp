package org.collectiveone.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "authorized_emails" )
public class AuthorizedEmail {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String email;
	private Long referralId;
	private String token;
	private Timestamp dateRequested;
	private Boolean authorized;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getReferral() {
		return referralId;
	}
	public void setReferral(Long referralId) {
		this.referralId = referralId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Timestamp getDateRequested() {
		return dateRequested;
	}
	public void setDateRequested(Timestamp dateRequested) {
		this.dateRequested = dateRequested;
	}
	public Boolean getAuthorized() {
		return authorized;
	}
	public void setAuthorized(Boolean authorized) {
		this.authorized = authorized;
	}
}
