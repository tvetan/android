package com.cai.workhourstracker;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import Utils.DateCalculateUtils;
import Utils.DateFormatUtils;
import Utils.MoneyFormatUtils;
import Utils.ToastUtils;
import Utils.Utils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.cai.workhourstracker.dialogs.DeleteEntryDialogFragment;
import com.cai.workhourstracker.helper.DatabaseHelper;
import com.cai.workhourstracker.helper.MoneyCalculateUtils;
import com.cai.workhourstracker.model.Entry;
import com.cai.workhourstracker.model.Job;

public class SingleEntryActivity extends FragmentActivity implements
		DeleteEntryDialogFragment.NoticeDialogListener {

	private DatabaseHelper db;
	private TextView jobNameTextView;
	private Job job;
	private Entry entry;
	private int entryId;
	private TextView entryDate;
	private TextView startWorkTime;
	private TextView endWorkTime;
	private TextView workingHours;
	private TextView comment;
	private TextView baseRateTextView;
	private TextView moneyEarnedTextView;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		db.closeDB();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_entry);
		
		jobNameTextView = (TextView) findViewById(R.id.single_entry_job_name);
		entryDate = (TextView) findViewById(R.id.single_entry_date);
		startWorkTime = (TextView) findViewById(R.id.single_entry_start_clock);
		endWorkTime = (TextView) findViewById(R.id.single_entry_end_clock);
		workingHours = (TextView) findViewById(R.id.single_entry_work_hours);
		comment = (TextView) findViewById(R.id.single_entry_comment);
		baseRateTextView = (TextView) findViewById(R.id.single_entry_base_rate);
		moneyEarnedTextView = (TextView) findViewById(R.id.single_entry_money_earned);
		
		setupActionBar();
		setUpDataBaseValues();
		setUpData();
	}
	
	private void setUpDataBaseValues(){
		db = new DatabaseHelper(this);
		Bundle extras = getIntent().getExtras();
		String idString = extras.get("id").toString();
		entryId = Integer.valueOf(idString);

		entry = db.getEntryById(entryId);
		int jobId = entry.getJobId();
		job = db.getJobById(jobId);
	}

	private void setUpData() {
		baseRateTextView.setText(Utils.convertMoneyToString(entry.getBaseRate()) + "/hour");
		jobNameTextView.setText(job.getName() + " Entry");
		comment.setText(entry.getComment());
		startWorkTime.setText(DateFormatUtils.formatToHourAndMinutes(entry.getStartClock()));
		endWorkTime.setText(DateFormatUtils.formatToHourAndMinutes(entry.getStopClock()));

		setUpWorkHoursAndMoneyEarned();
	}

	private void setUpWorkHoursAndMoneyEarned() {
		Date startClock = DateFormatUtils.fromDatabaseFormatToDate(entry.getStartClock());
		Date endClock = DateFormatUtils.fromDatabaseFormatToDate(entry.getStopClock());
		double workHours = DateCalculateUtils.differenceBetweenTwoDatesInMinutes(startClock,
				endClock);
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMinimumFractionDigits(2);
		format.setMaximumFractionDigits(2);

		workingHours.setText(format.format(workHours) + "h");
		BigDecimal moneyEarned = MoneyCalculateUtils.moneyEarned(workHours, entry.getBaseRate());

		moneyEarnedTextView.setText(MoneyFormatUtils
				.toLocaleCurrencyFormatFromBigDecimal(moneyEarned));
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.single_entry, menu);
		return true;
	}

	public void goHome() {
		Intent intent = new Intent(this, StartClockActivity.class);
		CharSequence id = String.valueOf(entryId);
		intent.putExtra("id", id);
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			//NavUtils.navigateUpFromSameTask(this);
			finish();
			return true;

		case R.id.delete_entry:
			DeleteEntryDialogFragment dialog = new DeleteEntryDialogFragment();
			dialog.show(getSupportFragmentManager(), "delete entry");
			return true;

		case R.id.edit_entry:
			Intent intent = new Intent(this, EditEntryActivity.class);
			intent.putExtra("id", entryId);
			startActivityForResult(intent, 100);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		db.deleteEntry(entryId);
		db.closeDB();
		finish();
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 100) {
			
			String startClock = data.getExtras().getString("startClock");
			String stopClock = data.getExtras().getString("stopClock");		
			entry.setStartClock(startClock);
			entry.setStopClock(stopClock);
			
			String commentString = data.getExtras().getString("comment");
			String jobName = data.getExtras().getString("jobName");
			Integer baseRate = data.getExtras().getInt("baseRate");

			startWorkTime.setText(Utils.dateToHour(startClock));
			endWorkTime.setText(Utils.dateToHour(stopClock));
			comment.setText(commentString);
			//baseRateTextView.setText(String.valueOf(baseRate));
			jobNameTextView.setText(jobName + " Entry");
			baseRateTextView.setText(Utils.convertMoneyToString(baseRate) + "/hour");
			
			setUpWorkHoursAndMoneyEarned();
		}
	}
}
