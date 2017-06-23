package org.collectiveone.modules.users;

public class AppUserDto {
	private String c1Id;
	private String auth0Id;
	private String email;
	private String nickname;
	private String pictureUrl;
	
	public String getC1Id() {
		return c1Id;
	}
	public void setC1Id(String c1Id) {
		this.c1Id = c1Id;
	}
	public String getAuth0Id() {
		return auth0Id;
	}
	public void setAuth0Id(String auth0Id) {
		this.auth0Id = auth0Id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	
}
