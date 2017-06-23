package org.collectiveone.modules.assignations;

import java.util.ArrayList;
import java.util.List;

public class EvaluationDto {
	
	private String id;
	private String evaluationState;
	private List<EvaluationGradeDto> evaluationGrades = new ArrayList<EvaluationGradeDto>();
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEvaluationState() {
		return evaluationState;
	}
	public void setEvaluationState(String evaluationState) {
		this.evaluationState = evaluationState;
	}
	public List<EvaluationGradeDto> getEvaluationGrades() {
		return evaluationGrades;
	}
	public void setEvaluationGrades(List<EvaluationGradeDto> evaluationGrades) {
		this.evaluationGrades = evaluationGrades;
	}
	

}
