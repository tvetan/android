package com.cai.workhourstracker.dialogs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.cai.workhourstracker.R;
import com.cai.workhourstracker.R.id;
import com.cai.workhourstracker.R.layout;
import com.cai.workhourstracker.R.string;
import com.cai.workhourstracker.fragments.WorkProgressFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

public class TimerPickerFragmentWithCancel extends DialogFragment {

	public interface ChangeStartTimeListener {
		public void onChangeStartTime(String time);
	}

	public TimerPickerFragmentWithCancel() {
	}

	public static TimerPickerFragmentWithCancel newInstance(Integer hours, Integer minutes) {

		TimerPickerFragmentWithCancel dialog = new TimerPickerFragmentWithCancel();
		Bundle args = new Bundle();
		args.putInt("hours", hours);
		args.putInt("minutes", minutes);
		dialog.setArguments(args);

		return dialog;
	}

	private TimePicker timePicker;
	private ChangeStartTimeListener mListener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		final View textEntryView = inflater.inflate(R.layout.time_picker_layout, null);
		timePicker = (TimePicker) textEntryView.findViewById(R.id.timePicker1);
		Bundle bundle = getArguments();
		int hours = bundle.getInt("hours");
		int minutes = bundle.getInt("minutes");
		
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(hours);
		timePicker.setCurrentMinute(minutes);
		

		builder.setView(textEntryView).setPositiveButton(R.string.stop_clock,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						mListener = (ChangeStartTimeListener) getActivity();
						
						int hours = timePicker.getCurrentHour();
						int minutes = timePicker.getCurrentMinute();
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
								Locale.getDefault());
						Calendar cal = Calendar.getInstance();
						cal.set(Calendar.HOUR_OF_DAY, hours);
						cal.set(Calendar.MINUTE, minutes);
						Date date = cal.getTime();
						mListener.onChangeStartTime(dateFormat.format(date));
					}
				}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
			mListener = (ChangeStartTimeListener) activity;

		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement NoticeDialogListener");
		}
	}
}
