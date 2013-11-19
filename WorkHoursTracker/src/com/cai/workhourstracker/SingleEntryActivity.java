package com.cai.workhourstracker;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Locale;

import com.cai.workhourstracker.helper.DatabaseHelper;
import com.cai.workhourstracker.helper.Utils;
import com.cai.workhourstracker.model.Entry;
import com.cai.workhourstracker.model.Job;

import android.os.Bundle;

import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;

public class SingleEntryActivity extends FragmentActivity implements
		DeleteEntryDialogFragment.NoticeDialogListener {

	private DatabaseHelper db;
	private TextView jobNameTextView;
	private Job job;
	private Entry entry;
	private int entryId;
	private TextView entryDate;
	private TextView startHour;
	private TextView endHour;
	private TextView workingHours;
	private TextView comment;
	private TextView baseRateTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_entry);
		setupActionBar();

		db = new DatabaseHelper(this);
		Bundle extras = getIntent().getExtras();
		String id_string = extras.get("id").toString();
		entryId = Integer.valueOf(id_string);

		entry = db.getEntryById(entryId);
		int jobId = entry.getJobId();
		job = db.getJobById(jobId);
		db.closeDB();

		jobNameTextView = (TextView) findViewById(R.id.single_entry_job_name);
		entryDate = (TextView) findViewById(R.id.single_entry_date);
		startHour = (TextView) findViewById(R.id.single_entry_start_clock);
		endHour = (TextView) findViewById(R.id.single_entry_end_clock);
		workingHours = (TextView) findViewById(R.id.single_entry_work_hours);
		comment = (TextView) findViewById(R.id.single_entry_comment);
		baseRateTextView = (TextView) findViewById(R.id.single_entry_base_rate);

		baseRateTextView.setText(Utils.convertMoneyToString(entry.getBaseRate()));
		jobNameTextView.setText(job.getName() + " Entry");
		comment.setText(entry.getComment());

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale
				.getDefault());
		SimpleDateFormat formatterFullDate = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale
				.getDefault());
		SimpleDateFormat formatterHour = new SimpleDateFormat("HH:mm a", Locale.getDefault());

		try {
			Date startClock = dateFormat.parse(entry.getStartClock());
			Date endClock = dateFormat.parse(entry.getStopClock());
			int workHours = (int) ((endClock.getTime() - startClock.getTime()) / (1000 * 60 * 60));
			entryDate.setText(formatterFullDate.format(startClock));
			startHour.setText(formatterHour.format(startClock));
			endHour.setText(formatterHour.format(endClock));
			workingHours.setText(String.valueOf(workHours) + "h");

		} catch (Exception e) {
			e.printStackTrace();
		}
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
			NavUtils.navigateUpFromSameTask(this);
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
			String startClock = Utils.dateToHour(data.getExtras().getString("startClock"));
			String stopClock = Utils.dateToHour(data.getExtras().getString("stopClock"));
			String commentString = data.getExtras().getString("comment");
			String jobName = data.getExtras().getString("jobName");
			Integer baseRate = data.getExtras().getInt("baseRate");

			startHour.setText(startClock);
			endHour.setText(stopClock);
			comment.setText(commentString);
			baseRateTextView.setText(String.valueOf(baseRate));
			jobNameTextView.setText(jobName + " Entry");
			baseRateTextView.setText(Utils.convertMoneyToString(baseRate) + "/hour");
		}
	}
}
