package com.example.yarss;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import com.example.yarss.Adapters.FeedsAdapter;
import com.example.yarss.Database.FeedsDB;

public class FeedActivity extends FragmentActivity implements OnClickListener,
		LoaderCallbacks<Cursor>, OnItemLongClickListener {

	private AlertDialog addDialog;
	private EditText editAddresssEditText;
	private ListView feedsListView;
	private YarssApplication app;
	private FeedsAdapter feedsAdapter;
	private final static int FEED_LOADER = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feeds);

		feedsListView = (ListView) findViewById(R.id.feeds_list_view);

		app = ((YarssApplication) getApplication());
		editAddresssEditText = new EditText(this);

		AlertDialog.Builder _addDialog = new AlertDialog.Builder(this);
		_addDialog.setTitle("Add address");
		_addDialog.setPositiveButton("Add", this);
		_addDialog.setNegativeButton("Cancel", this);
		_addDialog.setView(editAddresssEditText);
		addDialog = _addDialog.create();

		feedsAdapter = new FeedsAdapter(this, null, false);
		feedsListView.setOnItemLongClickListener(this);
		feedsListView.setAdapter(feedsAdapter);
		getSupportLoaderManager().initLoader(FEED_LOADER, null, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.feed, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.btn_add_feeds) {
			addDialog.show();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		String addressEditText = editAddresssEditText.getText().toString();
		if (which == AlertDialog.BUTTON_POSITIVE) {
			try {
				app.getFeedsDB().addFeed(addressEditText);
				getSupportLoaderManager().restartLoader(0, null, this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new FeedsCursorLoader(this, app.getFeedsDB());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		feedsAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		feedsAdapter.swapCursor(null);
	}

	public static final class FeedsCursorLoader extends SimpleCursorLoader {
		private FeedsDB feeds;

		public FeedsCursorLoader(Context context, FeedsDB feeds) {
			super(context);
			this.feeds = feeds;
		}

		@Override
		public Cursor loadInBackground() {
			return feeds.getFeedsCursor();
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, final long id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Delete message");
		builder.setPositiveButton("ok", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				app.getFeedsDB().deleteFeed(id);
				getSupportLoaderManager().restartLoader(0, null, FeedActivity.this);
			}
		});

		builder.setNegativeButton("cancel", null);
		builder.create().show();

		return false;
	}
}
