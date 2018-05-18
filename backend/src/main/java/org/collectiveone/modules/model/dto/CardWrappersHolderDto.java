package org.collectiveone.modules.model.dto;

import java.util.ArrayList;
import java.util.List;

public class CardWrappersHolderDto {
	
	private List<ModelCardWrapperDto> cardsWrappersPrivate = new ArrayList<ModelCardWrapperDto>();
	private List<ModelCardWrapperDto> cardsWrappersPersonal = new ArrayList<ModelCardWrapperDto>();
	private List<ModelCardWrapperDto> cardsWrappers = new ArrayList<ModelCardWrapperDto>();
	
	public List<ModelCardWrapperDto> getCardsWrappersPrivate() {
		return cardsWrappersPrivate;
	}
	public void setCardsWrappersPrivate(List<ModelCardWrapperDto> cardsWrappersPrivate) {
		this.cardsWrappersPrivate = cardsWrappersPrivate;
	}
	public List<ModelCardWrapperDto> getCardsWrappersPersonal() {
		return cardsWrappersPersonal;
	}
	public void setCardsWrappersPersonal(List<ModelCardWrapperDto> cardsWrappersPersonal) {
		this.cardsWrappersPersonal = cardsWrappersPersonal;
	}
	public List<ModelCardWrapperDto> getCardsWrappers() {
		return cardsWrappers;
	}
	public void setCardsWrappers(List<ModelCardWrapperDto> cardsWrappers) {
		this.cardsWrappers = cardsWrappers;
	}	
	
}
