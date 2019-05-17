package org.collectiveone.modules.users;

import org.collectiveone.modules.users.nannies.DIDNannyDto;

public class AppUserDto {
	private String did;
	private String rootPerspectiveLink;
	private DIDNannyDto nanny;
	
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	public String getRootPerspectiveLink() {
		return rootPerspectiveLink;
	}
	public void setRootPerspectiveLink(String rootPerspectiveLink) {
		this.rootPerspectiveLink = rootPerspectiveLink;
	}
	public DIDNannyDto getNanny() {
		return nanny;
	}
	public void setNanny(DIDNannyDto nanny) {
		this.nanny = nanny;
	}
	
}
