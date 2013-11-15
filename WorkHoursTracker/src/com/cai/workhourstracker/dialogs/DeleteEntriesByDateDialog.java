package com.cai.workhourstracker.dialogs;

import java.util.Calendar;
import java.util.Date;
import com.cai.workhourstracker.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

public class DeleteEntriesByDateDialog extends DialogFragment {

	public interface IDeleteEntriesListener {
		void onDeleteEntriesListener(Date date);
	}

	public DeleteEntriesByDateDialog() {

	}

	public static DeleteEntriesByDateDialog newInstance() {
		return new DeleteEntriesByDateDialog();
	}

	private DatePicker datePicker;
	private IDeleteEntriesListener listener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View deleteEntriesView = inflater.inflate(
				R.layout.dialog_delete_entries, null);

		datePicker = (DatePicker) deleteEntriesView
				.findViewById(R.id.delete_entries_datePicker);

		builder.setView(deleteEntriesView)
				.setPositiveButton("Delete",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								int day = datePicker.getDayOfMonth();
								int month = datePicker.getMonth();
								Calendar calendar = Calendar.getInstance();
								calendar.set(Calendar.MONTH, month);
								calendar.set(Calendar.DATE, day);
								Date date = calendar.getTime();
								listener.onDeleteEntriesListener(date);

							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}
						});
		return builder.create();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (IDeleteEntriesListener) activity;

		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement IDeleteEntriesListener");
		}
	}
}
