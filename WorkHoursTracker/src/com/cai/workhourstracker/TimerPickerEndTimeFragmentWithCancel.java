package com.cai.workhourstracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

public class TimerPickerEndTimeFragmentWithCancel extends DialogFragment {

	public interface ChangeEndTimeListener {
		public void onChangeEndTime(String time);
	}

	TimePicker timePicker;
	ChangeEndTimeListener mListener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View textEntryView = inflater.inflate(
				R.layout.time_picker_layout, null);

		builder.setView(textEntryView)
				.setPositiveButton(R.string.stop_clock,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								mListener = (ChangeEndTimeListener) getActivity();
								timePicker = (TimePicker) textEntryView
										.findViewById(R.id.timePicker1);
								int hours = timePicker.getCurrentHour();
								int minutes = timePicker.getCurrentMinute();
								SimpleDateFormat dateFormat = new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss", Locale
												.getDefault());
								Calendar cal = Calendar.getInstance();
								cal.add(Calendar.HOUR, hours);
								cal.add(Calendar.MINUTE, minutes);
								Date date = cal.getTime();
								mListener.onChangeEndTime(dateFormat.format(date));
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
			mListener = (ChangeEndTimeListener) activity;

		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement NoticeDialogListener");
		}
	}
}
