package com.cai.workhourstracker.helper;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;

import com.cai.workhourstracker.model.EntriesGroupViewRow;
import com.cai.workhourstracker.model.Entry;
import com.cai.workhourstracker.model.PayPeriod;

public class Utils {

	public static List<String> getHeaders(Map<String, List<Entry>> all) {
		List<String> headers = new ArrayList<String>(all.keySet());

		return headers;
	}

	public static Map<String, List<EntriesGroupViewRow>> groupEachEntryGroup(
			Map<String, List<Entry>> all, Context context) {
		HashMap<String, List<EntriesGroupViewRow>> groups = new HashMap<String, List<EntriesGroupViewRow>>();

		for (String key : all.keySet()) {
			HashMap<String, List<Entry>> groupedByJob = groupEntriesByJob(
					all.get(key), context);
			List<EntriesGroupViewRow> entiesGroupViewRows = createEntiesGroupViewRow(groupedByJob);

			groups.put(key, entiesGroupViewRows);
		}

		return groups;
	}

	private static List<EntriesGroupViewRow> createEntiesGroupViewRow(
			HashMap<String, List<Entry>> groupedByJob) {
		List<EntriesGroupViewRow> result = new ArrayList<EntriesGroupViewRow>();

		for (String key : groupedByJob.keySet()) {
			EntriesGroupViewRow entriesGroupViewRow = new EntriesGroupViewRow(
					key, groupedByJob.get(key));
			result.add(entriesGroupViewRow);
		}

		return result;
	}

	private static HashMap<String, List<Entry>> groupEntriesByJob(
			List<Entry> entries, Context context) {
		HashMap<String, List<Entry>> eachSectionGroups = new HashMap<String, List<Entry>>();
		DatabaseHelper db = new DatabaseHelper(context);

		for (int i = 0; i < entries.size(); i++) {
			String currentJob = db.getJobById(entries.get(i).getJobId())
					.getName();
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
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
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

	public static int calculateHoursWorked(
			List<EntriesGroupViewRow> currentSection) {
		int sum = 0;

		for (EntriesGroupViewRow entriesGroupViewRow : currentSection) {
			sum += entriesGroupViewRow.getWorkHours();
		}

		return sum;
	}

	public static String calculateMoneyEarned(
			List<EntriesGroupViewRow> currentSection) {
		int sum = 0;
		for (EntriesGroupViewRow entriesGroupViewRow : currentSection) {
			sum += entriesGroupViewRow.getMoneyEarned();
		}

		Currency currency = Currency.getInstance(Locale.getDefault());
		BigDecimal money = (new BigDecimal(sum)).divide(new BigDecimal(100));
	
		return (currency.getSymbol() + new DecimalFormat("#,##0.00").format(money));
	}
}
