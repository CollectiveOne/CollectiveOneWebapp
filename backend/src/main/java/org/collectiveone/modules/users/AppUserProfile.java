package org.collectiveone.modules.users;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.collectiveone.modules.files.FileStored;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table( name = "app_users_profiles" )
public class AppUserProfile {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@OneToOne
	private AppUser user;
	
	@Column(name = "nickname", length = 32)
	private String nickname;
	
	@Column(name = "pitcure_url")
	private String pictureUrl;
	
	@Column(name = "use_uploaded_picture")
	private Boolean useUploadedPicture;
	
	@ManyToOne
	private FileStored uploadedPicture;
	
	@Column(name = "username", unique = true, length = 16)
	private String username;
	
	@Column(name = "short_bio", length = 250)
	private String shortBio;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column(name = "long_bio")
	private String longBio;
	
	@Column(name = "twitter_handle", length = 140)
	private String twitterHandle;
	
	@Column(name = "facebook_handle", length = 140)
	private String facebookHandle;
	
	@Column(name = "linkedin_handle", length = 140)
	private String linkedinHandle;
	
	

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
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

	public Boolean getUseUploadedPicture() {
		return useUploadedPicture;
	}

	public void setUseUploadedPicture(Boolean useUploadedPicture) {
		this.useUploadedPicture = useUploadedPicture;
	}

	public FileStored getUploadedPicture() {
		return uploadedPicture;
	}

	public void setUploadedPicture(FileStored uploadedPicture) {
		this.uploadedPicture = uploadedPicture;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
	
	

}
