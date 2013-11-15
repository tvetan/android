package com.cai.workhourstracker.dialogs;

import com.cai.workhourstracker.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DeleteEntriestConfirmDialog extends DialogFragment {

	public interface IDeleteEntriesConfirm {
		void onDeleteEntriesConfirmListener(String date);
	}

	public DeleteEntriestConfirmDialog() {
	}

	private String deleteDate;
	private IDeleteEntriesConfirm listener;

	public static DeleteEntriestConfirmDialog newInstance(String date) {
		DeleteEntriestConfirmDialog dialog = new DeleteEntriestConfirmDialog();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putString("date", date);
		dialog.setArguments(args);

		return dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		deleteDate = getArguments().getString("date");
		String message = "Are you sure you want to delete all entries for all jobs that are older than "
				+ deleteDate + "? This cannot be undone.";

		builder.setMessage(message)
				.setPositiveButton(R.string.delete,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								listener.onDeleteEntriesConfirmListener(deleteDate);
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}
						});
		// Create the AlertDialog object and return it
		builder.setTitle("Delete Time Entries?");
		return builder.create();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (IDeleteEntriesConfirm) activity;

		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement IDeleteEntriesConfirm");
		}
	}
}
