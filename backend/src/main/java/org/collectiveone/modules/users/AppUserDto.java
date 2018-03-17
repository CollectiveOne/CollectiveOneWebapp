package org.collectiveone.modules.users;

import java.util.ArrayList;
import java.util.List;

public class AppUserDto {
	private String c1Id;
	private List<String> auth0Ids = new ArrayList<String>();
	private String email;
	private String username;
	private String twitterHandle;
	private String facebookHandle;
	private String linkedinHandle;
	
	private String nickname;
	private String pictureUrl;
	private String shortBio;
	private String longBio;
	
	private Boolean useUploadedPicture;
	
	
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public String getTwitterHandle() {
		return twitterHandle;
	}
	public void setTwitterHandle(String twitterHandle) {
		this.twitterHandle = twitterHandle;
	}
	public String getFacebookHandle() {
		return facebookHandle;
	}
	public void setFacebookHandle(String facebookHandle) {
		this.facebookHandle = facebookHandle;
	}
	public String getLinkedinHandle() {
		return linkedinHandle;
	}
	public void setLinkedinHandle(String linkedinHandle) {
		this.linkedinHandle = linkedinHandle;
	}
	public String getShortBio() {
		return shortBio;
	}
	public void setShortBio(String shortBio) {
		this.shortBio = shortBio;
	}
	public String getLongBio() {
		return longBio;
	}
	public void setLongBio(String longBio) {
		this.longBio = longBio;
	}
	public Boolean getUseUploadedPicture() {
		return useUploadedPicture;
	}
	public void setUseUploadedPicture(Boolean useUploadedPicture) {
		this.useUploadedPicture = useUploadedPicture;
	}
	
}
