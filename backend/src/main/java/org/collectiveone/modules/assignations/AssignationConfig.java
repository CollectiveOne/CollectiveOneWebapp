package org.collectiveone.modules.assignations;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table( name = "assignations_configs" )
public class AssignationConfig {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@Column(name = "self_bias_visible")
	private Boolean selfBiasVisible;
	
	@Column(name = "evaluations_visible")
	private Boolean evaluationsVisible;
	
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public Boolean getSelfBiasVisible() {
		return selfBiasVisible;
	}
	public void setSelfBiasVisible(Boolean selfBiasVisible) {
		this.selfBiasVisible = selfBiasVisible;
	}
	public Boolean getEvaluationsVisible() {
		return evaluationsVisible;
	}
	public void setEvaluationsVisible(Boolean evaluationsVisible) {
		this.evaluationsVisible = evaluationsVisible;
	}
	
	
}