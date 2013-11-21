package Utils;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;

import com.cai.workhourstracker.model.Entry;

public class DateCalculateUtils {
	private String workTimeElapsedFormated() {
		double hours = workTimeElapsed();
		String workHoursFormated = formatTimeHours(hours);

		return workHoursFormated;
	}
	
	public static Integer getWeekNumber(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTime(date);
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	public static double differenceBetweenTwoDatesInMinutes(Date start, Date end){
		long diffInMillies = end.getTime() - start.getTime();
		int differenceInMinutes = (int) (diffInMillies / (1000 * 60));
		double hours = (double) differenceInMinutes / 60;
		
		return hours;
	}
	

	private double workTimeElapsed() {

		long differenceInMinutes = workTimeInMinutes();
		double hours = (double) differenceInMinutes / 60;

		return hours;
	}
	
	private Integer workTimeInMinutes() {
		Date currentDate = new Date();
		//long diffInMillies = currentDate.getTime() - startWorkDate.getTime();
		//int differenceInMinutes = (int) (diffInMillies / (1000 * 60));

		//return differenceInMinutes;
		return 0;
	}
	
	private String formatTimeHours(double hours) {
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(2);

		return format.format(hours);
	}
}
