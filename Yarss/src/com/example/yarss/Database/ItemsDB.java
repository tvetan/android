package com.example.yarss.Database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yarss.Parser.Item;

public class ItemsDB {
	private SQLiteDatabase db;
	private static final String TABLE_NAME = "items";

	public ItemsDB(SQLiteDatabase db) {
		this.db = db;
	}

	public void addItems(ArrayList<Item> items) {
		if (items != null && items.size() > 0) {
			for (Item item : items) {
				ContentValues vals = new ContentValues();
				vals.put("title", item.getTitle());
				vals.put("description", item.getDescription());
				vals.put("link", item.getLink());
				vals.put("guid", item.getGuid());
				vals.put("date_time", item.getDate());
				db.insert(TABLE_NAME, null, vals);
			}
		}
	}

	public Cursor getItemsCursor(){
		return db.query(TABLE_NAME, new String[]{"_id,title,description,link"}, null, null, null, null, null);			
	}
}
