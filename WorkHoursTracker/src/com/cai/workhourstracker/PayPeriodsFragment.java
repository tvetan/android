package com.cai.workhourstracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.cai.workhourstracker.EntriesFragment.SectionComposerAdapter;
import com.cai.workhourstracker.Views.GroupListView;
import com.cai.workhourstracker.adapters.GroupViewAdapter;
import com.cai.workhourstracker.helper.DatabaseHelper;
import com.cai.workhourstracker.model.Entry;
import com.cai.workhourstracker.model.PayPeriod;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class PayPeriodsFragment extends Fragment implements
		AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

	private DatabaseHelper db;
	private GroupListView listView;
	private SectionComposerAdapter adapter;
	private List<PayPeriod> payPeriods;
	private Map<String, List<PayPeriod>> all;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_pay_periods,
				container, false);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		listView = (GroupListView) getActivity().findViewById(
				R.id.lsComposerPayPeriods);

		listView.setPinnedHeaderView(LayoutInflater.from(getActivity())
				.inflate(R.layout.item_composer_header, listView, false));
		listView.setOnItemClickListener(this);

		db = new DatabaseHelper(getActivity());
		payPeriods = db.getAllPayPeriods();
		db.close();

		sortByDate();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Spinner spinner = (Spinner) getActivity().findViewById(
				R.id.jobs_filter_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.pay_periods_spinner_array,
				android.R.layout.simple_spinner_item);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		inflater.inflate(R.menu.fragment_pay_periods_menu, menu);

		super.onCreateOptionsMenu(menu, inflater);
	}

	private void sortByDate() {
		all = new HashMap<String, List<PayPeriod>>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM, yyyy",
				Locale.getDefault());
		for (int i = 0; i < payPeriods.size(); i++) {
			String currentDay = changeDateStringFormat(formatter, payPeriods
					.get(i).getDate());
			if (all.containsKey(currentDay)) {
				List<PayPeriod> currentEntries = all.get(currentDay);
				currentEntries.add(payPeriods.get(i));
				all.put(currentDay, currentEntries);
			} else {
				List<PayPeriod> currentEntries = new ArrayList<PayPeriod>();
				currentEntries.add(payPeriods.get(i));
				all.put(currentDay, currentEntries);
			}
		}

		List<String> list = new ArrayList<String>(all.keySet());
		listView.setAdapter(adapter = new SectionComposerAdapter(list, all));
	}

	private void sortByJob() {
		all = new HashMap<String, List<PayPeriod>>();
		for (int i = 0; i < payPeriods.size(); i++) {
			String currentJob = db.getJobById(payPeriods.get(i).getJobId())
					.getName();
			if (all.containsKey(currentJob)) {
				List<PayPeriod> currentEntries = all.get(currentJob);
				currentEntries.add(payPeriods.get(i));
				all.put(currentJob, currentEntries);
			} else {
				List<PayPeriod> currentEntries = new ArrayList<PayPeriod>();
				currentEntries.add(payPeriods.get(i));
				all.put(currentJob, currentEntries);
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
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		if (position == 0) {
			sortByDate();
		} else {
			sortByJob();
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

	public class SectionComposerAdapter extends GroupViewAdapter {
		Map<String, List<PayPeriod>> all;
		List<String> sectionHeaders;
		List<List<PayPeriod>> payPariodsGroups;

		public SectionComposerAdapter(List<String> sectionHeaders,
				Map<String, List<PayPeriod>> all) {
			this.sectionHeaders = sectionHeaders;
			this.all = all;
			this.payPariodsGroups = new ArrayList<List<PayPeriod>>(all.values());
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
		public PayPeriod getItem(int position) {
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

			PayPeriod payPeriod = getItem(position);
			jobName.setText(payPeriod.getDate());
			jobId.setText(String.valueOf(payPeriod.getId()));

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
			return sectionHeaders.toArray(new String[sectionHeaders.size()]);
		}

	}
}
