package com.cai.workhourstracker;



import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class EditJobActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// BEGIN_INCLUDE (inflate_set_custom_view)
		// Inflate a "Done" custom action bar view to serve as the "Up"
		// affordance.
		//final LayoutInflater inflater = (LayoutInflater) getActionBar()
		//		.getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
 
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View customActionBarView = inflater.inflate(
				R.layout.actionbar_custom_view_done, null);
		customActionBarView.findViewById(R.id.actionbar_done)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// "Done"
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

		setContentView(R.layout.activity_edit_job);
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
}
