package com.cai.workhourstracker.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.cai.workhourstracker.R;
import com.cai.workhourstracker.R.id;
import com.cai.workhourstracker.R.layout;
import com.cai.workhourstracker.R.string;
import com.cai.workhourstracker.adapters.TagsArrayAdapter;
import com.cai.workhourstracker.model.TagRow;

import Utils.ToastUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class TagsAddDialogFragment extends DialogFragment implements
		AdapterView.OnItemClickListener {

	public interface TagsChangeListener {
		public void onTagsChangeTime(String time);
	}

	TagsChangeListener mListener;
	private ImageView addTagButton;
	private TextView tagName;
	private ListView tags;
	private List<TagRow> tagRows;
	private boolean[] checkedTags;
	private TagsArrayAdapter adapter;

	public static TagsAddDialogFragment newInstance() {
		TagsAddDialogFragment f = new TagsAddDialogFragment();

		return f;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View view = inflater.inflate(R.layout.dialog_fragment_tags, null);
		tagName = (TextView) view.findViewById(R.id.tag_name_dialog);

		tags = (ListView) view.findViewById(R.id.tags_dialog);
		tagRows = new ArrayList<TagRow>();
		tagRows.add(new TagRow("test", true));
		tagRows.add(new TagRow("tes1", false));
		tagRows.add(new TagRow("tes3", true));

		adapter = new TagsArrayAdapter(getActivity(), tagRows.toArray(new TagRow[tagRows.size()]));
		tags.setOnItemClickListener(this);
		tags.setAdapter(adapter);

		addTagButton = (ImageView) view.findViewById(R.id.add_tag_dialog);
		addTagButton.setClickable(true);
		addTagButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = tagName.getText().toString();
				tagName.setText("");
				if (name == null || name.isEmpty()) {
					Toast.makeText(getActivity(), "No tag name", Toast.LENGTH_SHORT).show();
				} else {
					TagRow addedRow = new TagRow(name, false);
					tagRows.add(addedRow);
					adapter = new TagsArrayAdapter(getActivity(), tagRows
							.toArray(new TagRow[tagRows.size()]));
					tags.setAdapter(adapter);
				}
			}
		});

		builder.setView(view).setPositiveButton(R.string.stop_clock,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

					}
				}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
			throw new ClassCastException(activity.toString() + " must implement TagsChangeListener");
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		TagRow tagRow = tagRows.get(position);
		ToastUtils.makeShortToast(getActivity(), String.valueOf(tagRow.isChecked()));
		if (tagRow.isChecked()) {
			tagRow.setChecked(false);
		} else {
			tagRow.setChecked(true);
		}

		tags.setAdapter(adapter);

	}
}
