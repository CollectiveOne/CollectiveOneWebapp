package org.collectiveone.services;

import java.sql.Timestamp;

public interface TimeServiceIf {

	public Timestamp getNow();
	
	public void setNow(Timestamp now);
	
}
