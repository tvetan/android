package Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

public class DateFormatUtils {

	private static final String DATABASE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String HOUR_MINUTES_FORMAT = "HH:mm aa";
	private static final String MINUTES_FORMAT = "mm";
	private static final String DATE_MONTH_YEAR_FORMAT = "dd MMMM yyyy";

	public static Date fromDateMonthYear(String dateAsString) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_MONTH_YEAR_FORMAT, Locale
				.getDefault());
		
		try {
			return formatter.parse(dateAsString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public static Integer getMinutes(String dateAsString) {
		return getDateField(Calendar.MINUTE, dateAsString);
	}

	public static Integer getHours(String dateAsString) {
		return getDateField(Calendar.HOUR_OF_DAY, dateAsString);
	}

	private static Integer getDateField(int field, String dateAsString) {
		SimpleDateFormat databaseFormat = new SimpleDateFormat(DATABASE_FORMAT, Locale.getDefault());
		try {
			Date date = databaseFormat.parse(dateAsString);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			return calendar.get(field);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

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
