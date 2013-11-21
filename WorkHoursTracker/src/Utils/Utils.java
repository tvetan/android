package Utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.cai.workhourstracker.helper.DatabaseHelper;
import com.cai.workhourstracker.model.EntriesGroupViewRow;
import com.cai.workhourstracker.model.Entry;
import com.cai.workhourstracker.model.PayPeriod;

public class Utils {

	public static <T> List<String> getHeaders(LinkedHashMap<String, List<T>> all) {
		List<String> headers = new ArrayList<String>(all.keySet());

		return headers;
	}

	public static LinkedHashMap<String, List<EntriesGroupViewRow>> groupEachPayPeriodGroupByJob(
			LinkedHashMap<String, List<PayPeriod>> all, Context context) {
		LinkedHashMap<String, List<EntriesGroupViewRow>> groups = new LinkedHashMap<String, List<EntriesGroupViewRow>>();

		for (String key : all.keySet()) {
			LinkedHashMap<String, List<Entry>> groupedByJob = groupPayPeriodsByJob(all.get(key),
					context);
			List<EntriesGroupViewRow> entiesGroupViewRows = createEntiesGroupViewRow(groupedByJob);

			groups.put(key, entiesGroupViewRows);
		}

		return groups;
	}

	private static LinkedHashMap<String, List<Entry>> groupPayPeriodsByJob(
			List<PayPeriod> payPeriod, Context context) {
		LinkedHashMap<String, List<Entry>> eachSectionGroups = new LinkedHashMap<String, List<Entry>>();
		DatabaseHelper db = new DatabaseHelper(context);

		for (int i = 0; i < payPeriod.size(); i++) {
			String currentJob = db.getJobById(payPeriod.get(i).getJobId()).getName();
			List<Entry> payPeriodEntries = db.getAllEntriesByPayPeriodId(payPeriod.get(i).getId());
			if (eachSectionGroups.containsKey(currentJob)) {
				List<Entry> currentEntries = eachSectionGroups.get(currentJob);

				currentEntries.addAll(payPeriodEntries);
				eachSectionGroups.put(currentJob, currentEntries);
			} else {
				List<Entry> currentEntries = new ArrayList<Entry>();
				currentEntries.addAll(payPeriodEntries);
				eachSectionGroups.put(currentJob, currentEntries);
			}
		}

		db.close();
		return eachSectionGroups;
	}

	public static LinkedHashMap<String, List<EntriesGroupViewRow>> groupEachPayPeriodGroupByDate(
			LinkedHashMap<String, List<PayPeriod>> all, Context context) {
		LinkedHashMap<String, List<EntriesGroupViewRow>> groups = new LinkedHashMap<String, List<EntriesGroupViewRow>>();

		for (String key : all.keySet()) {
			LinkedHashMap<String, List<Entry>> groupedByJob = groupPayPeriodsByDate(all.get(key),
					context);
			List<EntriesGroupViewRow> entiesGroupViewRows = createEntiesGroupViewRow(groupedByJob);

			groups.put(key, entiesGroupViewRows);
		}

		return groups;
	}

	public static String changeDateStringFormat(SimpleDateFormat format, String dateAsString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale
				.getDefault());
		Date date;

