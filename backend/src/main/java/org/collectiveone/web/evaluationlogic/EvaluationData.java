package org.collectiveone.web.evaluationlogic;

public class EvaluationData {
	public String evaluatorId;
	public double value;
	public double weight;
	
	public EvaluationData(String _evaluatorId, double _value, double _weight) {
		evaluatorId = _evaluatorId;
		value = _value;
		weight = _weight;
	}
	
	public String getEvaluatorId() {
		return evaluatorId;
	}
	public void setEvaluatorId(String evaluatorId) {
		this.evaluatorId = evaluatorId;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
}
