package org.collectiveone.modules.users;

import org.collectiveone.modules.users.nannies.DIDNannyDto;

public class AppUserDto {
	private String did;
	private String rootContextId;
	private DIDNannyDto nanny;
	
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	public String getRootContextId() {
		return rootContextId;
	}
	public void setRootContextId(String rootContextId) {
		this.rootContextId = rootContextId;
	}
	public DIDNannyDto getNanny() {
		return nanny;
	}
	public void setNanny(DIDNannyDto nanny) {
		this.nanny = nanny;
	}
	
}
