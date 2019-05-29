package org.collectiveone.modules.users;

import org.collectiveone.modules.users.nannies.DIDNannyDto;

public class AppUserDto {
	private String did;
	private String rootPerspectiveId;
	private DIDNannyDto nanny;
	
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	public String getRootPerspectiveId() {
		return rootPerspectiveId;
	}
	public void setRootPerspectiveId(String rootPerspectiveId) {
		this.rootPerspectiveId = rootPerspectiveId;
	}
	public DIDNannyDto getNanny() {
		return nanny;
	}
	public void setNanny(DIDNannyDto nanny) {
		this.nanny = nanny;
	}
	
}
