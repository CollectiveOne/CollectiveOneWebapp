package org.collectiveone.modules.assignations.evaluationlogic;

import java.util.ArrayList;
import java.util.List;

public class ReceiverData {
	
	public String receiverId;
	public List<EvaluationData> evaluations = new ArrayList<EvaluationData>();
	public double nonNormalizedPercent;
	public double normalizedPercent;
	
	
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	public List<EvaluationData> getEvaluations() {
		return evaluations;
	}
	public void setEvaluations(List<EvaluationData> evaluations) {
		this.evaluations = evaluations;
	}
	public double getNonNormalizedPercent() {
		return nonNormalizedPercent;
	}
	public void setNonNormalizedPercent(double nonNormalizedPercent) {
		this.nonNormalizedPercent = nonNormalizedPercent;
	}
	public double getNormalizedPercent() {
		return normalizedPercent;
	}
	public void setNormalizedPercent(double normalizedPercent) {
		this.normalizedPercent = normalizedPercent;
	}
	
	

}
