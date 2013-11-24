package com.example.yarss;

import com.example.yarss.Database.DBHelper;
import com.example.yarss.Database.FeedsDB;
import com.example.yarss.Database.ItemsDB;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

public class YarssApplication extends Application {
	private SQLiteDatabase db = null;
	private FeedsDB feedsDb = null;
	private ItemsDB itemsDb = null;

	public SQLiteDatabase getDB() {
		if (db == null) {
			db = new DBHelper(getApplicationContext()).open();
		}

		return db;
	}

	public FeedsDB getFeedsDB() {
		if (feedsDb == null) {
			feedsDb = new FeedsDB(getDB());
		}

		return feedsDb;
	}

	public ItemsDB getItemsDB() {
		if (itemsDb == null) {
			itemsDb = new ItemsDB(getDB());
		}

		return itemsDb;
	}
}
