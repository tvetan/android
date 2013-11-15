package com.cai.workhourstracker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import com.cai.workhourstracker.JobsFragment.SectionComposerAdapter;
import com.cai.workhourstracker.helper.DatabaseHelper;
import com.cai.workhourstracker.helper.StopClockViewHolder;
import com.cai.workhourstracker.model.Entry;
import com.cai.workhourstracker.model.Job;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;

public class StartClockActivity extends FragmentActivity implements
		AdapterView.OnItemClickListener {

	private static final int REQUEST_CODE = 10;
	private DatabaseHelper db;
	private List<Entry> entries;
	private ListView entriesList;
	private TextView comment;
	private TextView jobName;
	private TextView jobPrice;
	private int jobId;
	private ActionMode mActionMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_clock);
		setupActionBar();
		getActionBar().setDisplayHomeAsUpEnabled(true);

		jobName = (TextView) findViewById(R.id.job_name_main_stop_clock);
		jobPrice = (TextView) findViewById(R.id.start_clock_job_price);
		db = new DatabaseHelper(getApplicationContext());

		Bundle extras = getIntent().getExtras();
		String id_string = extras.get("id").toString();

		jobId = Integer.valueOf(id_string);
		Job job = db.getJobById(jobId);
		entries = db.getAllEntriesByJobId(jobId);
		db.closeDB();

		jobName.setText(job.getName());
		Integer moneyPerHour = job.getHourPrice();
		BigDecimal money = (new BigDecimal(moneyPerHour))
				.divide(new BigDecimal(100));
		jobPrice.setText("$" + new DecimalFormat("#0.##").format(money)
				+ "/hour");
		entriesList = (ListView) findViewById(R.id.entries_start_clock);

		LayoutInflater inflater = getLayoutInflater();
		ViewGroup header = (ViewGroup) inflater.inflate(
				R.layout.start_clock_list_header, entriesList, false);

		entriesList.addHeaderView(header, null, false);
		entriesList.setHeaderDividersEnabled(true);

		entriesList.setOnItemClickListener(this);
		Entry[] entries_as_string_array = new Entry[entries.size()];

		StopClockAdapter adapter = new StopClockAdapter(this,
				entries.toArray(entries_as_string_array));
		// entriesList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
		entriesList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long id) {

				mActionMode = StartClockActivity.this
						.startActionMode(modeCallBack);
				entriesList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
				entriesList.setItemChecked(position, true);

				return true;
			}
		});

		entriesList.setAdapter(adapter);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
		savedInstanceState.putInt("id", jobId);

		// etc.
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
		// TODO Auto-generated method stub
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
}

class StopClockAdapter extends ArrayAdapter<Entry> {

	Context context;
	Entry[] entries;

	public StopClockAdapter(Context c, Entry[] entries) {
		super(c, R.layout.stop_clock_list_row, entries);
		// super(c, R.layout.stop_clock_list_row, R.id.textView1, entries);
		context = c;
		this.entries = entries;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		StopClockViewHolder holder = null;
		
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.stop_clock_list_row, parent, false);
			holder = new StopClockViewHolder(row);
			row.setTag(holder);
		} else {
			holder = (StopClockViewHolder) row.getTag();
		}

		/*
		 * ImageView myImage = (ImageView) row.findViewById(R.id.imageView1);
		 * TextView title = (TextView) row.findViewById(R.id.textView1);
		 * TextView description = (TextView) row.findViewById(R.id.textView2);
		 */
		// Date startClock = entries[position].getStartClock();
		holder.getComment().setText(entries[position].getComment());
		holder.getFullDate().setText(entries[position].getStartClock());
		holder.getId().setText(String.valueOf(entries[position].getId()));

		return row;
	}
}
