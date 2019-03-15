package org.collectiveone.modules.users;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table( name = "app_users_push_subscriptions" )
public class PushSubscription {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private AppUserProfile profile;
	
	@Column(length = 2084)
	private String endpoint;
	
	@Column(length = 256)
	private byte[] p256dh;
	
	@Column(length = 256)
	private byte[] auth;
	
	public PushSubscription() {
		super();
	}
	
	public PushSubscription(String endpoint, byte[] p256dh, byte[] auth) {
		super();
		this.endpoint = endpoint;
		this.p256dh = p256dh;
		this.auth = auth;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public AppUserProfile getProfile() {
		return profile;
	}

	public void setProfile(AppUserProfile profile) {
		this.profile = profile;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public byte[] getP256dh() {
		return p256dh;
	}

	public void setP256dh(byte[] p256dh) {
		this.p256dh = p256dh;
	}

	public byte[] getAuth() {
		return auth;
	}

	public void setAuth(byte[] auth) {
		this.auth = auth;
	}
	
}
