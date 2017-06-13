package org.collectiveone.web.dto;

import java.util.ArrayList;
import java.util.List;

public class EvaluationDto {
	
	private String id;
	private String evaluationState;
	private List<ReceiverDto> receivers = new ArrayList<ReceiverDto>();
	
	
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
	public List<ReceiverDto> getReceivers() {
		return receivers;
	}
	public void setReceivers(List<ReceiverDto> receivers) {
		this.receivers = receivers;
	}

}
