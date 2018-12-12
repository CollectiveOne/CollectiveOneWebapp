package org.collectiveone.modules.users;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.collectiveone.modules.contexts.entities.Context;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table( name = "app_users" )
public class AppUser {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@Column(name = "email", unique = true)
	private String email;
	
	@ElementCollection
	@CollectionTable(name="auth_ids", joinColumns=@JoinColumn(name="user_id"))
	@Column(name="auth0_id")
	private List<String> auth0Ids = new ArrayList<String>();
	
	@OneToOne(mappedBy="user")
	private AppUserProfile profile;
	
	@OneToOne
	private Context rootContext;
	
	
	public AppUserDto toDtoLight() {
		AppUserDto dto = new AppUserDto();
		
		dto.setC1Id(id.toString());
		dto.setUsername(getProfile().getUsername());
		dto.setNickname(getProfile().getNickname());
		dto.setShortBio(getProfile().getShortBio());
		dto.setFacebookHandle(getProfile().getFacebookHandle());
		dto.setTwitterHandle(getProfile().getTwitterHandle());
		dto.setLinkedinHandle(getProfile().getLinkedinHandle());
		
		if (getProfile().getUseUploadedPicture() != null) {
			if (!getProfile().getUseUploadedPicture()) {
				dto.setPictureUrl(getProfile().getPictureUrl());
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
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<String> getAuth0Ids() {
		return auth0Ids;
	}
	public void setAuth0Ids(List<String> auth0Ids) {
		this.auth0Ids = auth0Ids;
	}
	public AppUserProfile getProfile() {
		return profile;
	}
	public void setProfile(AppUserProfile profile) {
		this.profile = profile;
	}
	public Context getRootContext() {
		return rootContext;
	}
	public void setRootContext(Context rootContext) {
		this.rootContext = rootContext;
	}

	
}
