package com.cai.workhourstracker.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class EntryStartClockComparator implements Comparator<Entry> {

	@Override
	public int compare(Entry first, Entry second) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());

		Date firstDate;
		Date secondDate;
		try {
			firstDate = dateFormat.parse(first.getStartClock());
			secondDate = dateFormat.parse(second.getStartClock());

			int compareResult = firstDate.compareTo(secondDate);

			if (compareResult > 0) {
				return -1;
			} else if (compareResult < 0) {
				return 1;
			} else if (compareResult == 0) {
				return 0;
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}
}
