package com.cai.workhourstracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.cai.workhourstracker.filters.InputFilterMinMax;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

public class DurationPickerFragment extends DialogFragment {

	public interface ChangeDurationListener {
		public void onChangeDuration(String hours, String minutes);
	}

	ChangeDurationListener mListener;
	private TextView hours;
	private TextView minutes;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View textEntryView = inflater.inflate(R.layout.dialog_duration,
				null);

		hours = (TextView) textEntryView
				.findViewById(R.id.duration_dialog_hours);
		hours.setFilters(new InputFilter[] { new InputFilterMinMax("0", "23") });
		minutes = (TextView) textEntryView
				.findViewById(R.id.duration_dialog_minutes);
		minutes.setFilters(new InputFilter[] { new InputFilterMinMax("0", "59") });
		builder.setView(textEntryView)
				.setPositiveButton(R.string.stop_clock,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								mListener = (ChangeDurationListener) getActivity();

								mListener.onChangeDuration(hours.getText()
										.toString(), minutes.getText()
										.toString());
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}
						});
		builder.setTitle("Pick A Stop Time");
		return builder.create();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the
			// host
			mListener = (ChangeDurationListener) activity;

		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement ChangeDurationListener");
		}
	}
}
