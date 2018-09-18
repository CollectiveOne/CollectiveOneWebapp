package org.collectiveone.modules.contexts.dto;

import java.util.ArrayList;
import java.util.List;

public class CardWrappersHolderDto {
	
	private List<ModelCardWrapperDto> cardsWrappersPrivate = new ArrayList<ModelCardWrapperDto>();
	private List<ModelCardWrapperDto> cardsWrappersShared = new ArrayList<ModelCardWrapperDto>();
	private List<ModelCardWrapperDto> cardsWrappersCommon = new ArrayList<ModelCardWrapperDto>();
	
	public List<ModelCardWrapperDto> getCardsWrappersPrivate() {
		return cardsWrappersPrivate;
	}
	public void setCardsWrappersPrivate(List<ModelCardWrapperDto> cardsWrappersPrivate) {
		this.cardsWrappersPrivate = cardsWrappersPrivate;
	}
	public List<ModelCardWrapperDto> getCardsWrappersShared() {
		return cardsWrappersShared;
	}
	public void setCardsWrappersShared(List<ModelCardWrapperDto> cardsWrappersShared) {
		this.cardsWrappersShared = cardsWrappersShared;
	}
	public List<ModelCardWrapperDto> getCardsWrappersCommon() {
		return cardsWrappersCommon;
	}
	public void setCardsWrappersCommon(List<ModelCardWrapperDto> cardsWrappersCommon) {
		this.cardsWrappersCommon = cardsWrappersCommon;
	}
	
}
