package com.cai.workhourstracker;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.cai.workhourstracker.JobsFragment.SectionComposerAdapter;
import com.cai.workhourstracker.Views.GroupListView;
import com.cai.workhourstracker.adapters.GroupViewAdapter;
import com.cai.workhourstracker.helper.DatabaseHelper;
import com.cai.workhourstracker.helper.Utils;
import com.cai.workhourstracker.model.EntriesGroupViewRow;
import com.cai.workhourstracker.model.Entry;
import com.cai.workhourstracker.model.EntryStartClockComparator;
import com.cai.workhourstracker.model.Job;
import com.cai.workhourstracker.model.JobNameComparator;
import com.cai.workhourstracker.model.JobNameReverseComparator;
import com.cai.workhourstracker.model.PayPeriod;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EntriesFragment extends Fragment implements
		AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

	private DatabaseHelper db;
	private GroupListView listView;
	private SectionComposerAdapter adapter;
	private List<Entry> entries;
	private Map<String, List<Entry>> all;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_entries, container,
				false);

		rootView.findViewById(R.id.actionbar_done).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(getActivity(),
								ExportSelectionActivity.class));
					}
				});
		rootView.findViewById(R.id.actionbar_cancel).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// "Cancel"
					}
				});

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		listView = (GroupListView) getActivity().findViewById(
				R.id.lsComposerEntries);
		listView.setPinnedHeaderView(LayoutInflater.from(getActivity())
				.inflate(R.layout.group_view_header, listView, false));
		listView.setOnItemClickListener(this);
		db = new DatabaseHelper(getActivity());
		entries = db.getAllEntries();
		db.close();

		all = groupByMonth();
		listView.setAdapter(adapter = new SectionComposerAdapter(Utils
				.getHeaders(all), Utils.groupEachEntryGroup(all, getActivity())));
	}

	private void sortByWeek() {
		// Calendar calendar = Calendar.getInstance();
		// int week = calendar.get(Calendar.WEEK_OF_YEAR);
		// calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		// calendar.set(Calendar.WEEK_OF_YEAR, week);
		// Date date = calendar.getTime();
		// Log.d("date", date.toString());

	}

	private HashMap<String, List<Entry>> groupByDate() {
		HashMap<String, List<Entry>> groupDate = new HashMap<String, List<Entry>>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM, yyyy",
				Locale.getDefault());

		for (int i = 0; i < entries.size(); i++) {
			String currentDay = changeDateStringFormat(formatter, entries
					.get(i).getStartClock());
			if (groupDate.containsKey(currentDay)) {
				List<Entry> currentEntries = groupDate.get(currentDay);
				currentEntries.add(entries.get(i));
				groupDate.put(currentDay, currentEntries);
			} else {
				List<Entry> currentEntries = new ArrayList<Entry>();
				currentEntries.add(entries.get(i));
				groupDate.put(currentDay, currentEntries);
			}
		}

		return groupDate;
	}

	private HashMap<String, List<Entry>> groupByMonth() {
		HashMap<String, List<Entry>> groupMonth = new HashMap<String, List<Entry>>();
		SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy",
				Locale.getDefault());
		for (int i = 0; i < entries.size(); i++) {
			String currentDay = changeDateStringFormat(formatter, entries
					.get(i).getStartClock());
			if (groupMonth.containsKey(currentDay)) {
				List<Entry> currentEntries = groupMonth.get(currentDay);
				currentEntries.add(entries.get(i));
				groupMonth.put(currentDay, currentEntries);
			} else {
				List<Entry> currentEntries = new ArrayList<Entry>();
				currentEntries.add(entries.get(i));
				groupMonth.put(currentDay, currentEntries);
			}
		}

		return groupMonth;
	}

	public String changeDateStringFormat(SimpleDateFormat format,
			String dateAsString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date;

		try {
			date = dateFormat.parse(dateAsString);
			return format.format(date);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setCustomView(R.layout.jobs_filter);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);
		Spinner spinner = (Spinner) getActivity().findViewById(
				R.id.jobs_filter_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.entries_spinner_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);

		inflater.inflate(R.menu.fragment_entries_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onItemSelected(AdapterView<?> arg0, View view, int position,
			long arg3) {
		if (position == 0) {
			all = groupByDate();
			listView.setAdapter(adapter = new SectionComposerAdapter(Utils
					.getHeaders(all), Utils.groupEachEntryGroup(all,
					getActivity())));
		} else if (position == 1) {
			new SortDates().execute(entries);
		} else {
			all = groupByMonth();
			listView.setAdapter(adapter = new SectionComposerAdapter(Utils
					.getHeaders(all), Utils.groupEachEntryGroup(all,
					getActivity())));
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}

	private class SortDates
			extends
			AsyncTask<List<Entry>, Void, HashMap<String, List<EntriesGroupViewRow>>> {

		private Date getDateFromString(String dateAsString) {
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
			Date date;
			try {
				date = formatter.parse(dateAsString);
				return date;
			} catch (ParseException e) {
				e.printStackTrace();
			}

			return null;
		}

		private Integer getWeekNumber(Entry entry) {

			// Calendar calendar = Calendar.getInstance();
			// int week = calendar.get(Calendar.WEEK_OF_YEAR);
			// calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			// calendar.set(Calendar.WEEK_OF_YEAR, week);
			// Date date = calendar.getTime();
			Date date = getDateFromString(entry.getStartClock());
			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			calendar.setTime(date);
			return calendar.get(Calendar.WEEK_OF_YEAR);
		}

		@Override
		protected HashMap<String, List<EntriesGroupViewRow>> doInBackground(
				List<Entry>... params) {

			List<Entry> entryList = params[0];
			HashMap<String, List<Entry>> byWeek = new HashMap<String, List<Entry>>();

			for (int i = 0; i < entryList.size(); i++) {

				String weekNumber = String.valueOf(getWeekNumber(entryList
						.get(i)));

				if (byWeek.containsKey(weekNumber)) {
					List<Entry> currentEntries = byWeek.get(weekNumber);
					currentEntries.add(entryList.get(i));
					byWeek.put(weekNumber, currentEntries);
				} else {
					List<Entry> currentEntries = new ArrayList<Entry>();
					currentEntries.add(entryList.get(i));
					byWeek.put(weekNumber, currentEntries);
				}
			}

			HashMap<String, List<EntriesGroupViewRow>> result = (HashMap<String, List<EntriesGroupViewRow>>) Utils
					.groupEachEntryGroup(byWeek, getActivity());

			return result;
		}

		private String fromDateToWeekString(Date date) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM",
					Locale.getDefault());

			return "week of " + formatter.format(date);
		}

		private List<String> fromWeekNumbersToWeekStrings(
				List<String> weekNumbers) {

			List<String> result = new ArrayList<String>();
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

			for (String number : weekNumbers) {
				calendar.set(Calendar.WEEK_OF_YEAR, Integer.valueOf(number));
				Date currentWeek = calendar.getTime();
				result.add(fromDateToWeekString(currentWeek));
			}

			return result;
		}

		public HashMap<String, List<EntriesGroupViewRow>> convertHeaders(
				List<String> headerWeekStrings,
				HashMap<String, List<EntriesGroupViewRow>> byWeek) {
			HashMap<String, List<EntriesGroupViewRow>> result = new HashMap<String, List<EntriesGroupViewRow>>();
			int index = 0;
			for (String key : byWeek.keySet()) {

				result.put(headerWeekStrings.get(index), byWeek.get(key));
				index++;
			}

			return result;
		}

		protected void onPostExecute(
				HashMap<String, List<EntriesGroupViewRow>> byWeek) {
			List<String> headers = new ArrayList<String>(byWeek.keySet());
			List<String> headerWeekStrings = fromWeekNumbersToWeekStrings(headers);

			HashMap<String, List<EntriesGroupViewRow>> convertedHeaders = convertHeaders(
					headerWeekStrings, byWeek);

			listView.setAdapter(adapter = new SectionComposerAdapter(
					headerWeekStrings, convertedHeaders));
		}
	}

	public class SectionComposerAdapter extends GroupViewAdapter {
		Map<String, List<EntriesGroupViewRow>> groups;
		List<String> sectionHeaders;
		List<List<EntriesGroupViewRow>> payPariodsGroups;

		public SectionComposerAdapter(List<String> sectionHeaders,
				Map<String, List<EntriesGroupViewRow>> groups) {
			this.sectionHeaders = sectionHeaders;
			this.groups = groups;
			this.payPariodsGroups = new ArrayList<List<EntriesGroupViewRow>>(
					groups.values());
		}

		@Override
		public int getCount() {
			int res = 0;
			for (int i = 0; i < payPariodsGroups.size(); i++) {
				res += payPariodsGroups.get(i).size();
			}

			return res;
		}

		@Override
		public EntriesGroupViewRow getItem(int position) {
			int c = 0;

			for (int i = 0; i < payPariodsGroups.size(); i++) {
				if (position >= c
						&& position < c + payPariodsGroups.get(i).size()) {
					return payPariodsGroups.get(i).get(position - c);
				}
				c += payPariodsGroups.get(i).size();
			}

			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		protected void onNextPageRequested(int page) {
		}

		@Override
		protected void bindSectionHeader(View view, int position,
				boolean displaySectionHeader) {
			if (displaySectionHeader) {
				view.findViewById(R.id.header).setVisibility(View.VISIBLE);
				TextView criteria = (TextView) view
						.findViewById(R.id.group_view_header_group_criteria);
				TextView workHours = (TextView) view
						.findViewById(R.id.group_view_header_work_hours);
				TextView moneyEarned = (TextView) view.findViewById(R.id.group_view_header_money_earned);

				criteria.setText(getSections()[getSectionForPosition(position)]);

				List<EntriesGroupViewRow> currentSection = groups
						.get(getSections()[getSectionForPosition(position)]);
				if (currentSection.size() > 1) {
					int hoursWorked = Utils.calculateHoursWorked(currentSection);
					workHours.setText(String.valueOf(hoursWorked));
					
					String moneyEarnedString = Utils.calculateMoneyEarned(currentSection);
					moneyEarned.setText(moneyEarnedString);
			}

			} else {
				view.findViewById(R.id.header).setVisibility(View.GONE);
			}
		}

		@Override
		public View getAmazingView(int position, View convertView,
				ViewGroup parent) {
			View res = convertView;
			if (res == null) {
				res = getActivity().getLayoutInflater().inflate(
						R.layout.entries_fragment_row, null);
			}

			TextView jobName = (TextView) res
					.findViewById(R.id.entries_row_job_name);
			TextView workHours = (TextView) res
					.findViewById(R.id.enties_row_work_hours);
			TextView moneyEarned = (TextView) res
					.findViewById(R.id.enties_row_money_earned);
			EntriesGroupViewRow entriesGroupViewRow = getItem(position);

			jobName.setText(entriesGroupViewRow.getJobName());
			workHours
					.setText(String.valueOf(entriesGroupViewRow.getWorkHours())
							+ "h");
			Integer moneyPerHour = entriesGroupViewRow.getMoneyEarned();

			Currency currency = Currency.getInstance(Locale.getDefault());

			BigDecimal money = (new BigDecimal(moneyPerHour))
					.divide(new BigDecimal(100));
			moneyEarned.setText(currency.getSymbol()
					+ new DecimalFormat("#,##0.00").format(money));

			return res;
		}

		@Override
		public void configurePinnedHeader(View header, int position, int alpha) {
			LinearLayout lSectionHeader = (LinearLayout) header;
			TextView criteria = (TextView) lSectionHeader
					.findViewById(R.id.group_view_header_group_criteria);
			criteria.setText(getSections()[getSectionForPosition(position)]);
			lSectionHeader.setBackgroundColor(alpha << 24 | (0xbbffbb));
			criteria.setTextColor(alpha << 24 | (0x000000));
		}

		@Override
		public int getPositionForSection(int section) {
			if (section < 0)
				section = 0;
			if (section >= payPariodsGroups.size())
				section = payPariodsGroups.size() - 1;
			int c = 0;
			for (int i = 0; i < payPariodsGroups.size(); i++) {
				if (section == i) {
					return c;
				}
				c += payPariodsGroups.get(i).size();
			}
			return 0;
		}

		@Override
		public int getSectionForPosition(int position) {
			int c = 0;
			for (int i = 0; i < payPariodsGroups.size(); i++) {
				if (position >= c
						&& position < c + payPariodsGroups.get(i).size()) {
					return i;
				}
				c += payPariodsGroups.get(i).size();
			}
			return -1;
		}

		@Override
		public String[] getSections() {
			//
			// String[] res = new String[groups.size()];
			// for (int i = 0; i < groups.size(); i++) {
			// res[i] = groups.get(i).first;
			// }
			// return res;
			return sectionHeaders.toArray(new String[sectionHeaders.size()]);
		}
	}
}
