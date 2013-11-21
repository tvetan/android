package com.cai.workhourstracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.cai.workhourstracker.helper.DatabaseHelper;
import com.cai.workhourstracker.model.Entry;
import com.cai.workhourstracker.model.Job;
import com.cai.workhourstracker.model.PayPeriod;

import Utils.DateFormatUtils;
import Utils.Utils;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class PayPeriodsGroupListActivity extends BaseEntriesListActivity implements
		AdapterView.OnItemClickListener {

	private Job job;
	private DatabaseHelper db;
	private List<Entry> entries;
	private List<PayPeriod> payPeriods;
	private TextView entriesGroupJobNameTextView;
	private TextView entriesGroupFilterDateTextView;
	private ListView entriesList;
	private ViewGroup listviewHeader;
	private LayoutInflater layoutInflater;
	private ViewGroup listViewHeader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entries_group_list);
		setupActionBar();

		entriesGroupJobNameTextView = (TextView) findViewById(R.id.entries_group_job_name);
		entriesGroupFilterDateTextView = (TextView) findViewById(R.id.entries_group_filter_date);
		layoutInflater = getLayoutInflater();
		entriesList = (ListView) findViewById(R.id.entries_group_list_view);
		listViewHeader = (ViewGroup) layoutInflater.inflate(R.layout.start_clock_list_header,
				entriesList, false);

		Bundle extras = getIntent().getExtras();
		String jobName = extras.getString("jobName");
		String filterDate = extras.getString("dateFilter");

		entriesGroupFilterDateTextView.setText(filterDate);
		entriesGroupJobNameTextView.setText(jobName);

		db = new DatabaseHelper(this);
		job = db.getJobByName(jobName);

		payPeriods = db.getAllPayPeriodsByJobId(job.getId());
		payPeriods = filterPayPeriodsByDate(payPeriods, filterDate);
		entries = getAllEntriesFromPayPeriods(payPeriods);

		entriesList.setHeaderDividersEnabled(true);
		entriesList.setOnItemClickListener(this);
		setUpEntriesListView(entries, entriesList, listViewHeader);

	}

	private List<Entry> getAllEntriesFromPayPeriods(List<PayPeriod> payPeriods) {
		List<Entry> entries = new ArrayList<Entry>();

		for (PayPeriod payPeriod : payPeriods) {
			entries.addAll(db.getAllEntriesByPayPeriodId(payPeriod.getId()));
		}

		return entries;
	}

	private List<PayPeriod> filterPayPeriodsByDate(List<PayPeriod> payPeriods, String filterDate) {

		List<PayPeriod> filteredPayPeriods = new ArrayList<PayPeriod>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
		for (PayPeriod payPeriod : payPeriods) {
			Date payPeriodDate = DateFormatUtils.fromDatabaseFormatToDate(payPeriod.getDate());
			String payPeriodDateString = formatter.format(payPeriodDate);
			if (filterDate.compareTo(payPeriodDateString) == 0) {
				filteredPayPeriods.add(payPeriod);
				
			}
		}

		return filteredPayPeriods;
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pay_periods_group_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub	
	}

}
