package com.cai.workhourstracker.fragments;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import com.cai.workhourstracker.EntriesGroupListActivity;
import com.cai.workhourstracker.ExportSelectionActivity;
import com.cai.workhourstracker.R;
import com.cai.workhourstracker.R.array;
import com.cai.workhourstracker.R.id;
import com.cai.workhourstracker.R.layout;
import com.cai.workhourstracker.R.menu;
import com.cai.workhourstracker.Views.GroupListView;
import com.cai.workhourstracker.adapters.GroupViewAdapter;
import com.cai.workhourstracker.helper.DatabaseHelper;
import com.cai.workhourstracker.model.EntriesGroupViewRow;
import com.cai.workhourstracker.model.Entry;

import Utils.MoneyFormatUtils;
import Utils.ToastUtils;
import Utils.Utils;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

public class EntriesFragment extends TabSwipeBaseFragment implements
		AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

	private DatabaseHelper db;
	private GroupListView listView;
	private SectionComposerAdapter adapter;
	private List<Entry> entries;
	private LinkedHashMap<String, List<Entry>> all;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		Log.d("test", "craete fragment entries");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_entries, container, false);

		rootView.findViewById(R.id.actionbar_done).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), ExportSelectionActivity.class));
			}
		});
		rootView.findViewById(R.id.actionbar_cancel).setOnClickListener(new View.OnClickListener() {
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
		listView = (GroupListView) getActivity().findViewById(R.id.lsComposerEntries);
		listView.setPinnedHeaderView(LayoutInflater.from(getActivity()).inflate(
				R.layout.group_view_header, listView, false));
		listView.setOnItemClickListener(this);
		db = new DatabaseHelper(getActivity());
		entries = db.getAllEntries();

		// Inflate group listview header
		LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		View header = inflater.inflate(R.layout.start_clock_list_header, null);

		// new GroupByDaysAsyncTask().execute(entries);
		LinkedHashMap<String, List<Entry>> groupedByDate = groupByDate(entries);
		LinkedHashMap<String, List<EntriesGroupViewRow>> entriesGroupViewRow = Utils
				.groupEachEntryGroup(groupedByDate, getActivity());

		fillHeaderData(header, entriesGroupViewRow.values());
		listView.addHeaderView(header);
		header.setOnClickListener(null);
		List<String> headers = new ArrayList<String>(groupedByDate.keySet());
		listView.setAdapter(adapter = new SectionComposerAdapter(headers, entriesGroupViewRow));

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	private LinkedHashMap<String, List<Entry>> groupByDate(List<Entry> entries) {
		LinkedHashMap<String, List<Entry>> groupDate = new LinkedHashMap<String, List<Entry>>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault());

		for (int i = 0; i < entries.size(); i++) {
			String currentDay = changeDateStringFormat(formatter, entries.get(i).getStartClock());
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

	private LinkedHashMap<String, List<Entry>> groupByMonth(List<Entry> entries) {
		LinkedHashMap<String, List<Entry>> groupMonth = new LinkedHashMap<String, List<Entry>>();
		SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
		for (int i = 0; i < entries.size(); i++) {
			String currentDay = changeDateStringFormat(formatter, entries.get(i).getStartClock());
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

	public String changeDateStringFormat(SimpleDateFormat format, String dateAsString) {
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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setCustomView(R.layout.jobs_filter);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
		Spinner spinner = (Spinner) getActivity().findViewById(R.id.jobs_filter_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.entries_spinner_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(this);
		spinner.setSelection(spinnerSelectedPosition);
		inflater.inflate(R.menu.fragment_entries_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.d("on save state called entries", " state called entrie");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onItemSelected(AdapterView<?> arg0, View view, int position, long arg3) {
		if (position == 0) {
			// new GroupByDaysAsyncTask().execute(entries);
			spinnerSelectedPosition = 0;
			LinkedHashMap<String, List<Entry>> groupedByDate = groupByDate(entries);
			LinkedHashMap<String, List<EntriesGroupViewRow>> result = Utils.groupEachEntryGroup(
					groupedByDate, getActivity());
			List<String> headers = new ArrayList<String>(groupedByDate.keySet());
			listView.setAdapter(adapter = new SectionComposerAdapter(headers, result));
		} else if (position == 1) {
			spinnerSelectedPosition = 1;
			new GroupByWeeksAsyncTask().execute(entries);
		} else {
			spinnerSelectedPosition = 2;
			all = groupByMonth(entries);
			listView.setAdapter(adapter = new SectionComposerAdapter(Utils.getHeaders(all), Utils
					.groupEachEntryGroup(all, getActivity())));
			// new GroupByMonthAsyncTask().execute(entries);
		}
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		db.closeDB();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		String[] headers = adapter.getSections();
		int headerPosition = adapter.getSectionForPosition(position - 1);
		String headerName = headers[headerPosition];
		TextView criteriaNameTextView = (TextView) view
				.findViewById(R.id.group_view_group_criteria);
		String criteriaName = criteriaNameTextView.getText().toString();

		Intent intent = new Intent(getActivity(), EntriesGroupListActivity.class);

		intent.putExtra("jobName", criteriaName);
		intent.putExtra("dateFilter", headerName);

		if (spinnerSelectedPosition == 1) {
			String weekStartDate = headerName.replace("week of ", "");
			intent.putExtra("weekStartDate", weekStartDate);
		}

		startActivity(intent);

	}

	private Date getDateFromString(String dateAsString) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale
				.getDefault());
		Date date;
		try {
			date = formatter.parse(dateAsString);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	public Integer getWeekNumber(Entry entry) {
		Date date = getDateFromString(entry.getStartClock());
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTime(date);
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	private class GroupByWeeksAsyncTask extends
			AsyncTask<List<Entry>, Void, LinkedHashMap<String, List<EntriesGroupViewRow>>> {

		@Override
		protected LinkedHashMap<String, List<EntriesGroupViewRow>> doInBackground(
				List<Entry>... params) {

			List<Entry> entryList = params[0];
			LinkedHashMap<String, List<Entry>> byWeek = new LinkedHashMap<String, List<Entry>>();

			for (int i = 0; i < entryList.size(); i++) {

				String weekNumber = String.valueOf(getWeekNumber(entryList.get(i)));

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

			all = new LinkedHashMap<String, List<Entry>>(byWeek);

			LinkedHashMap<String, List<EntriesGroupViewRow>> result = Utils.groupEachEntryGroup(
					byWeek, getActivity());

			return result;
		}

		private String fromDateToWeekString(Date date) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
			return "week of " + formatter.format(date);
		}

		private List<String> fromWeekNumbersToWeekStrings(List<String> weekNumbers) {

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

		public LinkedHashMap<String, List<EntriesGroupViewRow>> convertHeaders(
				List<String> headerWeekStrings,
				LinkedHashMap<String, List<EntriesGroupViewRow>> byWeek) {
			LinkedHashMap<String, List<EntriesGroupViewRow>> result = new LinkedHashMap<String, List<EntriesGroupViewRow>>();
			int index = 0;
			for (String key : byWeek.keySet()) {

				result.put(headerWeekStrings.get(index), byWeek.get(key));
				index++;
			}

			return result;
		}

		protected void onPostExecute(LinkedHashMap<String, List<EntriesGroupViewRow>> byWeek) {
			List<String> headers = new ArrayList<String>(byWeek.keySet());
			List<String> headerWeekStrings = fromWeekNumbersToWeekStrings(headers);

			LinkedHashMap<String, List<EntriesGroupViewRow>> convertedHeaders = convertHeaders(
					headerWeekStrings, byWeek);

			listView.setAdapter(adapter = new SectionComposerAdapter(headerWeekStrings,
					convertedHeaders));
		}
	}

	private class GroupByDaysAsyncTask extends
			AsyncTask<List<Entry>, Void, LinkedHashMap<String, List<EntriesGroupViewRow>>> {

		@Override
		protected LinkedHashMap<String, List<EntriesGroupViewRow>> doInBackground(
				List<Entry>... params) {
			LinkedHashMap<String, List<Entry>> groupedByDate = groupByDate(entries);
			LinkedHashMap<String, List<EntriesGroupViewRow>> result = Utils.groupEachEntryGroup(
					groupedByDate, getActivity());

			return result;
		}

		protected void onPostExecute(LinkedHashMap<String, List<EntriesGroupViewRow>> groupByDate) {
			List<String> headers = new ArrayList<String>(groupByDate.keySet());
			listView.setAdapter(adapter = new SectionComposerAdapter(headers, groupByDate));
		}
	}

	private class GroupByMonthAsyncTask extends
			AsyncTask<List<Entry>, Void, LinkedHashMap<String, List<EntriesGroupViewRow>>> {

		@Override
		protected LinkedHashMap<String, List<EntriesGroupViewRow>> doInBackground(
				List<Entry>... params) {
			LinkedHashMap<String, List<Entry>> groupedByMonth = groupByMonth(entries);
			LinkedHashMap<String, List<EntriesGroupViewRow>> result = Utils.groupEachEntryGroup(
					groupedByMonth, getActivity());

			return result;
		}

		protected void onPostExecute(LinkedHashMap<String, List<EntriesGroupViewRow>> groupByMonth) {
			List<String> headers = new ArrayList<String>(groupByMonth.keySet());
			listView.setAdapter(adapter = new SectionComposerAdapter(headers, groupByMonth));
		}
	}

	public class SectionComposerAdapter extends GroupViewAdapter {
		LinkedHashMap<String, List<EntriesGroupViewRow>> groups;
		List<String> sectionHeaders;
		List<List<EntriesGroupViewRow>> payPariodsGroups;

		public SectionComposerAdapter(List<String> sectionHeaders,
				LinkedHashMap<String, List<EntriesGroupViewRow>> groups) {
			this.sectionHeaders = sectionHeaders;
			this.groups = groups;
			this.payPariodsGroups = new ArrayList<List<EntriesGroupViewRow>>(groups.values());
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
				if (position >= c && position < c + payPariodsGroups.get(i).size()) {
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
		protected void bindSectionHeader(View view, int position, boolean displaySectionHeader) {
			if (displaySectionHeader) {
				view.findViewById(R.id.header).setVisibility(View.VISIBLE);
				TextView criteria = (TextView) view
						.findViewById(R.id.group_view_header_group_criteria);
				TextView workHours = (TextView) view
						.findViewById(R.id.group_view_header_work_hours);
				TextView moneyEarned = (TextView) view
						.findViewById(R.id.group_view_header_money_earned);

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
		public View getAmazingView(int position, View convertView, ViewGroup parent) {
			View res = convertView;
			if (res == null) {
				res = getActivity().getLayoutInflater()
						.inflate(R.layout.entries_fragment_row, null);
			}

			TextView jobName = (TextView) res.findViewById(R.id.group_view_group_criteria);
			TextView workHours = (TextView) res.findViewById(R.id.enties_row_work_hours);
			TextView moneyEarned = (TextView) res.findViewById(R.id.enties_row_money_earned);
			EntriesGroupViewRow entriesGroupViewRow = getItem(position);

			jobName.setText(entriesGroupViewRow.getGroupCriteria());
			workHours.setText(String.valueOf(entriesGroupViewRow.getWorkHours()) + "h");
			Integer moneyPerHour = entriesGroupViewRow.getMoneyEarned();

			Currency currency = Currency.getInstance(Locale.getDefault());

			BigDecimal money = (new BigDecimal(moneyPerHour)).divide(new BigDecimal(100));
			moneyEarned.setText(currency.getSymbol() + new DecimalFormat("#,##0.00").format(money));

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
				if (position >= c && position < c + payPariodsGroups.get(i).size()) {
					return i;
				}
				c += payPariodsGroups.get(i).size();
			}
			return -1;
		}

		@Override
		public String[] getSections() {
			return sectionHeaders.toArray(new String[sectionHeaders.size()]);
		}
	}
}
