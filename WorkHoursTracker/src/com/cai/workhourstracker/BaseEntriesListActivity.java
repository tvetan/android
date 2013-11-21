package com.cai.workhourstracker;

import java.util.List;

import com.cai.workhourstracker.adapters.EntriesListAdapter;
import com.cai.workhourstracker.model.Entry;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ListView;

public class BaseEntriesListActivity extends FragmentActivity {

	protected EntriesListAdapter adapter;
	protected boolean isFirstListViewInicialization = true;

	protected void setUpEntriesListView(List<Entry> entries, ListView entriesList,
			ViewGroup listViewHeader) {
		Entry[] entriesAsArray = new Entry[entries.size()];
		adapter = new EntriesListAdapter(this, entries.toArray(entriesAsArray));
		if (isFirstListViewInicialization) {
			entriesList.addHeaderView(listViewHeader, null, false);
			this.isFirstListViewInicialization = false;
		}

		entriesList.setAdapter(adapter);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_entries_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.base_entries_list, menu);
		return true;
	}

}
