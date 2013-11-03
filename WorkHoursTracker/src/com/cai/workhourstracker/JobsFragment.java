package com.cai.workhourstracker;



import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class JobsFragment extends Fragment {

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
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		 

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   // handle item selection
	   switch (item.getItemId()) {
	      case R.id.add_job:
	    	  startActivity(new Intent(getActivity(),
						DoneBarActivity.class));
	    	 // Toast.makeText(getActivity(), "sdaf", Toast.LENGTH_SHORT).show();
	         return true;
	      default:
	         return super.onOptionsItemSelected(item);
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
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		
		inflater.inflate(R.menu.fragment_jobs_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
}
