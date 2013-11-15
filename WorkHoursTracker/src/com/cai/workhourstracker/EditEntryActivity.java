package com.cai.workhourstracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.cai.workhourstracker.helper.DatabaseHelper;
import com.cai.workhourstracker.model.Entry;
import com.cai.workhourstracker.model.Job;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.PendingIntent.OnFinished;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;

public class EditEntryActivity extends FragmentActivity implements
		AdapterView.OnItemSelectedListener,
		TimerPickerFragmentWithCancel.ChangeStartTimeListener,
		TimerPickerEndTimeFragmentWithCancel.ChangeEndTimeListener,
		DurationPickerFragment.ChangeDurationListener,
		TagsAddDialogFragment.TagsChangeListener {

	private Spinner jobsSpinner;
	private DatabaseHelper db;
	private List<Job> jobs;
	private List<String> jobNames;
	private Spinner startTime;
	private Spinner endTime;
	private Spinner duration;
	private EditText comment;
	private int entryId;
	private Entry entry;
	private Job job;
	private Spinner tags;
	private EditText baseRate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addCustomActionBar();
		setContentView(R.layout.activity_edit_entry);

		entryId = getIntent().getExtras().getInt("id");
		db = new DatabaseHelper(this);
		entry = db.getEntryById(entryId);

		initializeDate();
	}

	private void setSpinnerSingleValue(Spinner spinner, String value) {

		List<String> values = new ArrayList<String>();
		values.add(value);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, values);
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		spinner.setOnItemSelectedListener(this);
		spinner.setAdapter(adapter);
	}

	private void initializeDate() {
		db = new DatabaseHelper(this);
		jobs = db.getAllJobs();
		job = db.getJobById(entry.getJobId());
		db.close();
		jobNames = getJobsNames(jobs);
		jobNames.add(0, job.getName());
		jobsSpinner = (Spinner) findViewById(R.id.edit_entry_jobs);
		startTime = (Spinner) findViewById(R.id.edit_entry_start_clock);
		comment = (EditText) findViewById(R.id.edit_entry_comment);
		tags = (Spinner) findViewById(R.id.edit_entry_tags);
		baseRate = (EditText) findViewById(R.id.edit_entry_base_rate);

		baseRate.setText(String.valueOf(entry.getBaseRate()));

		setSpinnerSingleValue(tags, "None");

		setSpinnerSingleValue(startTime, entry.getStartClock());

		endTime = (Spinner) findViewById(R.id.edit_entry_end_clock);

		setSpinnerSingleValue(endTime, entry.getStartClock());
		duration = (Spinner) findViewById(R.id.edit_entry_duration);
		comment = (EditText) findViewById(R.id.edit_entry_comment);

		startTime.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					TimerPickerFragmentWithCancel dialog = new TimerPickerFragmentWithCancel();
					dialog.show(getSupportFragmentManager(), "pick time");
				}
				return true;
			}

		});

		endTime.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					TimerPickerEndTimeFragmentWithCancel dialog = new TimerPickerEndTimeFragmentWithCancel();
					dialog.show(getSupportFragmentManager(), "pick time");
				}
				return true;
			}
		});

		duration.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					DurationPickerFragment dialog = new DurationPickerFragment();
					dialog.show(getSupportFragmentManager(), "pick time");
				}
				return true;
			}

		});

		tags.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {

					TagsAddDialogFragment newFragment = TagsAddDialogFragment
							.newInstance();
					newFragment.show(getSupportFragmentManager(), "dialog");

					// TagsAddDialogFragment dialog = new
					// TagsAddDialogFragment();
					// dialog.show(getSupportFragmentManager(), "pick tags");
				}
				return true;
			}

		});

		baseRate.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub

				LinearLayout layout = (LinearLayout) findViewById(R.id.base_rate_buttons);
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(
						R.layout.edit_entry_regulate_buttons, null);
				if (hasFocus) {
					layout.addView(view);
				} else {
					layout.removeViewAt(0);
				}

			}

		});

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, jobNames);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		jobsSpinner.setOnItemSelectedListener(this);
		jobsSpinner.setAdapter(adapter);
	}

	private void addCustomActionBar() {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View customActionBarView = inflater.inflate(
				R.layout.actionbar_custom_view_done, null);
		customActionBarView.findViewById(R.id.actionbar_done)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						entry.setComment(comment.getText().toString());
						db.updateEntry(entry);
						db.close();
						finish();
					}
				});

		// Show the custom action bar view and hide the normal Home icon and
		// title.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
				ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
						| ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.setCustomView(customActionBarView);
		// END_INCLUDE (inflate_set_custom_view)
	}

	private List<String> getJobsNames(List<Job> jobs) {
		List<String> jobNamesList = new ArrayList();
		for (int i = 0; i < jobs.size(); i++) {
			jobNamesList.add(jobs.get(i).getName());
		}

		return jobNamesList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.cancel, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.cancel:
			// "Cancel"
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChangeStartTime(String time) {
		entry.setStartClock(time);
		List<String> currentTime = new ArrayList<String>();
		currentTime.add(time);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, currentTime);
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

		startTime.setOnItemSelectedListener(this);
		startTime.setAdapter(adapter);

	}

	@Override
	public void onChangeEndTime(String time) {

		entry.setStopClock(time);
		List<String> currentTime = new ArrayList<String>();
		currentTime.add(time);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, currentTime);
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

		endTime.setOnItemSelectedListener(this);
		endTime.setAdapter(adapter);

	}

	@Override
	public void onChangeDuration(String hours, String minutes) {

		String result = hours + " hours " + minutes + " minutes";
		List<String> durationList = new ArrayList<String>();
		durationList.add(result);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, durationList);
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		duration.setOnItemSelectedListener(this);
		duration.setAdapter(adapter);

		String startTimeString = startTime.getSelectedItem().toString();

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		try {
			Date date = dateFormat.parse(startTimeString);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.MINUTE, Integer.valueOf(minutes));
			calendar.add(Calendar.HOUR, Integer.valueOf(hours));

			date = calendar.getTime();

			List<String> time = new ArrayList<String>();
			time.add(dateFormat.format(date));

			ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, time);
			timeAdapter
					.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
			endTime.setOnItemSelectedListener(this);
			endTime.setAdapter(timeAdapter);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Toast.makeText(this, startTimeString, Toast.LENGTH_SHORT).show();

	}

	@Override
	public void finish() {
		Intent intent = new Intent();

		intent.putExtra("startClock", entry.getStartClock());
		intent.putExtra("stopClock", entry.getStopClock());
		intent.putExtra("comment", entry.getComment());

		setResult(RESULT_OK, intent);
		super.finish();
	}

	@Override
	public void onTagsChangeTime(String time) {
		// TODO Auto-generated method stub

	}

	public void onUnpaidRate(View view) {
		baseRate.setText("0");
	}

	public void onTwiceRate(View view) {
		String baseRateString = String.valueOf(100 * 2);
		baseRate.setText(baseRateString);
	}

	public void onJobRate(View view) {
		String baseRateString = String.valueOf(100);
		baseRate.setText(baseRateString);
	}
}
