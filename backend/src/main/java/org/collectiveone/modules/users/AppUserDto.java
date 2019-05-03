package org.collectiveone.modules.users;

import org.collectiveone.modules.uprcl.dtos.PerspectiveDto;
import org.collectiveone.modules.users.nannies.DIDNannyDto;

public class AppUserDto {
	private String did;
	private PerspectiveDto rootPerspective;
	private DIDNannyDto nanny;
	
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	public PerspectiveDto getRootPerspective() {
		return rootPerspective;
	}
	public void setRootPerspective(PerspectiveDto rootPerspective) {
		this.rootPerspective = rootPerspective;
	}
	public DIDNannyDto getNanny() {
		return nanny;
	}
	public void setNanny(DIDNannyDto nanny) {
		this.nanny = nanny;
	}
	
}
