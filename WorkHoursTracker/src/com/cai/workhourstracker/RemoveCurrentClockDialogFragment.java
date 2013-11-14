package com.cai.workhourstracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class RemoveCurrentClockDialogFragment extends DialogFragment {

	public interface NoticeDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);

		public void onDialogNegativeClick(DialogFragment dialog);
	}
	
	public RemoveCurrentClockDialogFragment(){
		
	}

	// Use this instance of the interface to deliver action events
	NoticeDialogListener mListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the
			// host
			mListener = (NoticeDialogListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement NoticeDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Undo Clock in?")
				.setMessage(R.string.undo_clock_in)
				.setPositiveButton(R.string.undo_clock_in,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mListener
										.onDialogPositiveClick(RemoveCurrentClockDialogFragment.this);
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								RemoveCurrentClockDialogFragment.this
										.getDialog().cancel();
							}
						});
		// Create the AlertDialog object and return it
		return builder.create();
	}
}
