package org.collectiveone.modules.uprcl.entities.ext;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.collectiveone.modules.uprcl.entities.Commit;
import org.collectiveone.modules.uprcl.entities.Perspective;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "links")
public class OrderedLink {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	private LinkType type;
	
	@ManyToOne
	private Perspective perspective;
	
	@ManyToOne
	private Commit commit;
	
	@Enumerated(EnumType.STRING)
	private OrderType orderType;
	
	@ManyToOne
	private OrderedLink before;
	
	@ManyToOne
	private OrderedLink after;
	

	public LinkType getType() {
		return type;
	}

	public void setType(LinkType type) {
		this.type = type;
	}

	public Perspective getPerspective() {
		return perspective;
	}

	public void setPerspective(Perspective perspective) {
		this.perspective = perspective;
	}

	public Commit getCommit() {
		return commit;
	}

	public void setCommit(Commit commit) {
		this.commit = commit;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public OrderedLink getBefore() {
		return before;
	}

	public void setBefore(OrderedLink before) {
		this.before = before;
	}

	public OrderedLink getAfter() {
		return after;
	}

	public void setAfter(OrderedLink after) {
		this.after = after;
	}
	
}
