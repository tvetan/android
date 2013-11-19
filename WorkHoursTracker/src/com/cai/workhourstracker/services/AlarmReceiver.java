package com.cai.workhourstracker.services;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	private static final String DEBUG_TAG = "AlarmReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(DEBUG_TAG, "Recurring alarm; requesting download service.");
		// start the download
		// Intent downloader = new Intent(context,
		// TutListDownloaderService.class);
		// downloader.setData(Uri
		// .parse("http://feeds.feedburner.com/MobileTuts?format=xml"));
		// context.startService(downloader);
	}

//	private void setRecurringAlarm(Context context) {
//
//		Calendar updateTime = Calendar.getInstance();
//		updateTime.setTimeZone(TimeZone.getTimeZone("GMT"));
//		updateTime.set(Calendar.HOUR_OF_DAY, 11);
//		updateTime.set(Calendar.MINUTE, 45);
//		Intent downloader = new Intent(context, AlarmReceiver.class);
//		PendingIntent recurringDownload = PendingIntent.getBroadcast(context, 0, downloader,
//				PendingIntent.FLAG_CANCEL_CURRENT);
//		AlarmManager alarms = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
//		alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, updateTime.getTimeInMillis(),
//				AlarmManager.INTERVAL_DAY, recurringDownload);
//	}
}