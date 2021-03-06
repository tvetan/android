package com.cai.workhourstracker;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import Utils.DateCalculateUtils;
import Utils.DateFormatUtils;
import Utils.MoneyFormatUtils;
import Utils.ToastUtils;
import Utils.Utils;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cai.workhourstracker.adapters.EntriesListAdapter;
import com.cai.workhourstracker.dialogs.RemoveCurrentClockDialogFragment;
import com.cai.workhourstracker.fragments.WorkProgressFragment;
import com.cai.workhourstracker.helper.DatabaseHelper;
import com.cai.workhourstracker.helper.MoneyCalculateUtils;
import com.cai.workhourstracker.helper.StopClockViewHolder;
import com.cai.workhourstracker.model.Entry;
import com.cai.workhourstracker.model.Job;

public class StartClockActivity extends BaseEntriesListActivity implements
		AdapterView.OnItemClickListener, View.OnClickListener,
		RemoveCurrentClockDialogFragment.NoticeDialogListener {

	private static final String WORK_PROGRESS_FRAGMENT = "workProgressFragment";
	private static final int REQUEST_CODE = 10;
	private DatabaseHelper db;
	private List<Entry> entries;
	private ListView entriesList;
	private TextView commentTextView;
	private TextView jobNameTextView;
	private TextView jobBaseRateTextView;
	private Integer jobId;
	private ActionMode mActionMode;
	private TextView workHoursTextView;
	private TextView moneyEarnedTextView;
	private TextView startClockButton;
	private TextView stopClockButton;
	private ViewGroup listViewHeader;
	private LayoutInflater layoutInflater;
	private Job job;
	private FragmentManager fragmentManager;
	private LinearLayout jobNameBaseRateLayout;
	private LinearLayout startStopButtonsLayout;
	private boolean isWorkProgressShown;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_clock);

		setUpActionBar();
		getActionBar().setDisplayHomeAsUpEnabled(true);

		fragmentManager = getSupportFragmentManager();
		layoutInflater = getLayoutInflater();
		jobNameTextView = (TextView) findViewById(R.id.job_name_main_stop_clock);
		jobBaseRateTextView = (TextView) findViewById(R.id.start_clock_job_price);
		startClockButton = (TextView) findViewById(R.id.start_clock_button);
		stopClockButton = (TextView) findViewById(R.id.stop_clock_button);
		commentTextView = (TextView) findViewById(R.id.start_stop_row_comment);
		listViewHeader = (ViewGroup) layoutInflater.inflate(R.layout.start_clock_list_header,
				entriesList, false);
		workHoursTextView = (TextView) listViewHeader
				.findViewById(R.id.start_clock_header_work_hours);
		moneyEarnedTextView = (TextView) listViewHeader
				.findViewById(R.id.start_clock_header_money_earned);
		entriesList = (ListView) findViewById(R.id.entries_start_clock);
		jobNameBaseRateLayout = (LinearLayout) findViewById(R.id.job_name_base_rate_layout);
		startStopButtonsLayout = (LinearLayout) findViewById(R.id.start_stop_buttons_linear_layout);
		db = new DatabaseHelper(getApplicationContext());

		Bundle extras = getIntent().getExtras();
		if (extras.get("id") != null) {
			setUpJobFragment(extras);
			isWorkProgressShown = true;
		} else {
			setUpGroupViewFragments(extras);
			startStopButtonsLayout.setVisibility(View.GONE);
			isWorkProgressShown = false;
		}

		this.setUpData();
		this.setUpEvents();
		this.setUpEntriesListView(entries,  entriesList,
				listViewHeader);

		if (job.getIsWorking() && isWorkProgressShown) {
			addWorkProgressFragment();
		}
	}

	private void setUpGroupViewFragments(Bundle extras) {
		String jobName = extras.getString("jobName");
		String filterDate = extras.getString("dateFilter");
		job = db.getJobByName(jobName);
		jobNameTextView.setText(jobName + "\n" + filterDate);
		entries = db.getAllEntriesByJobId(job.getId());
	
	}

	private void setUpJobFragment(Bundle extras) {

		String idString = extras.get("id").toString();
		jobId = Integer.valueOf(idString);
		job = db.getJobById(jobId);

		entries = db.getAllEntriesByJobId(jobId);
		db.closeDB();
		jobNameTextView.setText(job.getName());

		setJobBaseRate();
	}

	private void setUpData() {
		int moneyEarned = Utils.moneyEarnedEntries(entries);
		int workHours = Utils.workHoursForEntries(entries);

		workHoursTextView.setText(String.valueOf(workHours) + "h");
		moneyEarnedTextView.setText(Utils.convertMoneyToString(moneyEarned));
		isFirstListViewInicialization = true;
	}

	private void setJobBaseRate() {
		Integer jobBaseRate = job.getHourPrice();

		String formatedBaseRate = MoneyFormatUtils.toLocaleCurrencyFormatFromInteger(jobBaseRate);
		// BigDecimal money = (new BigDecimal(moneyPerHour)).divide(new
		// BigDecimal(100));

		// jobMoneyEanerdTextView.setText("$" + new
		// DecimalFormat("#0.##").format(money) + "/hour");

		jobBaseRateTextView.setText(formatedBaseRate + "/hour");

	}

	private void setUpEvents() {

		jobNameBaseRateLayout.setOnClickListener(this);

		startClockButton.setOnClickListener(this);
		stopClockButton.setOnClickListener(this);

		entriesList.setHeaderDividersEnabled(true);
		entriesList.setOnItemClickListener(this);
		entriesList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
				mActionMode = StartClockActivity.this.startActionMode(modeCallBack);
				entriesList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
				entriesList.setItemChecked(position, true);

				return true;
			}
		});

	}

	private void setUpActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.start_clock, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.remove_current_clock:
			RemoveCurrentClockDialogFragment dialog = new RemoveCurrentClockDialogFragment();
			dialog.show(getSupportFragmentManager(), "d");
			return true;
		case R.id.edit_current_job:
			Intent intent = new Intent(this, EditJobActivity.class);
			intent.putExtra("id", jobId);
			startActivityForResult(intent, REQUEST_CODE);
			return true;
		}

		return super.onOptionsItemSelected(item);
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

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		Log.d("test11", "on save instance called");
		if (jobId != null) {
			savedInstanceState.putInt("id", jobId);
		}

	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Log.d("test restore", "on restore instance called");
		jobId = savedInstanceState.getInt("id");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("pause", "pause");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.d("restart", "restart" + jobId);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("Resume", "Resume");
	}

	private ActionMode.Callback modeCallBack = new ActionMode.Callback() {

		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		public void onDestroyActionMode(ActionMode mode) {
			entriesList.clearChoices();
			for (int i = 0; i < entriesList.getChildCount(); i++)
				entriesList.setItemChecked(i, false);
			entriesList.post(new Runnable() {
				@Override
				public void run() {
					entriesList.setChoiceMode(ListView.CHOICE_MODE_NONE);
				}
			});

			mode = null;
		}

		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			mode.setTitle("1 Aufgabe");
			mode.getMenuInflater().inflate(R.menu.edit_entry, menu);
			return true;
		}

		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			// case R.id.actionmode_delete:
			// int choiceCount = listView.getCount();
			// SparseBooleanArray spBoolArray =
			// listView.getCheckedItemPositions();
			//
			// DBAufgaben db = new DBAufgaben(MainActivity.getMContext());
			// db.open();
			//
			// for (int i = 0; i < choiceCount; i++) {
			// if(spBoolArray.get(i)){
			// db.deletContact(listView.getItemIdAtPosition(i));
			// }
			//
			// }
			// Cursor cursor = db.getAllRecords();
			// AdapterEingang adapterE = new
			// AdapterEingang(MainActivity.getMContext(), cursor, 0);
			// listView.setAdapter(adapterE);
			// db.close();
			// mode.finish();
			// break;
			// case R.id.actionmode_cancel:
			// mode.finish();
			// break;
			}
			return false;
		}
	};

	// The entry is stopped and removed
	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		db.stopWork(job.getId());
		job.setIsWorking(false);
		this.removeWorkProgressFragment();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		db.closeDB();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == StartClockActivity.RESULT_OK && requestCode == REQUEST_CODE) {

			job = db.getJobById(jobId);
			jobNameTextView.setText(job.getName());
			setJobBaseRate();

		}
	}

	// public class StopClockAdapter extends ArrayAdapter<Entry> {
	//
	// private Context context;
	// private Entry[] entries;
	//
	// public StopClockAdapter(Context c, Entry[] entries) {
	// super(c, R.layout.stop_clock_list_row, entries);
	// // super(c, R.layout.stop_clock_list_row, R.id.textView1, entries);
	// context = c;
	// this.entries = entries;
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// View row = convertView;
	// StopClockViewHolder holder = null;
	//
	// if (row == null) {
	// LayoutInflater inflater = (LayoutInflater) context
	// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	// row = inflater.inflate(R.layout.stop_clock_list_row, parent, false);
	// holder = new StopClockViewHolder(row);
	// row.setTag(holder);
	// } else {
	// holder = (StopClockViewHolder) row.getTag();
	// }
	//
	// String monthDateYearDate =
	// Utils.dateToMonthDateYearFormat(entries[position]
	// .getStartClock());
	// String dayOfWeek =
	// Utils.dateToDayOfWeek(entries[position].getStartClock());
	// String startHour = Utils.dateToHour(entries[position].getStartClock());
	// String stopHour = Utils.dateToHour(entries[position].getStopClock());
	//
	// holder.getComment().setText(entries[position].getComment());
	// holder.getFullDate().setText(monthDateYearDate);
	// holder.getDayOfWeek().setText(dayOfWeek);
	// holder.getStartHour().setText(startHour);
	// holder.getStopHours().setText(stopHour);
	// holder.getId().setText(String.valueOf(entries[position].getId()));
	//
	// Date startClock =
	// DateFormatUtils.fromDatabaseFormatToDate(entries[position]
	// .getStartClock());
	// Date endClock =
	// DateFormatUtils.fromDatabaseFormatToDate(entries[position]
	// .getStopClock());
	// double workHours =
	// DateCalculateUtils.differenceBetweenTwoDatesInMinutes(startClock,
	// endClock);
	// NumberFormat format = NumberFormat.getNumberInstance();
	// format.setMinimumFractionDigits(2);
	// format.setMaximumFractionDigits(2);
	//
	// holder.getWorkHours().setText(format.format(workHours) + "h");
	// BigDecimal moneyEarned = MoneyCalculateUtils.moneyEarned(workHours,
	// entries[position]
	// .getBaseRate());
	//
	// holder.getMoneyEarned().setText(
	// MoneyFormatUtils.toLocaleCurrencyFormatFromBigDecimal(moneyEarned));
	//
	// // holder.getMoneyEarned().setText(
	// // Utils.convertMoneyToString(entries[position].getEarned_money()));
	// // holder.getWorkHours().setText(
	// // String.valueOf(Utils.differenceBetweenStartAndStop(entries[position]))
	// // + "h");
	//
	// return row;
	// }
	// }

	private void addWorkProgressFragment() {
		WorkProgressFragment testFragment = (WorkProgressFragment) fragmentManager
				.findFragmentByTag(WORK_PROGRESS_FRAGMENT);
		if (testFragment != null) {
			ToastUtils.makeShortToast(this, "There is a started job");
			return;
		}

		WorkProgressFragment fragment = createProgressFragment();// // new
																	// WorkProgressFragment();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.work_progress_container, fragment, WORK_PROGRESS_FRAGMENT);
		fragmentTransaction.commit();
	}

	private WorkProgressFragment createProgressFragment() {
		String workStartedAt = job.getStartWorkAt();
		Integer jobBaseRate = job.getHourPrice();
		Integer jobTimePerDay = job.getTimePerDate();

		return WorkProgressFragment.newInstance(workStartedAt, jobBaseRate, jobTimePerDay);

	}

	private void removeWorkProgressFragment() {
		WorkProgressFragment fragment = (WorkProgressFragment) fragmentManager
				.findFragmentByTag(WORK_PROGRESS_FRAGMENT);
		if (fragment == null) {
			ToastUtils.makeShortToast(this, "There is no work to be stopped");
			return;
		}

		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.remove(fragment);
		fragmentTransaction.commit();
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.stop_clock_button) {
			job.setIsWorking(false);
			db.stopWork(job.getId());
			this.removeWorkProgressFragment();
			this.addNewEntryAndUpdateListView();
		} else if (view.getId() == R.id.start_clock_button) {
			// We are already working
			if (!job.getIsWorking()) {
				ToastUtils.makeShortToast(this, "ne raboti");
				String startedWorkTime = db.startWork(job.getId());
				job.setStartWorkAt(startedWorkTime);
				job.setIsWorking(true);
			}

			this.addWorkProgressFragment();
		} else {
			Intent intent = new Intent(this, EditJobActivity.class);
			intent.putExtra("id", jobId);
			startActivityForResult(intent, REQUEST_CODE);
		}
	}

	private void addNewEntryAndUpdateListView() {
		Entry entry = new Entry();
		entry.setBaseRate(job.getHourPrice());
		entry.setJobId(job.getId());
		entry.setStartClock(job.getStartWorkAt());
		entry.setStopClock(DateFormatUtils.toDatabaseFormat(new Date()));
		// TODO calculate it
		entry.setEarned_money(111);
		long entryId = db.createEntry(entry);
		entry.setId((int) entryId);
		entries.add(0, entry);

		this.setUpEntriesListView(entries,  entriesList,
				listViewHeader);
	}
}
