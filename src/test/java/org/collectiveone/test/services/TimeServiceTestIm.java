package org.collectiveone.test.services;

/* Exposes a timestamp to set during tests */
import java.sql.Timestamp;

import org.collectiveone.services.TimeServiceIf;

public class TimeServiceTestIm implements TimeServiceIf {
	
	public Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	
	public Timestamp now() {
		return timestamp;
		
	}

	@Override
	public Timestamp getNow() {
		return timestamp;
	}

	@Override
	public void setNow(Timestamp now) {
		timestamp = now;
	}
}
