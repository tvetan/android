package com.cai.workhourstracker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.cai.workhourstracker.Views.GroupListView;
import com.cai.workhourstracker.adapters.GroupViewAdapter;
import com.cai.workhourstracker.helper.DatabaseHelper;
import com.cai.workhourstracker.model.Composer;
import com.cai.workhourstracker.model.Job;
import com.cai.workhourstracker.model.JobNameComparator;
import com.cai.workhourstracker.model.JobNameReverseComparator;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class JobsFragment extends Fragment implements
		AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

	ActionMode mActionMode;
	private int counterChecked = 0;
	GroupListView listView;
	SectionComposerAdapter adapter;
	DatabaseHelper db;
	List<Job> onTheClock;
	List<Job> ofTheClock;
	List<Pair<String, List<Job>>> all;
	// String[] headers = new String[] { "On the clock", "Of the clock" };
	List<String> headers = new ArrayList<String>(Arrays.asList("On the clock",
			"Of the clock"));
	private boolean initializedView = false;
	private static final int REQUEST_CODE = 10;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_jobs, container,
				false);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		listView = (GroupListView) getActivity().findViewById(R.id.lsComposer);
		listView.setPinnedHeaderView(LayoutInflater.from(getActivity())
				.inflate(R.layout.item_composer_header, listView, false));
		listView.setOnItemClickListener(this);
		db = new DatabaseHelper(getActivity());

		List<Job> jobs = db.getAllJobs();

		Toast.makeText(getActivity(), Integer.toString(jobs.size()),
				Toast.LENGTH_LONG).show();
		all = new ArrayList<Pair<String, List<Job>>>();

		// String[] headers = new String[] { "On the clock", "Of the clock" };

		onTheClock = db.getOnClockJobs();
		ofTheClock = db.getOffClockJobs();

		all.add(new Pair<String, List<Job>>(headers.get(0), onTheClock));
		all.add(new Pair<String, List<Job>>(headers.get(1), ofTheClock));

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				if (mActionMode != null) {
					return false;
				}
				mActionMode = getActivity().startActionMode(modeCallBack);
				listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
				listView.setItemChecked(position, true);

				counterChecked++;
				setNewTitle();

				return true;
			}
		});

		listView.setAdapter(adapter = new SectionComposerAdapter(headers, all));

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
		case R.id.add_job:
			// Intent intent = new Intent(getActivity(), AddJobActivity.class);

			mActionMode = getActivity().startActionMode(modeCallBack);
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

			counterChecked = listView.getAdapter().getCount();

			setNewTitle();
			listView.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					for (int i = 0; i < listView.getAdapter().getCount(); i++) {
						listView.setItemChecked(i, true);
					}
				}
			});

			// startActivityForResult(intent, REQUEST_CODE);
			// Toast.makeText(getActivity(), "sdaf", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == MainActivity.RESULT_OK && requestCode == REQUEST_CODE) {
			if (data.hasExtra("jobName") && data.hasExtra("jobPrice")) {
				String jobNameString = data.getExtras().getString("jobName");
				String jobPriceString = data.getExtras().getString("jobPrice");

				if (jobNameString != null && jobPriceString != null
						&& jobNameString.length() > 0) {

					BigDecimal money = (new BigDecimal(jobPriceString))
							.multiply(new BigDecimal(1000));

					int money_int = money.intValueExact();

					Job job = new Job(jobNameString, false, money_int);
					db.createJob(job, null);
					ofTheClock.add(job);
					listView.setAdapter(adapter = new SectionComposerAdapter(
							headers, all));
				}
			}
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setCustomView(R.layout.jobs_filter);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);

		Spinner spinner = (Spinner) getActivity().findViewById(
				R.id.jobs_filter_spinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.jobs_spinner_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner.setOnItemSelectedListener(this);

		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);

		inflater.inflate(R.menu.fragment_jobs_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	public class SectionComposerAdapter extends GroupViewAdapter {
		List<Pair<String, List<Job>>> all;
		List<String> sectionHeaders;

		public SectionComposerAdapter(List<String> sectionHeaders,
				List<Pair<String, List<Job>>> all) {
			this.sectionHeaders = sectionHeaders;
			this.all = all;
		}

		@Override
		public int getCount() {
			int res = 0;
			for (int i = 0; i < all.size(); i++) {
				res += all.get(i).second.size();
			}
			
			return res;
		}

		@Override
		public Job getItem(int position) {
			int c = 0;
			for (int i = 0; i < all.size(); i++) {
				if (position >= c && position < c + all.get(i).second.size()) {
					return all.get(i).second.get(position - c);
				}
				c += all.get(i).second.size();
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		protected void onNextPageRequested(int page) {
		}

		@Override
		protected void bindSectionHeader(View view, int position,
				boolean displaySectionHeader) {
			if (displaySectionHeader) {
				view.findViewById(R.id.header).setVisibility(View.VISIBLE);
				TextView lSectionTitle = (TextView) view
						.findViewById(R.id.header);
				lSectionTitle
						.setText(getSections()[getSectionForPosition(position)]);
			} else {
				view.findViewById(R.id.header).setVisibility(View.GONE);
			}
		}

		@Override
		public View getAmazingView(int position, View convertView,
				ViewGroup parent) {
			View res = convertView;
			if (res == null)
				res = getActivity().getLayoutInflater().inflate(
						R.layout.item_composer, null);

			TextView jobName = (TextView) res.findViewById(R.id.jobName);
			TextView jobId = (TextView) res.findViewById(R.id.jobId);
			Job job = getItem(position);
			
			jobName.setText(job.getName());
			jobId.setText(String.valueOf(job.getId()));

			return res;
		}

		@Override
		public void configurePinnedHeader(View header, int position, int alpha) {
			TextView lSectionHeader = (TextView) header;
			lSectionHeader
					.setText(getSections()[getSectionForPosition(position)]);
			lSectionHeader.setBackgroundColor(alpha << 24 | (0xbbffbb));
			lSectionHeader.setTextColor(alpha << 24 | (0x000000));
		}

		@Override
		public int getPositionForSection(int section) {
			if (section < 0)
				section = 0;
			if (section >= all.size())
				section = all.size() - 1;
			int c = 0;
			for (int i = 0; i < all.size(); i++) {
				if (section == i) {
					return c;
				}
				c += all.get(i).second.size();
			}
			return 0;
		}

		@Override
		public int getSectionForPosition(int position) {
			int c = 0;
			for (int i = 0; i < all.size(); i++) {
				if (position >= c && position < c + all.get(i).second.size()) {
					return i;
				}
				c += all.get(i).second.size();
			}
			return -1;
		}

		@Override
		public String[] getSections() {
			return sectionHeaders.toArray(new String[sectionHeaders.size()]);
		}
	}

	public void setNewTitle() {
		if (counterChecked == 1) {
			mActionMode.setTitle(counterChecked + " Selected");
		} else {
			mActionMode.setTitle(counterChecked + " Selected");
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {

		if (mActionMode == null) {
			Intent intent = new Intent(getActivity(), StartClockActivity.class);
			TextView textView = (TextView) view.findViewById(R.id.jobId);
			CharSequence id = textView.getText();
			intent.putExtra("id", id);
			startActivity(intent);
		} else {
			if (listView.isItemChecked(arg2)) {
				listView.setItemChecked(arg2, true);
				counterChecked++;
				setNewTitle();
			} else {
				listView.setItemChecked(arg2, false);
				counterChecked--;
				setNewTitle();
			}
			if (counterChecked == 0) {
				mActionMode.finish();
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (arg2 == 0) {
			for (int i = 0; i < all.size(); i++) {
				Collections.sort(all.get(i).second, new JobNameComparator());
			}
			
			listView.setAdapter(adapter = new SectionComposerAdapter(headers, all));
		} else if (arg2 == 1) {

			for (int i = 0; i < all.size(); i++) {
				Collections.sort(all.get(i).second, new JobNameReverseComparator());
			}
			
			listView.setAdapter(adapter = new SectionComposerAdapter(headers, all));
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}

	private ActionMode.Callback modeCallBack = new ActionMode.Callback() {

		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		public void onDestroyActionMode(ActionMode mode) {
			listView.clearChoices();
			for (int i = 0; i < listView.getChildCount(); i++)
				listView.setItemChecked(i, false);
			listView.post(new Runnable() {
				@Override
				public void run() {
					listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
				}
			});
			counterChecked = 0;
			mActionMode = null;
		}

		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			mode.setTitle("1 Selected");
			mode.getMenuInflater().inflate(R.menu.single_entry, menu);
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
