package com.example.yarss.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FeedsDB {
	private SQLiteDatabase db;
	private static final String TABLE_NAME = "feeds";
	public FeedsDB(SQLiteDatabase db){
		this.db = db;
	}
	
	public void addFeed(String url) throws Exception{
		if(url == null){
			throw new Exception("Url cannot be empty");
		}
		
		String trimedUrl = url.trim();
		ContentValues vals = new ContentValues();
		vals.put("feed_url", trimedUrl);
		db.insert(TABLE_NAME, null, vals);		
	}
	
	public Cursor getFeedsCursor(){
		Cursor cursor = db.query(TABLE_NAME, new String[]{"_id", "feed_url"}, null, null, null, null, null);
		
		return cursor;
	}
	
	public void deleteFeed(long id){
		db.delete(TABLE_NAME, "_id=?", new String[]{String.valueOf(id)});
	}
	
}
