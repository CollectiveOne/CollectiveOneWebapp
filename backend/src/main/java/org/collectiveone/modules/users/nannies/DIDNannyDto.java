package org.collectiveone.modules.users.nannies;

public class DIDNannyDto {
	private String did;
	private DIDNannyType type;
	private String extId;


	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public DIDNannyType getType() {
		return type;
	}

	public void setType(DIDNannyType type) {
		this.type = type;
	}

	public String getExtId() {
		return extId;
	}

	public void setExtId(String extId) {
		this.extId = extId;
	}

}
