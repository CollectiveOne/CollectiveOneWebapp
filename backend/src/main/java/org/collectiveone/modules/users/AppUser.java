package org.collectiveone.modules.users;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.collectiveone.modules.c1.data.entities.DefaultPerspective;
import org.collectiveone.modules.ipld.IpldService;
import org.collectiveone.modules.uprtcl.entities.Context;
import org.collectiveone.modules.users.nannies.DIDNanny;

@Entity
@Table( name = "app_users" )
public class AppUser {

	@Id
	@Column(name = "id", updatable = false, nullable = false, unique = true)
	private String did;
	
	@OneToOne
	private DIDNanny nanny;
	
	@OneToOne
	private Context context;
	
	@OneToMany(mappedBy = "user")
	private List<DefaultPerspective> defaultPerspectives = new ArrayList<DefaultPerspective>();
	
	public AppUserDto toDto() {
		AppUserDto dto = new AppUserDto();
		
		dto.setDid(did);
		dto.setRootContextId(IpldService.decode(context.getId()));
		dto.setNanny(nanny.toDto());
		
		return dto;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((did == null) ? 0 : did.hashCode());
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
		if (did == null) {
			if (other.did != null)
				return false;
		} else if (!did.equals(other.did))
			return false;
		return true;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}
	
	public DIDNanny getNanny() {
		return nanny;
	}

	public void setNanny(DIDNanny nanny) {
		this.nanny = nanny;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
}
