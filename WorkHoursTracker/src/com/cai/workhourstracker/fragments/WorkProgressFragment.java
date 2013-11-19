package com.cai.workhourstracker.fragments;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.cai.workhourstracker.ExportSelectionActivity;
import com.cai.workhourstracker.R;
import com.cai.workhourstracker.dialogs.DeleteEntriesByDateDialog;
import com.cai.workhourstracker.helper.DateFormatUtils;
import com.cai.workhourstracker.helper.MoneyCalculateUtils;
import com.cai.workhourstracker.helper.MoneyFormatUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WorkProgressFragment extends Fragment {

	private static final int MILLISECONDS_IN_A_SECOND = 1000;
	private TextView workHoursTextView;
	private TextView progressStartedTimeTextView;
	private TextView earnedMoneyTextView;
	private Timer workProgressTimer;
	private String startWorkTime;
	private BigDecimal jobBaseRate;
	private Date startWorkDate;
	private ProgressBar workProgressBar;
	private int i = 0;
	private final Handler myHandler = new Handler();

	public static WorkProgressFragment newInstance(String startTime, Integer jobBaseRate,
			Integer jobTimePerDay) {

		WorkProgressFragment fragment = new WorkProgressFragment();
		Bundle args = new Bundle();
		args.putString("startTime", startTime);
		args.putInt("jobBaseRate", jobBaseRate);
		args.putInt("jobTimePerDay", jobTimePerDay);
		fragment.setArguments(args);

		return fragment;
	}

	public WorkProgressFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("created fragment", "created fragment");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("pause fragment", "pause fragment");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("resume fragment", "resume fragment");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("destroy fragment", "destoy fragment");
		workProgressTimer.cancel();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_work_progress, container, false);
		Bundle arguments = getArguments();
		workHoursTextView = (TextView) rootView.findViewById(R.id.work_progress_work_hours);
		progressStartedTimeTextView = (TextView) rootView.findViewById(R.id.work_progress_started);
		earnedMoneyTextView = (TextView) rootView.findViewById(R.id.work_progress_money_earned);
		workProgressBar = (ProgressBar) rootView.findViewById(R.id.work_progress_bar);
		workProgressTimer = new Timer();

		this.setDefaultValues(arguments);

		workProgressBar.setMax(200);
		startProgress();

		return rootView;
	}

	private String workTimeElapsedFormated() {
		double hours = workTimeElapsed();
		String workHoursFormated = formatTimeHours(hours);

		return workHoursFormated;
	}

	private double workTimeElapsed() {
		Date currentDate = new Date();
		long diffInMillies = currentDate.getTime() - startWorkDate.getTime();
		long differenceInMinutes = diffInMillies / (MILLISECONDS_IN_A_SECOND * 60);
		double hours = (double) differenceInMinutes / 60;

		return hours;
	}

	private String formatTimeHours(double hours) {
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(2);

		return format.format(hours);
	}

	private void setDefaultValues(Bundle arguments) {
		startWorkTime = DateFormatUtils.formatToHourAndMinutes(arguments.getString("startTime"));
		progressStartedTimeTextView.setText("since " + startWorkTime);
		startWorkDate = DateFormatUtils.fromDatabaseFormatToDate(arguments.getString("startTime"));
		workHoursTextView.setText(workTimeElapsedFormated() + "h");

		jobBaseRate = MoneyCalculateUtils.convertToBigDecimal(arguments.getInt("jobBaseRate"));

	}

	private String moneyEanedFormated() {
		double workTimeHours = workTimeElapsed();
		BigDecimal moneyEarned = (new BigDecimal(workTimeHours)).multiply(jobBaseRate);

		return MoneyFormatUtils.toLocaleCurrencyFormatFromBigDecimal(moneyEarned);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	final Runnable myRunnable = new Runnable() {
		public void run() {
			workProgressBar.setProgress(i);
			workHoursTextView.setText(workTimeElapsedFormated());
			earnedMoneyTextView.setText(moneyEanedFormated());
		}
	};

	private void startProgress() {
		workProgressTimer = new Timer();
		workProgressTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				UpdateGUI();
			}
		}, 0, 1000);
	}

	private void UpdateGUI() {
		i++;
		myHandler.post(myRunnable);
	}
}
