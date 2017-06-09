package org.collectiveone.web.dto;

import java.util.ArrayList;
import java.util.List;

public class AssignationDto {
	
	private String type;
	private List<BillDto> assets = new ArrayList<BillDto>();
	private List<ReceiverDto> receivers = new ArrayList<ReceiverDto>();
	private List<EvaluatorDto> evaluators = new ArrayList<EvaluatorDto>();
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<BillDto> getAssets() {
		return assets;
	}
	public void setAssets(List<BillDto> assets) {
		this.assets = assets;
	}
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
	
	
}
