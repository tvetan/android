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

import java.math.BigDecimal;

import com.cai.workhourstracker.model.Job;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A sample activity demonstrating the "done bar" alternative action bar
 * presentation. For a more detailed description see
 * {@link R.string.done_bar_description}.
 */
public class AddJobActivity extends Activity {

	TextView jobName;
	TextView jobPrice;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// BEGIN_INCLUDE (inflate_set_custom_view)
		// Inflate a "Done/Cancel" custom action bar view.
		// final LayoutInflater inflater = (LayoutInflater)
		// getActionBar().getThemedContext()
		// .getSystemService(LAYOUT_INFLATER_SERVICE);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View customActionBarView = inflater.inflate(
				R.layout.actionbar_custom_view_done_cancel, null);
		customActionBarView.findViewById(R.id.actionbar_done)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						finish();
					}
				});
		customActionBarView.findViewById(R.id.actionbar_cancel)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// "Cancel"
						finish();
					}
				});

		// Show the custom action bar view and hide the normal Home icon and
		// title.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
				ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
						| ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.setCustomView(customActionBarView,
				new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT));
		// END_INCLUDE (inflate_set_custom_view)

		setContentView(R.layout.activity_add_job);

		jobName = (TextView) findViewById(R.id.add_job_name);
		jobPrice = (TextView) findViewById(R.id.add_job_price);
	}

	@Override
	public void finish() {
		Intent intent = new Intent();

		String jobNameString = jobName.getText().toString();
		String jobPriceString = jobPrice.getText().toString();
		Job job = new Job();
		job.setName(jobNameString);

		if (jobNameString.length() > 0) {
			intent.putExtra("jobName", jobNameString);
		}
		if (jobPriceString.length() > 0) {
			intent.putExtra("jobPrice", jobPriceString);
		}
	 
		
		setResult(RESULT_OK, intent);
		super.finish();
	}
}
