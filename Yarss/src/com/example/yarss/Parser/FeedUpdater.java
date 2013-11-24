package com.example.yarss.Parser;

import java.util.ArrayList;
import com.example.yarss.YarssApplication;
import android.database.Cursor;

public class FeedUpdater {
	public void updateAll(YarssApplication app){
		Cursor cursor = app.getFeedsDB().getFeedsCursor();
		RssParser parser = new RssParser();
		ArrayList<Item> result = new ArrayList<Item>();
		if(cursor.moveToFirst()){
			do{
				result.addAll(parser.parse(cursor.getString(cursor.getColumnIndex("feed_url"))));
			}while(cursor.moveToNext());
		}
		
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		
		if (result.size() > 0) {
			app.getItemsDB().addItems(result);
		}
	}
}
