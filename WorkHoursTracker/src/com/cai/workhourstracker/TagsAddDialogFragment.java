package com.cai.workhourstracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.cai.workhourstracker.adapters.TagsArrayAdapter;
import com.cai.workhourstracker.model.TagRow;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class TagsAddDialogFragment extends DialogFragment {

	public interface TagsChangeListener {
		public void onTagsChangeTime(String time);
	}

	TagsChangeListener mListener;
	private ImageView addTagButton;
	private TextView tagName;
	private ListView tags;

	private List<TagRow> tagRows;
	
	
	static TagsAddDialogFragment newInstance() {
		TagsAddDialogFragment f = new TagsAddDialogFragment();

        // Supply num input as an argument.
       // Bundle args = new Bundle();
       // args.putInt("num", num);
       // f.setArguments(args);

        return f;
    }
	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View view = inflater.inflate(R.layout.dialog_tags, null);
		tagName = (TextView) view.findViewById(R.id.tag_name_dialog);

		tags = (ListView) view.findViewById(R.id.tags_dialog);
		tagRows = new ArrayList<TagRow>();
		tagRows.add(new TagRow("test", true));
		tagRows.add(new TagRow("tes1", false));
		tagRows.add(new TagRow("tes3", true));

		TagsArrayAdapter adapter = new TagsArrayAdapter(getActivity(),
				tagRows.toArray(new TagRow[tagRows.size()]));
		tags.setAdapter(adapter);

		addTagButton = (ImageView) view.findViewById(R.id.add_tag_dialog);
		addTagButton.setClickable(true);
		addTagButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String name =  tagName.getText().toString();
				tagName.setText("");
				if (name == null || name.isEmpty()) {
					Toast.makeText(getActivity(),
							"No tag name", Toast.LENGTH_SHORT)
							.show();
				}else{
					TagRow addedRow = new TagRow(name, false);
					tagRows.add(addedRow);
					TagsArrayAdapter newAdapter = new TagsArrayAdapter(getActivity(),
							tagRows.toArray(new TagRow[tagRows.size()]));
					tags.setAdapter(newAdapter);
				}
			}
		});

		builder.setView(view)
				.setPositiveButton(R.string.stop_clock,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								
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
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the
			// host
			mListener = (TagsChangeListener) activity;

		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement TagsChangeListener");
		}
	}
}
