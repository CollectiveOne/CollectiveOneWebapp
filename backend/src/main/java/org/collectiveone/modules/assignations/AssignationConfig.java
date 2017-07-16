package org.collectiveone.modules.assignations;

import java.sql.Timestamp;
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
	
	@Column(name = "max_closure_date")
	private Timestamp maxClosureDate;
	
	@Column(name = "min_closure_date")
	private Timestamp minClosureDate;
	
	@Column(name = "self_bias_visible")
	private Boolean selfBiasVisible;
	
	@Column(name = "evaluations_visible")
	private Boolean evaluationsVisible;
	
	public AssignationConfigDto toDto() {
		AssignationConfigDto dto = new AssignationConfigDto();
		
		dto.setMaxClosureDate(maxClosureDate.getTime());
		dto.setMinClosureDate(minClosureDate.getTime());
		dto.setSelfBiasVisible(selfBiasVisible);
		dto.setEvaluationsVisible(evaluationsVisible);
		
		return dto;
	}
	
	
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
	public Timestamp getMaxClosureDate() {
		return maxClosureDate;
	}
	public void setMaxClosureDate(Timestamp maxClosureDate) {
		this.maxClosureDate = maxClosureDate;
	}
	public Timestamp getMinClosureDate() {
		return minClosureDate;
	}
	public void setMinClosureDate(Timestamp minClosureDate) {
		this.minClosureDate = minClosureDate;
	}
	
}