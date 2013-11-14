package com.cai.workhourstracker.adapters;

import java.util.List;

import com.cai.workhourstracker.R;
import com.cai.workhourstracker.model.TagRow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class TagsArrayAdapter extends ArrayAdapter<TagRow> {
	private final Context context;
	private final TagRow[] values;

	public TagsArrayAdapter(Context context, TagRow[] values) {
		super(context, R.layout.dialog_tags_row, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater
				.inflate(R.layout.dialog_tags_row, parent, false);

		TextView textView = (TextView) rowView
				.findViewById(R.id.dialog_tag_row_tag_name);
		CheckBox checkBoxView = (CheckBox) rowView
				.findViewById(R.id.dialog_tag_row_checkbox);

		textView.setText(values[position].getName());
		checkBoxView.setChecked(values[position].isChecked());
		
		return rowView;
	}
}