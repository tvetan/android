package com.cai.workhourstracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
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
import com.cai.workhourstracker.model.Entry;
import com.cai.workhourstracker.model.EntryStartClockComparator;
import com.cai.workhourstracker.model.Job;
import com.cai.workhourstracker.model.JobNameComparator;
import com.cai.workhourstracker.model.JobNameReverseComparator;

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
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		listView = (GroupListView) getActivity().findViewById(
				R.id.lsComposerEntries);
		listView.setPinnedHeaderView(LayoutInflater.from(getActivity())
				.inflate(R.layout.item_composer_header, listView, false));
		listView.setOnItemClickListener(this);
		db = new DatabaseHelper(getActivity());
		entries = db.getAllEntries();
		db.close();

		sortByMonth();
	}

	private void sortByWeek() {
		// Calendar calendar = Calendar.getInstance();
		// int week = calendar.get(Calendar.WEEK_OF_YEAR);
		// calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		// calendar.set(Calendar.WEEK_OF_YEAR, week);
		// Date date = calendar.getTime();
		// Log.d("date", date.toString());

	}

	private void sortByDate() {
		all = new HashMap<String, List<Entry>>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM, yyyy",
				Locale.getDefault());
		for (int i = 0; i < entries.size(); i++) {
			String currentDay = changeDateStringFormat(formatter, entries
					.get(i).getStartClock());
			if (all.containsKey(currentDay)) {
				List<Entry> currentEntries = all.get(currentDay);
				currentEntries.add(entries.get(i));
				all.put(currentDay, currentEntries);
			} else {
				List<Entry> currentEntries = new ArrayList<Entry>();
				currentEntries.add(entries.get(i));
				all.put(currentDay, currentEntries);
			}
		}

		List<String> list = new ArrayList<String>(all.keySet());
		listView.setAdapter(adapter = new SectionComposerAdapter(list, all));
	}

	private void sortByMonth() {
		all = new HashMap<String, List<Entry>>();
		SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy",
				Locale.getDefault());
		for (int i = 0; i < entries.size(); i++) {
			String currentDay = changeDateStringFormat(formatter, entries
					.get(i).getStartClock());
			if (all.containsKey(currentDay)) {
				List<Entry> currentEntries = all.get(currentDay);
				currentEntries.add(entries.get(i));
				all.put(currentDay, currentEntries);
			} else {
				List<Entry> currentEntries = new ArrayList<Entry>();
				currentEntries.add(entries.get(i));
				all.put(currentDay, currentEntries);
			}
		}
		List<String> list = new ArrayList<String>(all.keySet());
		listView.setAdapter(adapter = new SectionComposerAdapter(list, all));
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
			// TODO Auto-generated catch block
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
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.entries_spinner_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		inflater.inflate(R.menu.fragment_entries_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	public class SectionComposerAdapter extends GroupViewAdapter {
		Map<String, List<Entry>> all;
		// String[] sectionHeaders;
		List<String> sectionHeaders;

		public SectionComposerAdapter(List<String> sectionHeaders,
				Map<String, List<Entry>> all) {
			this.sectionHeaders = sectionHeaders;
			this.all = all;
		}

		@Override
		public int getCount() {
			int res = 0;

			List<List<Entry>> v = new ArrayList<List<Entry>>(all.values());

			for (int i = 0; i < v.size(); i++) {
				res += v.get(i).size();
			}
			return res;
		}

		@Override
		public Entry getItem(int position) {
			int c = 0;
			List<List<Entry>> v = new ArrayList<List<Entry>>(all.values());
			for (int i = 0; i < v.size(); i++) {
				if (position >= c && position < c + v.get(i).size()) {
					return v.get(i).get(position - c);
				}
				c += v.get(i).size();
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
				TextView lSectionTitle = (TextView) view
						.findViewById(R.id.header);
				lSectionTitle
						.setText(getSections()[getSectionForPosition(position)]);
			} else {
				view.findViewById(R.id.header).setVisibility(View.GONE);
			}
		}

		@Override
		public View getAmazingView(int position, View convertView,
				ViewGroup parent) {
			View res = convertView;
			if (res == null)
				res = getActivity().getLayoutInflater().inflate(
						R.layout.item_composer, null);

			TextView jobName = (TextView) res.findViewById(R.id.jobName);
			TextView jobId = (TextView) res.findViewById(R.id.jobId);

			Entry job = getItem(position);
			jobName.setText(job.getStartClock());
			jobId.setText(String.valueOf(job.getId()));

			return res;
		}

		@Override
		public void configurePinnedHeader(View header, int position, int alpha) {
			TextView lSectionHeader = (TextView) header;
			lSectionHeader
					.setText(getSections()[getSectionForPosition(position)]);
			lSectionHeader.setBackgroundColor(alpha << 24 | (0xbbffbb));
			lSectionHeader.setTextColor(alpha << 24 | (0x000000));
		}

		@Override
		public int getPositionForSection(int section) {
			List<List<Entry>> v = new ArrayList<List<Entry>>(all.values());
			if (section < 0)
				section = 0;
			if (section >= v.size())
				section = v.size() - 1;
			int c = 0;
			for (int i = 0; i < v.size(); i++) {
				if (section == i) {
					return c;
				}
				c += v.get(i).size();
			}
			return 0;
		}

		@Override
		public int getSectionForPosition(int position) {
			List<List<Entry>> v = new ArrayList<List<Entry>>(all.values());
			int c = 0;
			for (int i = 0; i < v.size(); i++) {
				if (position >= c && position < c + v.get(i).size()) {
					return i;
				}
				c += v.get(i).size();
			}
			return -1;
		}

		@Override
		public String[] getSections() {
			return sectionHeaders.toArray(new String[sectionHeaders.size()]);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onItemSelected(AdapterView<?> arg0, View view, int position,
			long arg3) {
		if (position == 0) {
			sortByDate();
		} else if (position == 1) {
			new SortDates().execute(entries);
		} else {
			sortByMonth();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	private class SortDates extends
			AsyncTask<List<Entry>, Void, HashMap<String, List<Entry>>> {

		private Date getDateFromString(String dateAsString) {
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
			Date date;
			try {
				date = formatter.parse(dateAsString);
				return date;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
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
		protected HashMap<String, List<Entry>> doInBackground(
				List<Entry>... params) {

			List<Entry> entryList = params[0];

			// Collections.sort(first, Collections.reverseOrder());
			// Collections.sort(first,
			// Collections.reverseOrder(new EntryStartClockComparator()));

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

			return byWeek;

		}

		// week of 17 November
		// week of 22 November

		private String fromDateToWeekString(Date date){
			SimpleDateFormat formatter = new SimpleDateFormat(
					"dd MMMM", Locale.getDefault());
			
			return "week of " + formatter.format(date);
		}
		
		private List<String> fromWeekNumbersToWeekStrings(List<String> weekNumbers) {

			List<String> result = new ArrayList<String>();
			Calendar calendar = Calendar.getInstance();
			// int week = calendar.get(Calendar.WEEK_OF_YEAR);
			 calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			
			Date date = calendar.getTime();
			List<String> weeks = new ArrayList<String>();

			for (String number : weekNumbers) {
				
				calendar.set(Calendar.WEEK_OF_YEAR,Integer.valueOf(number));
				Date currentWeek = calendar.getTime();
				
				result.add(fromDateToWeekString(currentWeek));
			}

			return result;
		}

		protected void onPostExecute(HashMap<String, List<Entry>> byWeek) {
			List<String> headers = new ArrayList<String>(byWeek.keySet());
			List<String> headerWeekStrings = fromWeekNumbersToWeekStrings(headers);
			listView.setAdapter(adapter = new SectionComposerAdapter(headerWeekStrings,
					byWeek));
		}
	}
}
