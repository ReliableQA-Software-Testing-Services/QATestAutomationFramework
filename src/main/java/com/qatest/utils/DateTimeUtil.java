/**
 * 
 */
package com.qatest.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;

/**
 * @author Divyang
 *
 */
public class DateTimeUtil {

	/**
	 * 
	 */
	public DateTimeUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static String getCurrentTimestamp() {
		// Get current date and time
        LocalDateTime now = LocalDateTime.now();

        // Define the formatter with milliseconds
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS");
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-Mmm-yyyy_HHmmssSSS");

        // Format the timestamp
        String timestamp = now.format(formatter);
        return timestamp;

	}
	
	public static String getCurrentTimestampInUTC(){
		// Get current date and time in UTC timezone
		String utcCurrentDatetime = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS"));
	
		//System.out.println("Current UTC time is: " + utcCurrentDatetime);
		return utcCurrentDatetime;
	}
}
