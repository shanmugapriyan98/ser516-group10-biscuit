package com.biscuit.models.services;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateService {

	/**
	 * Setting the date template.
	 */
	public static transient SimpleDateFormat dateFormat = new SimpleDateFormat("MMM. dd, yyyy");


	/**
	 * Return if date is set or not.
	 * @param d Date object.
	 * @return Boolean object.
	 */
	public static boolean isSet(Date d) {
		return (d.compareTo(new Date(0)) > 0);
	}


	/**
	 * Method to get date in a String format.
	 * @param d Date object.
	 * @return Date as a String.
	 */
	public static String getDateAsString(Date d) {
		if (DateService.isSet(d)) {
			return DateService.dateFormat.format(d);
		}
		return "Not set yet";
	}

}
