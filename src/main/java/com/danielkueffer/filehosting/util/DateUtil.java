package com.danielkueffer.filehosting.util;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Date utilities
 * 
 * @author dkueffer
 * 
 */
public class DateUtil {
	/**
	 * Get a SQL TIMESTAMP
	 * 
	 * @return
	 */
	public static Timestamp getSQLTimestamp() {
		Date date = new Date();
		return new Timestamp(date.getTime());
	}
}
