package coproject.cpweb.utils.db.services;

import java.util.List;

import coproject.cpweb.utils.db.entities.dtos.CbtionDto;

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
