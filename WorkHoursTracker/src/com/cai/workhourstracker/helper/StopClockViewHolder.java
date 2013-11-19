package com.cai.workhourstracker.helper;

import com.cai.workhourstracker.R;

import android.view.View;
import android.widget.TextView;

public class StopClockViewHolder {
	private TextView comment;
	private TextView fullDate;
	private TextView id;
	private TextView moneyEarned;
	private TextView workHours;
	private TextView dayOfWeek;
	private TextView startHour;
	private TextView stopHour;

	public TextView getStartHour() {
		return startHour;
	}

	public void setStartHour(TextView startHour) {
		this.startHour = startHour;
	}

	public TextView getStopHours() {
		return stopHour;
	}

	public void setStopHours(TextView stopHours) {
		this.stopHour = stopHours;
	}
 

	public TextView getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(TextView dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public TextView getStopHour() {
		return stopHour;
	}

	public void setStopHour(TextView stopHour) {
		this.stopHour = stopHour;
	}

	public TextView getMoneyEarned() {
		return moneyEarned;
	}

	public void setMoneyEarned(TextView moneyEarned) {
		this.moneyEarned = moneyEarned;
	}

	public TextView getWorkHours() {
		return workHours;
	}

	public void setWorkHours(TextView workHours) {
		this.workHours = workHours;
	}

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

	public StopClockViewHolder(View view) {

		comment = (TextView) view.findViewById(R.id.comment);
		fullDate = (TextView) view.findViewById(R.id.full_date);
		id = (TextView) view.findViewById(R.id.entry_id);
		workHours = (TextView) view.findViewById(R.id.stop_start_clock_row_work_hours);
		moneyEarned = (TextView) view.findViewById(R.id.stop_start_clock_row_earned_money);
		dayOfWeek = (TextView) view.findViewById(R.id.start_stop_clock_row_day_of_week);
		startHour = (TextView) view.findViewById(R.id.stop_start_clock_row_start_hour);
		stopHour = (TextView) view.findViewById(R.id.stop_start_clock_row_stop_hour);
	}
}
