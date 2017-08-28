package org.collectiveone.modules.assignations;

public class AssignationConfigDto {
	private Long maxClosureDate;
	private Long minClosureDate;
	private Integer maxDuration;
	private Integer minDuration;
	private Boolean selfBiasVisible;
	private Boolean evaluationsVisible;
	
	
	public Long getMaxClosureDate() {
		return maxClosureDate;
	}
	public void setMaxClosureDate(Long maxClosureDate) {
		this.maxClosureDate = maxClosureDate;
	}
	public Long getMinClosureDate() {
		return minClosureDate;
	}
	public void setMinClosureDate(Long minClosureDate) {
		this.minClosureDate = minClosureDate;
	}
	public Integer getMaxDuration() {
		return maxDuration;
	}
	public void setMaxDuration(Integer maxDuration) {
		this.maxDuration = maxDuration;
	}
	public Integer getMinDuration() {
		return minDuration;
	}
	public void setMinDuration(Integer minDuration) {
		this.minDuration = minDuration;
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
