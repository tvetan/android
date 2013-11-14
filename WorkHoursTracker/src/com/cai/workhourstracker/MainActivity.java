package com.cai.workhourstracker;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.cai.workhourstracker.adapters.TabsPagerAdapter;
import com.cai.workhourstracker.helper.DatabaseHelper;
import com.cai.workhourstracker.model.Entry;
import com.cai.workhourstracker.model.Job;
import com.cai.workhourstracker.model.Tag;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "JOBS", "ENTRIES", "PAY PERIODS" };

	DatabaseHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();

		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		// actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		db = new DatabaseHelper(getApplicationContext());

		//addJobs();
		//addEntries();
		db.closeDB();
	}

	public void addJobs() {

		Tag tag1 = new Tag("Shopping");
		long tag1_id = db.createTag(tag1);
		Job todo1 = new Job("Programmer", false, 123);
		db.createJob(todo1, new long[] { tag1_id });

		todo1 = new Job("Writer", false, 1123);
		db.createJob(todo1, new long[] { tag1_id });

		todo1 = new Job("Tennis Player", true, 1123);
		db.createJob(todo1, new long[] { tag1_id });

		todo1 = new Job("Worker", false, 11);
		db.createJob(todo1, new long[] { tag1_id });

		todo1 = new Job("QA", false, 11);
		db.createJob(todo1, new long[] { tag1_id });
		todo1 = new Job("QA", false, 11);
		db.createJob(todo1, new long[] { tag1_id });
		todo1 = new Job("QA", false, 11);
		db.createJob(todo1, new long[] { tag1_id });

		todo1 = new Job("Support", true, 11);
		db.createJob(todo1, new long[] { tag1_id });
	}

	public void addEntries() {
		List<Job> a = db.getAllJobs();

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.HOUR_OF_DAY, 1);
		String creationDate = dateFormat.format(cal.getTime());
		cal.add(Calendar.HOUR_OF_DAY, 1);
		String stopDate = dateFormat.format(cal.getTime());
		Integer money = 356;
		Entry entry = new Entry("some comment", creationDate, stopDate, a
				.get(2).getId(), money, 33);

		db.createEntry(entry);

		cal.add(Calendar.DATE, 1);
		creationDate = dateFormat.format(cal.getTime());
		cal.add(Calendar.MONTH, 1);
		stopDate = dateFormat.format(cal.getTime());
		entry = new Entry("com", creationDate, stopDate, a.get(3).getId(),
				money, 177);

		db.createEntry(entry);

		cal.add(Calendar.DAY_OF_WEEK, 1);
		creationDate = dateFormat.format(cal.getTime());
		cal.add(Calendar.WEEK_OF_YEAR, 3);
		stopDate = dateFormat.format(cal.getTime());
		entry = new Entry("com11", creationDate, stopDate, a.get(4).getId(),
				444, 11);

		db.createEntry(entry);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {

		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
	}

	public void toStartClock(View view) {
		// Toast.makeText(this, "adsfadsf", Toast.LENGTH_SHORT).show();
		startActivity(new Intent(this, StartClockActivity.class));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
		case R.id.action_settings:
			startActivity(new Intent(this, EditJobActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void showTimePickerDialog(View v) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getSupportFragmentManager(), "timePicker");
	}

	public void showTimePickerWithCancelDialog(View v) {
		DialogFragment newFragment = new TimerPickerFragmentWithCancel();
		newFragment.show(getSupportFragmentManager(), "timePickerwithCancel");
	}
}
