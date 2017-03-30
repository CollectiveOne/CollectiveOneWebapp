package org.collectiveone.web.dto;

import java.util.List;

public class CbtionDtoListRes {
	private List<CbtionDto> cbtionsDtos;
	private int[] resSet = {0,0,0};
	
	public List<CbtionDto> getCbtionsDtos() {
		return cbtionsDtos;
	}
	public void setCbtionsDtos(List<CbtionDto> cbtionsDtos) {
		this.cbtionsDtos = cbtionsDtos;
	}
	public int[] getResSet() {
		return resSet;
	}
	public void setResSet(int[] resSet) {
		this.resSet = resSet;
	}
	
}
