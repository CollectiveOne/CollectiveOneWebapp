package org.collectiveone.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table( name = "authorized_emails" )
@SequenceGenerator(name="authorized_emails_seq", initialValue=1, allocationSize=100)
public class AuthorizedEmail {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="authorized_emails_seq")
	private Long id;
	private String email;
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
	public Boolean getAuthorized() {
		return authorized;
	}
	public void setAuthorized(Boolean authorized) {
		this.authorized = authorized;
	}
	

}
