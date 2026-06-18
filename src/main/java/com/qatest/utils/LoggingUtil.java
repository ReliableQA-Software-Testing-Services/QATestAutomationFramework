/**
 * 
 */
package com.qatest.utils;

import java.util.Map;

import com.aventstack.extentreports.Status;

/**
 * @author Divyang
 *
 */
public class LoggingUtil {

	/**
	 * 
	 */
	public LoggingUtil() {
		// TODO Auto-generated constructor stub
	}
	//a method that converts the Map into a clean, formatted HTML table to be displayed inside the Extent Report
	public static void logDataMap(Map<String, String> data) {
	    StringBuilder table = new StringBuilder();
	    table.append("<table style='width:100%; border: 1px solid black; border-collapse: collapse;'>");
	    //table.append("<tr style='background-color: #f2f2f2;'><th style='border: 1px solid black;'>Column</th><th style='border: 1px solid black;'>Value</th></tr>");
	    //removing background-color for dark mode
	    table.append("<tr><th style='border: 1px solid black;'>Column</th><th style='border: 1px solid black;'>Value</th></tr>");
	    
	    for (Map.Entry<String, String> entry : data.entrySet()) {
	        table.append("<tr>");
	        table.append("<td style='border: 1px solid black; padding: 5px;'>" + entry.getKey() + "</td>");
	        table.append("<td style='border: 1px solid black; padding: 5px;'>" + entry.getValue() + "</td>");
	        table.append("</tr>");
	    }
	    table.append("</table>");
	    
	    // Log the HTML table to the Extent Report
	    Listeners.getTest().log(Status.INFO, "<b>Test Data Used:</b><br>" + table.toString());
	}
}
