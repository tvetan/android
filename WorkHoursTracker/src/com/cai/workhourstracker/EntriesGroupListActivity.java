package com.cai.workhourstracker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cai.workhourstracker.helper.DatabaseHelper;
import com.cai.workhourstracker.model.Entry;
import com.cai.workhourstracker.model.Job;

import Utils.DateCalculateUtils;
import Utils.DateFormatUtils;
import Utils.ToastUtils;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class EntriesGroupListActivity extends BaseEntriesListActivity implements
		AdapterView.OnItemClickListener {

	private static final int REQUEST_CODE = 10;
	private Job job;
	private DatabaseHelper db;
	private List<Entry> entries;

	private Integer weekNumber;
	private Date weekStartDate;
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
		// Show the Up button in the action bar.
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

		entries = db.getAllEntriesByJobId(job.getId());

		if (extras.getString("weekStartDate") != null) {
			weekStartDate = DateFormatUtils.fromDateMonthYear(extras.getString("weekStartDate"));
			weekNumber = DateCalculateUtils.getWeekNumber(weekStartDate);
			entries = filterEntriesByWeekNumber(entries, weekNumber);
		}

		entriesList.setHeaderDividersEnabled(true);
		entriesList.setOnItemClickListener(this);
		setUpEntriesListView(entries, entriesList, listViewHeader);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		LinearLayout row = (LinearLayout) view;
		TextView entryId = (TextView) row.findViewById(R.id.entry_id);
		String id = (String) entryId.getText();
		Intent intent = new Intent(this, SingleEntryActivity.class);
		intent.putExtra("id", id);
		startActivityForResult(intent, REQUEST_CODE);
	}

	private List<Entry> filterEntriesByWeekNumber(List<Entry> entries, Integer weekNumber) {

		List<Entry> filteredEntries = new ArrayList<Entry>();
		for (Entry entry : entries) {
			Date currentDate = DateFormatUtils.fromDatabaseFormatToDate(entry.getStartClock());
			Integer currentWeekNumber = DateCalculateUtils.getWeekNumber(currentDate);
			if (currentWeekNumber == weekNumber) {
				filteredEntries.add(entry);
			}
		}

		return filteredEntries;
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.entries_group_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
