package org.collectiveone.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.collectiveone.web.dto.UserDto;
import org.hibernate.annotations.Type;

@Entity
@Table( name = "APP_USERS" )
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String email;
	private String username;
	@Column(length = 60)
	private String password;
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String profile;
	private Boolean enabled;
	private Timestamp joindate;
	private Boolean isReferrer;
	
	public User() {
        super();
        this.enabled=false;
    }
	
	public UserDto toDto() {
		UserDto dto = new UserDto();
		
		dto.setId(id);
		dto.setUsername(username);
		dto.setProfile(profile);
		
		return dto;
	}
	
	// Getter-Setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public Timestamp getJoindate() {
		return joindate;
	}
	public void setJoindate(Timestamp joindate) {
		this.joindate = joindate;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public Boolean getIsReferrer() {
		return isReferrer;
	}
	public void setIsReferrer(Boolean isReferrer) {
		this.isReferrer = isReferrer;
	}
	
}
