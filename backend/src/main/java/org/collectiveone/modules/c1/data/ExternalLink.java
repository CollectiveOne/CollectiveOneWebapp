package org.collectiveone.modules.c1.data;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Value;

@Embeddable
public class ExternalLink {
	
	@Transient
	@Value("${UPRTLC_ENDPOINT}")
	private String UPRTCL_ENPOINT;
	
	private NetworkId network;
	private String networkAddress;
	private String element;
	
	public ExternalLink() {
		super();
	}
	
	public ExternalLink(NetworkId network, String networkAddress, String element) {
		this.network = network;
		this.networkAddress = networkAddress;
		this.element = element;
	}
	
	public ExternalLink(String linkString) {
		String[] parts = linkString.split("://");
		this.network = NetworkId.valueOf(parts[0]);
		int lastBar = parts[1].lastIndexOf("/");
		this.networkAddress = parts[1].substring(0, lastBar);
		this.element = parts[1].substring(lastBar + 1);
	}
	
	public void setLocalElement(String element) {
		this.network = NetworkId.HTTP;
		this.networkAddress = UPRTCL_ENPOINT;
		this.element = element;
	}
	
	public Boolean isLocal() {
		return (network == NetworkId.HTTP) && (networkAddress == UPRTCL_ENPOINT);
	}
	
	public NetworkId getNetwork() {
		return network;
	}
	public void setNetwork(NetworkId network) {
		this.network = network;
	}
	public String getNetworkAddress() {
		return networkAddress;
	}
	public void setNetworkAddress(String networkAddress) {
		this.networkAddress = networkAddress;
	}
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	
}
