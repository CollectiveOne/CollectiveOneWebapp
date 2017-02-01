package org.collectiveone.services;

import java.util.List;

import org.collectiveone.web.dto.DecisionDtoFull;

public class DecisionDtoListRes {
	private List<DecisionDtoFull> decisionDtos;
	private int[] resSet = {0,0,0};
	
	public List<DecisionDtoFull> getDecisionDtos() {
		return decisionDtos;
	}
	public void setDecisionDtos(List<DecisionDtoFull> decisionDtos) {
		this.decisionDtos = decisionDtos;
	}
	public int[] getResSet() {
		return resSet;
	}
	public void setResSet(int[] resSet) {
		this.resSet = resSet;
	}
}
