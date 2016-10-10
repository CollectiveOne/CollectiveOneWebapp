package coproject.cpweb.utils.db.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import coproject.cpweb.utils.db.entities.dtos.UserDto;

@Entity
@Table( name = "APP_USERS" )
public class User {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int id;
	private String username;
	private String password;
	private Timestamp joindate;
	
	// Dto
	public UserDto toDto() {
		UserDto dto = new UserDto();
		
		dto.setId(id);
		dto.setUsername(username);
		
		return dto;
	}
	
	
	// Getter-Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	
}
