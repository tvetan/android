package com.cai.workhourstracker.helper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.cai.workhourstracker.model.Entry;
import com.cai.workhourstracker.model.Job;
import com.cai.workhourstracker.model.Tag;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Logcat tag
	private static final String LOG = "DatabaseHelper";

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "workHoursTrackerDb";

	// Table Names
	private static final String TABLE_JOB = "jobs";
	private static final String TABLE_TAG = "tags";
	private static final String TABLE_JOBS_TAG = "jobs_tags";
	private static final String TABLE_ENTRIES = "entries";
	private static final String TABLE_PAY_PERIODS = "payPeriods";

	// Common column names
	private static final String KEY_ID = "id";
	private static final String KEY_CREATED_AT = "created_at";

	// NOTES Table - column names
	private static final String KEY_JOB_NAME = "name";

	// TAGS Table - column names
	private static final String KEY_TAG_NAME = "name";

	// NOTE_TAGS Table - column names
	private static final String KEY_JOB_ID = "job_id";
	private static final String KEY_TAG_ID = "tag_id";

	// TABLE_ENTRIES Table - column names

	// Tag table create statement
	private static final String CREATE_TABLE_TAG = "CREATE TABLE " + TABLE_TAG
			+ "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TAG_NAME + " TEXT,"
			+ KEY_CREATED_AT + " DATETIME" + ")";

	// Table Create Statements
	// Job table create statement
	private static final String CREATE_TABLE_JOB = "CREATE TABLE " + TABLE_JOB
			+ "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_JOB_NAME + " TEXT,"
			+ KEY_CREATED_AT + " DATETIME" + ")";

	// todo_tag table create statement
	private static final String CREATE_TABLE_JOBS_TAG = "CREATE TABLE "
			+ TABLE_JOBS_TAG + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_JOB_ID + " INTEGER," + KEY_TAG_ID + " INTEGER,"
			+ KEY_CREATED_AT + " DATETIME" + ")";

	private static final String EARNED_MONEY = "earned_money";
	private static final String COMMENT = "comment";
	private static final String START_CLOCK = "start_clock";
	private static final String STOP_CLOCK = "stop_clock";
	private static final String JOB_ID = "job_id";

	// todo_tag table create statement
	private static final String CREATE_TABLE_ENTRIES = "CREATE TABLE "
			+ TABLE_ENTRIES + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
			+ EARNED_MONEY + " INTEGER," + COMMENT + " TEXT," + START_CLOCK
			+ " DATETIME," + STOP_CLOCK + " DATETIME," + JOB_ID + " INTEGER,"
			+ "FOREIGN KEY(job_id) REFERENCES jobs(id)" + ")";

	private static final String CREATE_TEST = "create table test ( id INTEGER PRIMARY KEY, age INTEGER)";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// creating required tables
		db.execSQL(CREATE_TABLE_JOB);
		db.execSQL(CREATE_TABLE_TAG);
		db.execSQL(CREATE_TABLE_JOBS_TAG);
		db.execSQL(CREATE_TABLE_ENTRIES);
		db.execSQL(CREATE_TEST);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOB);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOBS_TAG);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ENTRIES);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TEST);

		// create new tables
		onCreate(db);
	}

	/**
	 * Creating a entry
	 */

	public long createEntry(Entry entry) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(JOB_ID, entry.getJobId());
		values.put(COMMENT, entry.getComment());
		values.put(START_CLOCK, entry.getStartClock().toString());
		values.put(STOP_CLOCK, entry.getStopClock().toString());
		values.put(EARNED_MONEY, entry.getEarned_money().toString());

		// insert row
		long entry_id = db.insert(TABLE_ENTRIES, null, values);

		return entry_id;
	}

	public Entry getEntryById(long id) {
		String selectQuery = "SELECT * FROM " + TABLE_ENTRIES + " WHERE id == "
				+ id;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		Entry t = null;
		if (c.moveToFirst()) {

			t = new Entry();
			t.setId(c.getInt((c.getColumnIndex(KEY_ID))));
			t.setComment(c.getString(c.getColumnIndex(COMMENT)));
			t.setStopClock(c.getString(c.getColumnIndex(STOP_CLOCK)));
			t.setStartClock(c.getString(c.getColumnIndex(START_CLOCK)));
			// t.setEarned_money(c.get(c.getColumnIndex(EARNED_MONEY)));
			// t.setName(c.getString(c.getColumnIndex()));

			// adding to tags list
		}
		return t;
	}

	public Job getJobById(long id) {
		String selectQuery = "SELECT * FROM " + TABLE_JOB + " WHERE id == "
				+ id;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		Job job = null;
		if (c.moveToFirst()) {

			job.setId(c.getInt((c.getColumnIndex(KEY_ID))));
			job.setName((c.getString(c.getColumnIndex(KEY_JOB_NAME))));
			job.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

			// adding to tags list
		}
		return job;
	}

	public List<Entry> getAllEntries() {
		List<Entry> tags = new ArrayList<Entry>();
		String selectQuery = "SELECT * FROM " + TABLE_ENTRIES;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Entry t = new Entry();
				t.setId(c.getInt((c.getColumnIndex(KEY_ID))));
				t.setComment(c.getString(c.getColumnIndex(COMMENT)));
				t.setStopClock(c.getString(c.getColumnIndex(STOP_CLOCK)));
				t.setStartClock(c.getString(c.getColumnIndex(START_CLOCK)));
				// t.setEarned_money(c.get(c.getColumnIndex(EARNED_MONEY)));
				// t.setName(c.getString(c.getColumnIndex()));

				// adding to tags list
				tags.add(t);
			} while (c.moveToNext());
		}
		return tags;
	}

	/**
	 * Creating a todo
	 */
	public long createJob(Job job, long[] tag_ids) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_JOB_NAME, job.getName());

		values.put(KEY_CREATED_AT, getDateTime());

		// insert row
		long job_id = db.insert(TABLE_JOB, null, values);

		// insert tag_ids
		for (long tag_id : tag_ids) {
			createJobTag(job_id, tag_id);
		}

		return job_id;
	}

	public long createJobTag(long todo_id, long tag_id) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_JOB_ID, todo_id);
		values.put(KEY_TAG_ID, tag_id);
		values.put(KEY_CREATED_AT, getDateTime());

		long id = db.insert(TABLE_JOBS_TAG, null, values);

		return id;
	}

	public List<Job> getAllToDos() {
		List<Job> todos = new ArrayList<Job>();
		String selectQuery = "SELECT  * FROM " + TABLE_JOB;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Job td = new Job();
				td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
				td.setName((c.getString(c.getColumnIndex(KEY_JOB_NAME))));
				td.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

				// adding to todo list
				todos.add(td);
			} while (c.moveToNext());
		}

		return todos;
	}

	public int getToDoCount() {
		String countQuery = "SELECT  * FROM " + TABLE_JOB;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

	/**
	 * Creating tag
	 */
	public long createTag(Tag tag) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TAG_NAME, tag.getName());
		values.put(KEY_CREATED_AT, getDateTime());

		// insert row
		long tag_id = db.insert(TABLE_TAG, null, values);

		return tag_id;
	}

	/**
	 * getting all tags
	 * */
	public List<Tag> getAllTags() {
		List<Tag> tags = new ArrayList<Tag>();
		String selectQuery = "SELECT  * FROM " + TABLE_TAG;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Tag t = new Tag();
				t.setId(c.getInt((c.getColumnIndex(KEY_ID))));
				t.setName(c.getString(c.getColumnIndex(KEY_TAG_NAME)));

				// adding to tags list
				tags.add(t);
			} while (c.moveToNext());
		}
		return tags;
	}

	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}
}
