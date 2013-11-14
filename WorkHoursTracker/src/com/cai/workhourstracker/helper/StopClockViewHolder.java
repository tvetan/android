package com.cai.workhourstracker.helper;

import com.cai.workhourstracker.R;

import android.view.View;
import android.widget.TextView;

public class StopClockViewHolder {
	private TextView comment;
	private TextView fullDate;
	private TextView id;

	public TextView getId() {
		return id;
	}

	public void setId(TextView id) {
		this.id = id;
	}

	public void setFullDate(TextView fullDate) {
		this.fullDate = fullDate;
	}

	public TextView getComment() {
		return comment;
	}

	public void setComment(TextView comment) {
		this.comment = comment;
	}

	public TextView getFullDate() {
		return fullDate;
	}

	public void getFullDate(TextView comment) {
		this.fullDate = comment;
	}

	public StopClockViewHolder(View v) {

		comment = (TextView) v.findViewById(R.id.comment);
		fullDate = (TextView) v.findViewById(R.id.full_date);
		id = (TextView) v.findViewById(R.id.entry_id);
	}
}
