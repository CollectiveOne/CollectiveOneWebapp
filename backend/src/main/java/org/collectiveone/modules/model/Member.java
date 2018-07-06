package org.collectiveone.modules.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.collectiveone.modules.initiatives.Initiative;
import org.collectiveone.modules.tokens.MemberTransfer;
import org.collectiveone.modules.users.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "members")
public class Member implements Comparable<Member>{

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	//#### delete this
	@ManyToOne
	private Initiative initiative;

	@ManyToOne
	private ModelSection modelSection;
	
	@ManyToOne
	private AppUser user;
	
	@OneToMany(mappedBy = "member")
	private List<MemberTransfer> tokensTransfers = new ArrayList<MemberTransfer>();


	@Override
    public int compareTo(Member m) {
        return user.getC1Id().compareTo(m.getUser().getC1Id());
    }
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public ModelSection getModelSection() {
		return modelSection;
	}

	public void setModelSection(ModelSection modelSection) {
		this.modelSection = modelSection;
	}

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public List<MemberTransfer> getTokensTransfers() {
		return tokensTransfers;
	}

	public void setTokensTransfers(List<MemberTransfer> tokensTransfers) {
		this.tokensTransfers = tokensTransfers;
	}

	public Initiative getInitiative()
	{
		return this.initiative;
	}

	public void setInitiative(Initiative initiative)
	{
		this.initiative = initiative;
	}

	
	

}
