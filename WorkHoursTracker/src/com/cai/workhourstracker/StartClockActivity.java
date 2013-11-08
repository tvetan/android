package com.cai.workhourstracker;

import java.util.Date;
import java.util.List;

import com.cai.workhourstracker.helper.DatabaseHelper;
import com.cai.workhourstracker.helper.StopClockViewHolder;
import com.cai.workhourstracker.model.Entry;
import com.cai.workhourstracker.model.Job;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

	private DatabaseHelper db;
	private List<Entry> entries;
	private ListView entriesList;
	private TextView comment;
	private TextView jobName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_clock);
		// Show the Up button in the action bar.
		setupActionBar();

		jobName = (TextView) findViewById(R.id.job_name_main_stop_clock);
		
		
		db = new DatabaseHelper(getApplicationContext());
		entries = db.getAllEntries();
		List<Job> jobs = db.getAllToDos();
		
		jobName.setText(jobs.get(0).getName());
		
		entriesList = (ListView) findViewById(R.id.entries_start_clock);
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, entries);
		LayoutInflater inflater = getLayoutInflater();
		ViewGroup header = (ViewGroup) inflater.inflate(
				R.layout.start_clock_list_header, entriesList, false);

		entriesList.addHeaderView(header, null, false);
		entriesList.setHeaderDividersEnabled(true);

		entriesList.setOnItemClickListener(this);
		Entry[] entries_as_string_array = new Entry[entries.size()];
		StopClockAdapter adapter = new StopClockAdapter(this,
				entries.toArray(entries_as_string_array));
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		LinearLayout temp = (LinearLayout) arg1;

		Toast.makeText(this, " " + arg2, Toast.LENGTH_SHORT).show();

	}

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
			Log.d("test", "dasfasd dfasf");
		} else {
			holder = (StopClockViewHolder) row.getTag();
			Log.d("test1", "dasfasd dfasf1111111111");
		}

		/*
		 * ImageView myImage = (ImageView) row.findViewById(R.id.imageView1);
		 * TextView title = (TextView) row.findViewById(R.id.textView1);
		 * TextView description = (TextView) row.findViewById(R.id.textView2);
		 */
		//Date startClock = entries[position].getStartClock();
		holder.getComment().setText(entries[position].getComment());
		holder.getFullDate().setText(entries[position].getStartClock());

		return row;
	}
}
