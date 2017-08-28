package org.collectiveone.modules.users;

import java.util.ArrayList;
import java.util.List;

public class AppUserDto {
	private String c1Id;
	private List<String> auth0Ids = new ArrayList<String>();
	private String email;
	private String nickname;
	private String pictureUrl;
	
	public String getC1Id() {
		return c1Id;
	}
	public void setC1Id(String c1Id) {
		this.c1Id = c1Id;
	}
	public List<String> getAuth0Ids() {
		return auth0Ids;
	}
	public void setAuth0Ids(List<String> auth0Ids) {
		this.auth0Ids = auth0Ids;
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
