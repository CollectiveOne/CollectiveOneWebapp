package org.collectiveone.modules.users;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table( name = "app_users" )
public class AppUser {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	
	@Column(name = "c1Id", updatable = false, nullable = false)
	private UUID c1Id;
	
	@Column(name = "auth0_id")
	private String auth0Id;
	
	@Column(name = "nickname")
	private String nickname;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "pitcure_url")
	private String pictureUrl;
	
	@Column(name = "email_notifications_enabled")
	private Boolean emailNotificationsEnabled;
	
	
	public AppUserDto toDto() {
		AppUserDto dto = new AppUserDto();
		
		dto.setC1Id(c1Id.toString());
		dto.setAuth0Id(auth0Id);
		dto.setNickname(nickname);
		dto.setEmail(email);
		dto.setPictureUrl(pictureUrl);
		
		return dto;
	}
	
	public UUID getC1Id() {
		return c1Id;
	}
	public void setC1Id(UUID c1Id) {
		this.c1Id = c1Id;
	}
	public String getAuth0Id() {
		return auth0Id;
	}
	public void setAuth0Id(String auth0Id) {
		this.auth0Id = auth0Id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public Boolean getEmailNotificationsEnabled() {
		return emailNotificationsEnabled;
	}
	public void setEmailNotificationsEnabled(Boolean emailNotificationsEnabled) {
		this.emailNotificationsEnabled = emailNotificationsEnabled;
	}
	
		
}
