package com.cai.workhourstracker;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.cai.workhourstracker.fragments.DurationPickerFragment;
import com.cai.workhourstracker.fragments.TagsAddDialogFragment;
import com.cai.workhourstracker.helper.DatabaseHelper;
import com.cai.workhourstracker.model.Job;

import Utils.EmptyUtils;
import Utils.MoneyFormatUtils;
import Utils.Utils;
import android.os.Bundle;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditJobActivity extends FragmentActivity implements
		AdapterView.OnItemSelectedListener, View.OnFocusChangeListener,
		DurationPickerFragment.ChangeDurationListener, TagsAddDialogFragment.TagsChangeListener {

	private DatabaseHelper db;
	private Job editedJob;
	private Integer editedJobId;
	private EditText jobNameEditText;
	private EditText jobBaseRateEditText;
	private EditText taxPercentageEditText;
	private EditText deductionsEditText;
	private Spinner tagsSpinner;
	private Spinner timePerDaySpinner;
	private Integer perDayWorkMinutes = 0;
	private Integer perDayWorkHours = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View customActionBarView = inflater
				.inflate(R.layout.actionbar_custom_view_done, null);
		customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Job jobFromFields = createJobFromFields();
						jobFromFields.setId(editedJobId);
						jobFromFields.setIsWorking(editedJob.getIsWorking());
						db.updateJob(jobFromFields);
						finish();
					}
				});
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.setCustomView(customActionBarView);
		// END_INCLUDE (inflate_set_custom_view)

		setContentView(R.layout.activity_add_job);

		db = new DatabaseHelper(this);
		editedJobId = getIntent().getExtras().getInt("id");
		editedJob = db.getJobById(editedJobId);

		jobNameEditText = (EditText) findViewById(R.id.add_job_name);
		jobBaseRateEditText = (EditText) findViewById(R.id.add_job_price);
		taxPercentageEditText = (EditText) findViewById(R.id.add_job_tax_percentage);
		deductionsEditText = (EditText) findViewById(R.id.add_job_deductions);
		tagsSpinner = (Spinner) findViewById(R.id.add_job_tags);
		timePerDaySpinner = (Spinner) findViewById(R.id.add_job_time_per_day);

		setSpinnerSingleValue(tagsSpinner, "None");
		setSpinnerSingleValue(timePerDaySpinner, "None");

		tagsSpinner.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					TagsAddDialogFragment newFragment = TagsAddDialogFragment.newInstance();
					newFragment.show(getSupportFragmentManager(), "dialog");
				}
				return true;
			}
		});

		timePerDaySpinner.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					DurationPickerFragment dialog = new DurationPickerFragment();
					dialog.show(getSupportFragmentManager(), "pick time");
				}

				return true;
			}
		});

		jobBaseRateEditText.setOnFocusChangeListener(this);
		taxPercentageEditText.setOnFocusChangeListener(this);
		deductionsEditText.setOnFocusChangeListener(this);

		setValuesFromJob(editedJob);
	}

	@Override
	public void finish() {
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);

		super.finish();
	}

	private Job createJobFromFields() {
		String jobName = jobNameEditText.getText().toString();
		String baseRate = jobBaseRateEditText.getText().toString();
		String taxPercentage = taxPercentageEditText.getText().toString();
		String deductions = deductionsEditText.getText().toString();
		Integer concatPerDayWorkHourMinutes = perDayWorkHours * 100 + perDayWorkMinutes;
		Job createdJob = new Job();

		createdJob.setName(jobName);
		createdJob.setHourPrice(Utils.stringToIntegerBaseRate(baseRate));
		createdJob.setDeduction(Utils.stringToIntegerBaseRate(deductions));
		createdJob.setTaxPercentage(Utils.stringToIntegerBaseRate(taxPercentage));
		createdJob.setTimePerDate(concatPerDayWorkHourMinutes);

		return createdJob;
	}

	private void setValuesFromJob(Job editedJob) {
		jobNameEditText.setText(editedJob.getName());
		jobBaseRateEditText.setText(MoneyFormatUtils.toLocaleCurrencyFormatFromInteger(editedJob
				.getHourPrice()));
		if (editedJob.getTimePerDate() != null) {
			setSpinnerSingleValue(timePerDaySpinner, String.valueOf(editedJob.getTimePerDate()));
		}

		// TODO ADD THE OTHER ONES THE % AND LV
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		db.closeDB();
	}

	// BEGIN_INCLUDE (handle_cancel)
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

	// END_INCLUDE (handle_cancel)

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		EditText currentView = (EditText) v;
		String inputValue = currentView.getText().toString();
		if (hasFocus) {
			if (inputValue.length() > 0) {
				inputValue.replaceAll("[$,/hour,%]", "");
				String jobBaseRateStriped = inputValue.replaceAll("[$,/hour,%]", "");
				currentView.setText(jobBaseRateStriped);
			}
		} else {
			if (inputValue.length() > 0) {
				if (currentView.getId() == R.id.add_job_price) {
					NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale
							.getDefault());
					currencyFormat.setMaximumFractionDigits(2);
					currencyFormat.setMinimumFractionDigits(2);

					double inputValueDouble = Double.parseDouble(inputValue);
					currentView.setText(currencyFormat.format(inputValueDouble) + "/hour");
				} else if (currentView.getId() == R.id.add_job_tax_percentage) {
					NumberFormat percentFormat = NumberFormat.getPercentInstance();
					percentFormat.setMaximumFractionDigits(2);
					percentFormat.setMinimumFractionDigits(2);
					double inputValueDouble = Double.parseDouble(inputValue) / 100;
					String result = percentFormat.format(inputValueDouble);
					currentView.setText(result);
				} else {

					NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale
							.getDefault());

					currencyFormat.setMaximumFractionDigits(2);
					currencyFormat.setMinimumFractionDigits(2);

					double inputValueDouble = Double.parseDouble(inputValue);
					currentView.setText(currencyFormat.format(inputValueDouble));
				}
			}
		}
	}

	private boolean isJobValid(Job createdJob) {
		if (EmptyUtils.isNullOrEmpty(createdJob.getName())) {
			Toast.makeText(this, "The job name cannot be empty", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (createdJob.getHourPrice() == 0) {
			Toast.makeText(this, "The job base rate cannot be 0", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	@Override
	public void onTagsChangeTime(String time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChangeDuration(String hours, String minutes) {
		// TODO Auto-generated method stub

	}

}
