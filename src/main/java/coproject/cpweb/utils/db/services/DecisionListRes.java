package coproject.cpweb.utils.db.services;

import java.util.List;

import coproject.cpweb.utils.db.entities.Decision;

public class DecisionListRes {
	private List<Decision> decisions;
	private int[] resSet = {0,0,0};
	
	public List<Decision> getDecisions() {
		return decisions;
	}
	public void setDecisions(List<Decision> decisions) {
		this.decisions = decisions;
	}
	public int[] getResSet() {
		return resSet;
	}
	public void setResSet(int[] resSet) {
		this.resSet = resSet;
	}
}
