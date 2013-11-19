package com.cai.workhourstracker.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatUtils {

	private static final String DATABASE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String HOUR_MINUTES_FORMAT = "HH:mm aa";

	public static String formatToHourAndMinutes(String dateAsString) {
		SimpleDateFormat databaseFormat = new SimpleDateFormat(DATABASE_FORMAT, Locale.getDefault());
		SimpleDateFormat hourMinuteFormat = new SimpleDateFormat(HOUR_MINUTES_FORMAT, Locale
				.getDefault());

		try {
			Date date = databaseFormat.parse(dateAsString);
			return hourMinuteFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static Date fromDatabaseFormatToDate(String dateAsString) {
		SimpleDateFormat databaseFormat = new SimpleDateFormat(DATABASE_FORMAT, Locale.getDefault());

		try {
			return databaseFormat.parse(dateAsString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return new Date();
	}

	public static String toDatabaseFormat(Date date) {
		SimpleDateFormat databaseFormat = new SimpleDateFormat(DATABASE_FORMAT, Locale.getDefault());

		return databaseFormat.format(date);
	}
}
