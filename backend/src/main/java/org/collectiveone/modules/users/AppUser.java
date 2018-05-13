package org.collectiveone.modules.users;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
	
	@ElementCollection
	@CollectionTable(name="auth_ids", joinColumns=@JoinColumn(name="user_id"))
	@Column(name="auth0_id")
	private List<String> auth0Ids = new ArrayList<String>();
	
	@Column(name = "email", unique = true)
	private String email;
	
	@Column(name = "email_notifications_enabled")
	private Boolean emailNotificationsEnabled;
	
	@OneToOne(mappedBy="user")
	private AppUserProfile profile;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "online_status")
	private UserOnlineStatus onlineStatus;
	
	private Timestamp lastSeen;
	
	public AppUserDto toDtoLight() {
		AppUserDto dto = new AppUserDto();
		
		dto.setC1Id(c1Id.toString());
		dto.setUsername(getProfile().getUsername());
		dto.setNickname(getProfile().getNickname());
		dto.setShortBio(getProfile().getShortBio());
		dto.setFacebookHandle(getProfile().getFacebookHandle());
		dto.setTwitterHandle(getProfile().getTwitterHandle());
		dto.setLinkedinHandle(getProfile().getLinkedinHandle());
		
		if (getProfile().getUseUploadedPicture() != null) {
			if (!getProfile().getUseUploadedPicture()) {
				dto.setPictureUrl(getProfile().getPictureUrl());
			} else {
				dto.setPictureUrl(getProfile().getUploadedPicture().getUrl()+"?updated="+getProfile().getUploadedPicture().getLastUpdated().toString());
			}
		} else {
			dto.setPictureUrl(getProfile().getPictureUrl());
		}
		
		
		return dto;
	}
	
	public AppUserDto toDtoFull() {
		AppUserDto dto = toDtoLight();
		dto.setLongBio(getProfile().getLongBio());
		
		return dto;
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((c1Id == null) ? 0 : c1Id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AppUser other = (AppUser) obj;
        if (c1Id == null) {
            if (other.c1Id != null)
                return false;
        } else if (!c1Id.equals(other.c1Id))
            return false;
        return true;
    }
	
	public UUID getC1Id() {
		return c1Id;
	}
	public void setC1Id(UUID c1Id) {
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
	public Boolean getEmailNotificationsEnabled() {
		return emailNotificationsEnabled;
	}
	public void setEmailNotificationsEnabled(Boolean emailNotificationsEnabled) {
		this.emailNotificationsEnabled = emailNotificationsEnabled;
	}
	public AppUserProfile getProfile() {
		return profile;
	}
	public void setProfile(AppUserProfile profile) {
		this.profile = profile;
	}
	public Timestamp getLastSeen() {
		return lastSeen;
	}
	public void setLastSeen(Timestamp lastSeen) {
		this.lastSeen = lastSeen;
	}
	public UserOnlineStatus getOnlineStatus() {
		return onlineStatus;
	}
	public void setOnlineStatus(UserOnlineStatus onlineStatus) {
		this.onlineStatus = onlineStatus;
	}
	
}
