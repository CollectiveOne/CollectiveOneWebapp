package org.collectiveone.modules.assignations;

import java.sql.Timestamp;

public class InitiativeMetaDto {
	
	private String name;
	private String driver;
	private Timestamp creationDate;
	private String color;
	private Boolean modelEnabled;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Boolean getModelEnabled() {
		return modelEnabled;
	}
	public void setModelEnabled(Boolean modelEnabled) {
		this.modelEnabled = modelEnabled;
	}
	

}