		try {
			date = dateFormat.parse(dateAsString);
			return format.format(date);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	private static LinkedHashMap<String, List<Entry>> groupPayPeriodsByDate(
			List<PayPeriod> payPeriods, Context context) {
		LinkedHashMap<String, List<Entry>> eachSectionGroups = new LinkedHashMap<String, List<Entry>>();
		DatabaseHelper db = new DatabaseHelper(context);
		SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

		for (int i = 0; i < payPeriods.size(); i++) {
			String currentDay = changeDateStringFormat(formatter, payPeriods.get(i).getDate());
			List<Entry> entryFromPayPeriod = db.getAllEntriesByPayPeriodId(payPeriods.get(i)
					.getId());
			if (eachSectionGroups.containsKey(currentDay)) {
				List<Entry> currentEntries = eachSectionGroups.get(currentDay);
				currentEntries.addAll(entryFromPayPeriod);
				eachSectionGroups.put(currentDay, currentEntries);
			} else {
				List<Entry> currentEntries = new ArrayList<Entry>();
				currentEntries.addAll(entryFromPayPeriod);
				eachSectionGroups.put(currentDay, currentEntries);
			}
		}

		db.close();
		return eachSectionGroups;
	}

	public static LinkedHashMap<String, List<EntriesGroupViewRow>> groupEachEntryGroup(
			LinkedHashMap<String, List<Entry>> all, Context context) {
		LinkedHashMap<String, List<EntriesGroupViewRow>> groups = new LinkedHashMap<String, List<EntriesGroupViewRow>>();

		for (String key : all.keySet()) {
			LinkedHashMap<String, List<Entry>> groupedByJob = groupEntriesByJob(all.get(key),
					context);
			List<EntriesGroupViewRow> entiesGroupViewRows = createEntiesGroupViewRow(groupedByJob);

			groups.put(key, entiesGroupViewRows);
		}

		return groups;
	}

	private static List<EntriesGroupViewRow> createEntiesGroupViewRow(
			LinkedHashMap<String, List<Entry>> groupedByJob) {
		List<EntriesGroupViewRow> result = new ArrayList<EntriesGroupViewRow>();

		for (String key : groupedByJob.keySet()) {
			EntriesGroupViewRow entriesGroupViewRow = new EntriesGroupViewRow(key, groupedByJob
					.get(key));
			result.add(entriesGroupViewRow);
		}

		return result;
	}

	private static LinkedHashMap<String, List<Entry>> groupEntriesByJob(List<Entry> entries,
			Context context) {
		LinkedHashMap<String, List<Entry>> eachSectionGroups = new LinkedHashMap<String, List<Entry>>();
		DatabaseHelper db = new DatabaseHelper(context);

		for (int i = 0; i < entries.size(); i++) {
			String currentJob = db.getJobById(entries.get(i).getJobId()).getName();
			if (eachSectionGroups.containsKey(currentJob)) {
				List<Entry> currentEntries = eachSectionGroups.get(currentJob);
				currentEntries.add(entries.get(i));
				eachSectionGroups.put(currentJob, currentEntries);
			} else {
				List<Entry> currentEntries = new ArrayList<Entry>();
				currentEntries.add(entries.get(i));
				eachSectionGroups.put(currentJob, currentEntries);
			}
		}

		db.close();
		return eachSectionGroups;
	}

	public static int differenceBetweenStartAndStop(Entry entry) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale
				.getDefault());
		try {

			Date startClock = dateFormat.parse(entry.getStartClock());
			Date endClock = dateFormat.parse(entry.getStopClock());
			int workHours = (int) ((endClock.getTime() - startClock.getTime()) / (1000 * 60 * 60));

			return workHours;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	public static int calculateHoursWorked(List<EntriesGroupViewRow> currentSection) {
		int sum = 0;

		for (EntriesGroupViewRow entriesGroupViewRow : currentSection) {
			sum += entriesGroupViewRow.getWorkHours();
		}

		return sum;
	}

	public static String calculateMoneyEarned(List<EntriesGroupViewRow> currentSection) {
		int sum = 0;
		for (EntriesGroupViewRow entriesGroupViewRow : currentSection) {
			sum += entriesGroupViewRow.getMoneyEarned();
		}

		Currency currency = Currency.getInstance(Locale.getDefault());
		BigDecimal money = (new BigDecimal(sum)).divide(new BigDecimal(100));

		return (currency.getSymbol() + new DecimalFormat("#,##0.00").format(money));
	}

	public static int workHoursForEntries(List<Entry> entries) {
		int workHours = 0;

		for (Entry entry : entries) {
			int currentWorkHours = differenceBetweenStartAndStop(entry);
			workHours += currentWorkHours;
		}

		return workHours;
	}

	public static int moneyEarnedEntries(List<Entry> entries) {
		int moneyEarned = 0;

		for (Entry entry : entries) {
			int currentMoneyEarned = entry.getEarnedMoney();
			moneyEarned += currentMoneyEarned;
		}

		return moneyEarned;
	}

	public static String convertMoneyToString(int money) {
		BigDecimal moneyEarned = (new BigDecimal(money)).divide(new BigDecimal(100));
		return "$" + new DecimalFormat("#,##0.00").format(moneyEarned);
	}

	public static String dateToMonthDateYearFormat(String dateAsString) {
		SimpleDateFormat yearMonthDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale
				.getDefault());
		SimpleDateFormat monthDateYearFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale
				.getDefault());
		try {
			Date date = yearMonthDateFormat.parse(dateAsString);
			return monthDateYearFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static String dateToDayOfWeek(String dateAsString) {
		SimpleDateFormat yearMonthDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale
				.getDefault());
		SimpleDateFormat monthFormat = new SimpleDateFormat("EEE", Locale.getDefault());

		try {
			Date date = yearMonthDateFormat.parse(dateAsString);
			return monthFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static String dateToHour(String dateAsString) {
		SimpleDateFormat yearMonthDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale
				.getDefault());
		SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm aa", Locale.getDefault());

		try {
			Date date = yearMonthDateFormat.parse(dateAsString);
			return hourFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static Integer stringToIntegerBaseRate(String baseRate) {
		String stripedBaseRate = baseRate.replaceAll("[$,/hour,%]", "");

		if (EmptyUtils.isNullOrEmpty(stripedBaseRate)) {
			return 0;
		}
		BigDecimal baseRateBigDecimal = (new BigDecimal(stripedBaseRate)).multiply(new BigDecimal(
				100));

		return baseRateBigDecimal.intValueExact();
	}
}
