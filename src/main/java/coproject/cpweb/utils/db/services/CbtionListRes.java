package coproject.cpweb.utils.db.services;

import java.util.List;

import coproject.cpweb.utils.db.entities.Cbtion;

public class CbtionListRes {
	private List<Cbtion> cbtions;
	private int[] resSet = {0,0,0};
	
	public List<Cbtion> getCbtions() {
		return cbtions;
	}
	public void setCbtions(List<Cbtion> cbtions) {
		this.cbtions = cbtions;
	}
	public int[] getResSet() {
		return resSet;
	}
	public void setResSet(int[] resSet) {
		this.resSet = resSet;
	}
}
