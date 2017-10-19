package org.collectiveone.modules.assignations.dto;

import java.util.ArrayList;
import java.util.List;

import org.collectiveone.modules.users.AppUserDto;

public class EvaluatorDto {
	
	private String id;
	private AppUserDto user;
	private double weight;
	private String state;
	private List<EvaluationGradeDto> grades = new ArrayList<EvaluationGradeDto>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public AppUserDto getUser() {
		return user;
	}
	public void setUser(AppUserDto user) {
		this.user = user;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<EvaluationGradeDto> getGrades() {
		return grades;
	}
	public void setGrades(List<EvaluationGradeDto> grades) {
		this.grades = grades;
	}
	
}
