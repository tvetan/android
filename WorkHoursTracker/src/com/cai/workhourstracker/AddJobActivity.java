/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cai.workhourstracker;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.cai.workhourstracker.helper.DatabaseHelper;
import com.cai.workhourstracker.helper.EmptyUtils;
import com.cai.workhourstracker.helper.Utils;
import com.cai.workhourstracker.model.Job;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.WorkSource;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class AddJobActivity extends FragmentActivity implements
		DurationPickerFragment.ChangeDurationListener, TagsAddDialogFragment.TagsChangeListener,
		AdapterView.OnItemSelectedListener, View.OnFocusChangeListener {

	private EditText jobNameEditText;
	private EditText jobBaseRateEditText;
	private EditText taxPercentageEditText;
	private EditText deductionsEditText;
	private Spinner tagsSpinner;
	private Spinner timePerDaySpinner;
	private Integer perDayWorkMinutes = 0;
	private Integer perDayWorkHours = 0;
	private Job createdJob;
	private DatabaseHelper db;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		db = new DatabaseHelper(this);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View customActionBarView = inflater.inflate(
				R.layout.actionbar_custom_view_done_cancel, null);
		customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						createdJob = createJobFromFields();
						if (isJobValid(createdJob)) {
							// TODO should add tags
							Integer jobId = (int) db.createJob(createdJob, null);
							createdJob.setId(jobId);
							finish();
						}
					}
				});
		customActionBarView.findViewById(R.id.actionbar_cancel).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
					//	isJobAdded = false;
						finish();
					}
				});

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		setContentView(R.layout.activity_add_job);

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
	public void finish() {
		Intent intent = new Intent();
		if (createdJob != null) {
			intent.putExtra("createdJobId", createdJob.getId());
			setResult(RESULT_OK, intent);
		}

		super.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		db.closeDB();
	}

	@Override
	public void onTagsChangeTime(String time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChangeDuration(String hours, String minutes) {
		
		Toast.makeText(this, hours + ""+ minutes, Toast.LENGTH_SHORT).show();
		perDayWorkHours = Integer.parseInt(hours);
		
		perDayWorkMinutes = Integer.parseInt(minutes);
		String timePerDay = String.valueOf(perDayWorkHours) + ","
				+ String.valueOf(perDayWorkMinutes) + "h";
		setSpinnerSingleValue(timePerDaySpinner, timePerDay);
	}

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
		
		if(createdJob.getHourPrice() == 0){
			Toast.makeText(this, "The job base rate cannot be 0", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}
}
