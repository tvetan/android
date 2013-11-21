package com.cai.workhourstracker.adapters;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import Utils.DateCalculateUtils;
import Utils.DateFormatUtils;
import Utils.MoneyFormatUtils;
import Utils.Utils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.cai.workhourstracker.R;
import com.cai.workhourstracker.helper.MoneyCalculateUtils;
import com.cai.workhourstracker.helper.StopClockViewHolder;
import com.cai.workhourstracker.model.Entry;

public class EntriesListAdapter extends ArrayAdapter<Entry> {

	private Context context;
	private Entry[] entries;

	public EntriesListAdapter(Context c, Entry[] entries) {
		super(c, R.layout.stop_clock_list_row, entries);
		// super(c, R.layout.stop_clock_list_row, R.id.textView1, entries);
		context = c;
		this.entries = entries;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		StopClockViewHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.stop_clock_list_row, parent, false);
			holder = new StopClockViewHolder(row);
			row.setTag(holder);
		} else {
			holder = (StopClockViewHolder) row.getTag();
		}

		String monthDateYearDate = Utils.dateToMonthDateYearFormat(entries[position]
				.getStartClock());
		String dayOfWeek = Utils.dateToDayOfWeek(entries[position].getStartClock());
		String startHour = Utils.dateToHour(entries[position].getStartClock());
		String stopHour = Utils.dateToHour(entries[position].getStopClock());

		holder.getComment().setText(entries[position].getComment());
		holder.getFullDate().setText(monthDateYearDate);
		holder.getDayOfWeek().setText(dayOfWeek);
		holder.getStartHour().setText(startHour);
		holder.getStopHours().setText(stopHour);
		holder.getId().setText(String.valueOf(entries[position].getId()));

		Date startClock = DateFormatUtils.fromDatabaseFormatToDate(entries[position]
				.getStartClock());
		Date endClock = DateFormatUtils.fromDatabaseFormatToDate(entries[position].getStopClock());
		double workHours = DateCalculateUtils.differenceBetweenTwoDatesInMinutes(startClock,
				endClock);
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMinimumFractionDigits(2);
		format.setMaximumFractionDigits(2);

		holder.getWorkHours().setText(format.format(workHours) + "h");
		BigDecimal moneyEarned = MoneyCalculateUtils.moneyEarned(workHours, entries[position]
				.getBaseRate());

		holder.getMoneyEarned().setText(
				MoneyFormatUtils.toLocaleCurrencyFormatFromBigDecimal(moneyEarned));

		// holder.getMoneyEarned().setText(
		// Utils.convertMoneyToString(entries[position].getEarned_money()));
		// holder.getWorkHours().setText(
		// String.valueOf(Utils.differenceBetweenStartAndStop(entries[position]))
		// + "h");

		return row;
	}
}
