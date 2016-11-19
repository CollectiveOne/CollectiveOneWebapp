package org.collectiveone.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.collectiveone.web.dto.UserDto;

@Entity
@Table( name = "APP_USERS" )
@SequenceGenerator(name="users_seq", initialValue=1, allocationSize=100)
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="users_seq")
	private Long id;
	private String email;
	private String username;
	@Column(length = 60)
	private String password;
	private Boolean enabled;
	private Timestamp joindate;
	
	public User() {
        super();
        this.enabled=false;
    }
	
	public UserDto toDto() {
		UserDto dto = new UserDto();
		
		dto.setId(id);
		dto.setUsername(username);
		
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
	
	
	
}
