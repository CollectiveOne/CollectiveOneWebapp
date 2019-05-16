package org.collectiveone.modules.c1.data.entities;

import javax.persistence.Embeddable;

import org.collectiveone.modules.c1.data.enums.NetworkId;

@Embeddable
public class ExternalLink {
	
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
	
	@Override
	public String toString() {
		return this.network + "://" + this.networkAddress + "/" + this.element;
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
