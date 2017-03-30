package org.collectiveone.services;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

@Service
class TimeServiceImp implements TimeServiceIf {
	
	public Timestamp getNow() {
		return new Timestamp(System.currentTimeMillis());
	}

	@Override
	public void setNow(Timestamp now) {
	}
}
