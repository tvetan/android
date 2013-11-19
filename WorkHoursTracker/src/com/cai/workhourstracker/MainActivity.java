package com.cai.workhourstracker;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.cai.workhourstracker.adapters.TabsPagerAdapter;
import com.cai.workhourstracker.dialogs.DeleteEntriesByDateDialog;
import com.cai.workhourstracker.dialogs.DeleteEntriestConfirmDialog;
import com.cai.workhourstracker.dialogs.DeleteEntriesByDateDialog.IDeleteEntriesListener;
import com.cai.workhourstracker.dialogs.DeleteEntriestConfirmDialog.IDeleteEntriesConfirm;
import com.cai.workhourstracker.helper.DatabaseHelper;
import com.cai.workhourstracker.model.Entry;
import com.cai.workhourstracker.model.Job;
import com.cai.workhourstracker.model.PayPeriod;
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

public class MainActivity extends FragmentActivity implements ActionBar.TabListener,
		IDeleteEntriesListener, IDeleteEntriesConfirm {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	private String[] tabs = { "JOBS", "ENTRIES", "PAY PERIODS" };
	private DatabaseHelper db;

	@Override
	protected void onDestroy() {
		super.onDestroy();
	
		db.closeDB();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);
		// actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
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
		//addPayPeriods();
		//addEntries();
	}

	private void addJobs() {
		Tag tag1 = new Tag("Shopping");
		long tag1_id = db.createTag(tag1);
		Job job = new Job("Programmer", false, 123);
		job.setDeduction(120);
		job.setTaxPercentage(2000);
		job.setTimePerDate(800);
		db.createJob(job, new long[] { tag1_id });

		job = new Job("Writer", false, 1123);
		job.setDeduction(1120);
		job.setTaxPercentage(3000);
		job.setTimePerDate(400);
		db.createJob(job, new long[] { tag1_id });

		job = new Job("Tennis Player", true, 1123);
		job.setDeduction(1125);
		job.setTaxPercentage(2125);
		job.setTimePerDate(755);
		db.createJob(job, new long[] { tag1_id });

		job = new Job("Worker", false, 1234);
		job.setDeduction(120);
		job.setTaxPercentage(2000);
		job.setTimePerDate(800);
		db.createJob(job, new long[] { tag1_id });

		job = new Job("job", false, 12000);
		job.setDeduction(120);
		job.setTaxPercentage(2000);
		job.setTimePerDate(800);
		db.createJob(job, new long[] { tag1_id });

		job = new Job("QA", false, 11111);
		job.setDeduction(120);
		job.setTaxPercentage(2000);
		job.setTimePerDate(800);
		db.createJob(job, new long[] { tag1_id });

		job = new Job("Support", true, 9888);
		job.setDeduction(2122);
		job.setTaxPercentage(2500);
		job.setTimePerDate(300);
		db.createJob(job, new long[] { tag1_id });
	}

	private void addPayPeriods() {
		List<Job> jobsList = db.getAllJobs();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		Calendar calendar = Calendar.getInstance();
		String date = dateFormat.format(calendar.getTime());

		calendar.add(Calendar.HOUR_OF_DAY, 1);
		PayPeriod payPeriod = new PayPeriod();
		payPeriod.setDate(date);
		payPeriod.setJobId(jobsList.get(0).getId());
		payPeriod.setMoney(12324);
		db.createPayPeriod(payPeriod);

		calendar.add(Calendar.HOUR_OF_DAY, 12);
		date = dateFormat.format(calendar.getTime());
		payPeriod = new PayPeriod();
		payPeriod.setDate(date);
		payPeriod.setJobId(jobsList.get(1).getId());
		payPeriod.setMoney(1234);
		db.createPayPeriod(payPeriod);

		calendar.add(Calendar.WEEK_OF_YEAR, 5);
		date = dateFormat.format(calendar.getTime());
		payPeriod = new PayPeriod();
		payPeriod.setDate(date);
		payPeriod.setJobId(jobsList.get(2).getId());
		payPeriod.setMoney(1234);
		db.createPayPeriod(payPeriod);

		calendar.add(Calendar.WEEK_OF_MONTH, 2);
		date = dateFormat.format(calendar.getTime());
		payPeriod = new PayPeriod();
		payPeriod.setDate(date);
		payPeriod.setJobId(jobsList.get(4).getId());
		payPeriod.setMoney(11);
		db.createPayPeriod(payPeriod);

		calendar.clear();
		calendar.add(Calendar.WEEK_OF_MONTH, 2);
		date = dateFormat.format(calendar.getTime());
		payPeriod = new PayPeriod();
		payPeriod.setDate(date);
		payPeriod.setJobId(jobsList.get(6).getId());
		payPeriod.setMoney(1112222);
		db.createPayPeriod(payPeriod);

		calendar.clear();
		calendar.add(Calendar.WEEK_OF_MONTH, 2);
		date = dateFormat.format(calendar.getTime());
		payPeriod = new PayPeriod();
		payPeriod.setDate(date);
		payPeriod.setJobId(jobsList.get(4).getId());
		payPeriod.setMoney(11111);
		db.createPayPeriod(payPeriod);

	}

	private void addEntries() {
		List<Job> jobList = db.getAllJobs();
		List<PayPeriod> payPeriodList = db.getAllPayPeriods();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.DATE, 1);
		String creationDate = dateFormat.format(cal.getTime());
		cal.add(Calendar.DATE, 2);
		String stopDate = dateFormat.format(cal.getTime());
		Integer money = 356;
		Entry entry = new Entry("some comment", creationDate, stopDate, jobList.get(1).getId(),
				money, 33, 123);
		db.createEntry(entry);

		cal.add(Calendar.DATE, 2);
		creationDate = dateFormat.format(cal.getTime());
		cal.add(Calendar.MONTH, 2);
		stopDate = dateFormat.format(cal.getTime());
		entry = new Entry("com", creationDate, stopDate, jobList.get(1).getId(), money, 177, 453);
		entry.setBaseRate(7700);
		db.createEntry(entry);

		cal.add(Calendar.DAY_OF_WEEK, 2);
		creationDate = dateFormat.format(cal.getTime());
		cal.add(Calendar.WEEK_OF_YEAR, 4);
		stopDate = dateFormat.format(cal.getTime());
		entry = new Entry("with pay period", creationDate, stopDate, jobList.get(4).getId(), 444,
				11, 123);
		entry.setPayPeriodId(payPeriodList.get(3).getId());
		entry.setBaseRate(1234);
		db.createEntry(entry);

		cal.add(Calendar.DAY_OF_WEEK, 3);
		creationDate = dateFormat.format(cal.getTime());
		cal.add(Calendar.WEEK_OF_YEAR, 2);
		stopDate = dateFormat.format(cal.getTime());
		entry = new Entry("without", creationDate, stopDate, jobList.get(4).getId(), 444, 11);
		entry.setBaseRate(1111);
		entry.setPayPeriodId(payPeriodList.get(0).getId());
		db.createEntry(entry);

		cal.add(Calendar.DAY_OF_WEEK, 3);
		creationDate = dateFormat.format(cal.getTime());
		cal.add(Calendar.WEEK_OF_YEAR, 2);
		stopDate = dateFormat.format(cal.getTime());
		entry = new Entry("1111", creationDate, stopDate, jobList.get(4).getId(), 444, 11);
		entry.setBaseRate(11);
		entry.setPayPeriodId(payPeriodList.get(2).getId());
		db.createEntry(entry);

		cal.add(Calendar.DAY_OF_WEEK, 3);
		creationDate = dateFormat.format(cal.getTime());
		cal.add(Calendar.WEEK_OF_YEAR, 2);
		stopDate = dateFormat.format(cal.getTime());
		entry = new Entry("112311", creationDate, stopDate, jobList.get(0).getId(), 44444, 11);
		entry.setBaseRate(11);
		entry.setPayPeriodId(payPeriodList.get(0).getId());
		db.createEntry(entry);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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

	public void showDialog(View view) {
		DeleteEntriesByDateDialog dialog = DeleteEntriesByDateDialog.newInstance();
		dialog.show(getSupportFragmentManager(), "date");
	}

	@Override
	public void onDeleteEntriesListener(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault());

		DeleteEntriestConfirmDialog dialog = DeleteEntriestConfirmDialog.newInstance(formatter
				.format(date));
		dialog.show(getSupportFragmentManager(), "date");
	}

	@Override
	public void onDeleteEntriesConfirmListener(String dateAsString) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault());
		try {
			Date date = formatter.parse(dateAsString);
			db.deleteEntriesOlderThanDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
