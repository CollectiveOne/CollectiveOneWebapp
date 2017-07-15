package org.collectiveone.modules.assignations;

import java.util.ArrayList;
import java.util.List;

public class AssignationDto extends AssignationDtoLight {
	
	private List<ReceiverDto> receivers = new ArrayList<ReceiverDto>();
	private List<EvaluatorDto> evaluators = new ArrayList<EvaluatorDto>();
	private EvaluationDto thisEvaluation;
	private Boolean selfBiasVisible;
	private Boolean evaluationsVisible;
	
	public List<ReceiverDto> getReceivers() {
		return receivers;
	}
	public void setReceivers(List<ReceiverDto> receivers) {
		this.receivers = receivers;
	}
	public List<EvaluatorDto> getEvaluators() {
		return evaluators;
	}
	public void setEvaluators(List<EvaluatorDto> evaluators) {
		this.evaluators = evaluators;
	}
	public EvaluationDto getThisEvaluation() {
		return thisEvaluation;
	}
	public void setThisEvaluation(EvaluationDto thisEvaluation) {
		this.thisEvaluation = thisEvaluation;
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
