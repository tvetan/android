package com.cai.workhourstracker.fragments;

import java.util.Collection;
import java.util.List;

import com.cai.workhourstracker.R;
import com.cai.workhourstracker.model.EntriesGroupViewRow;

import Utils.DateCalculateUtils;
import Utils.DateFormatUtils;
import Utils.MoneyFormatUtils;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class TabSwipeBaseFragment extends Fragment{

	
	protected Spinner attachSpinnerToActivity(int spinnerData, Fragment fragment){
		Spinner spinner = (Spinner) fragment.getActivity().findViewById(R.id.jobs_filter_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
				spinnerData, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setOnItemSelectedListener((OnItemSelectedListener) fragment);
		spinner.setAdapter(adapter);
		spinner.setSelection(spinnerSelectedPosition);
		
		return spinner;
	}
	
	protected int spinnerSelectedPosition = 0;
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		outState.putInt("spinnerSelectedPosition", spinnerSelectedPosition);
		Log.d("on SaveInstanceState base fragment swipe", "SaveInstanceState");
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	protected void fillHeaderData(View header, Collection<List<EntriesGroupViewRow>> groupViewRow) {

		 int moneyEarned = 0;
		 int workHours = 0;
		
		for (List<EntriesGroupViewRow> list : groupViewRow) {
			moneyEarned += moneyEarnedFromGroupViewRow(list);
			workHours += workHoursFromGroupViewRow(list);
		}

		TextView workHoursTextView = (TextView) header
				.findViewById(R.id.start_clock_header_work_hours);
		TextView moneyEarnedTextView = (TextView) header
				.findViewById(R.id.start_clock_header_money_earned);

		
		workHoursTextView.setText(String.valueOf(workHours) + "h");
		moneyEarnedTextView
				.setText(MoneyFormatUtils.toLocaleCurrencyFormatFromInteger(moneyEarned));
	}

	private int workHoursFromGroupViewRow(List<EntriesGroupViewRow> list) {
		int workHours = 0;
		for (EntriesGroupViewRow entriesGroupViewRow : list) {
			workHours += entriesGroupViewRow.getWorkHours();
		}
		
		return workHours;	 
	}

	private int moneyEarnedFromGroupViewRow(List<EntriesGroupViewRow> list) {
		int moneyEarned = 0;
		for (EntriesGroupViewRow entriesGroupViewRow : list) {
			moneyEarned += entriesGroupViewRow.getMoneyEarned();
		}
		
		return moneyEarned;
	}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
}
