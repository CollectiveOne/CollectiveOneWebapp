package org.collectiveone.modules.activity;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class TimeService {
	
	public Timestamp fiveMinutesAgo() {
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.add(Calendar.MINUTE, -5);
		return new Timestamp(c.getTimeInMillis());
	}
	
	public Timestamp inTwoMinutes () {
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.add(Calendar.MINUTE, 2);
		return new Timestamp(c.getTimeInMillis());
	}
	
	public Timestamp tomorrow() {
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.add(Calendar.DAY_OF_MONTH, 1);
//		c.add(Calendar.MINUTE, 1);
		return new Timestamp(c.getTimeInMillis());
	}
	
	public Timestamp nextWeek() {
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.add(Calendar.DAY_OF_MONTH, 7);
		return new Timestamp(c.getTimeInMillis());
	}

}
