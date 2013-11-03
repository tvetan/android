package com.cai.workhourstracker;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class PayPeriodsFragment extends Fragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_pay_periods,
				container, false);

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

//		ActionBar actionBar = getActivity().getActionBar();
//		actionBar.setCustomView(R.layout.jobs_filter);
//		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
//				| ActionBar.DISPLAY_SHOW_HOME);

		Spinner spinner = (Spinner) getActivity().findViewById(
				R.id.jobs_filter_spinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.pay_periods_spinner_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);

		inflater.inflate(R.menu.fragment_pay_periods_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
}