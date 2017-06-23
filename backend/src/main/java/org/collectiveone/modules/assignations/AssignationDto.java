package org.collectiveone.modules.assignations;

import java.util.ArrayList;
import java.util.List;

public class AssignationDto extends AssignationDtoLight {
	
	private List<ReceiverDto> receivers = new ArrayList<ReceiverDto>();
	private List<EvaluatorDto> evaluators = new ArrayList<EvaluatorDto>();
	private EvaluationDto thisEvaluation;
	
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
	
}
