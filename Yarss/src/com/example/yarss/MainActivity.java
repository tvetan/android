package com.example.yarss;

import com.example.yarss.Adapters.ItemsAdapter;
import com.example.yarss.Database.ItemsDB;
import com.example.yarss.Parser.FeedUpdater;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends FragmentActivity implements LoaderCallbacks<Cursor>,
		OnItemClickListener {
	
	private YarssApplication app;
	private ListView mainListView;
	private final int LOADER_ID = 0;
	private ItemsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		app = ((YarssApplication) getApplication());
		mainListView = (ListView) findViewById(R.id.main_list_view);

		adapter = new ItemsAdapter(this, null);
		mainListView.setOnItemClickListener(this);
		mainListView.setAdapter(adapter);
		getSupportLoaderManager().initLoader(LOADER_ID, null, this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.btn_manage_feets) {
			startActivity(new Intent(MainActivity.this, FeedActivity.class));
			return true;
		} else if (item.getItemId() == R.id.btn_refresh_feeds) {
			UpdateFeeds update = new UpdateFeeds();
			update.execute();
		}
		return super.onOptionsItemSelected(item);
	}

	private class UpdateFeeds extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			FeedUpdater updater = new FeedUpdater();
			updater.updateAll(app);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			getSupportLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle bundle) {
		return new ItemsCursorLoader(getApplicationContext(), app.getItemsDB());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		adapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}

	public static final class ItemsCursorLoader extends SimpleCursorLoader {
		private ItemsDB items;

		public ItemsCursorLoader(Context context, ItemsDB items) {
			super(context);
			this.items = items;
		}

		@Override
		public Cursor loadInBackground() {
			return items.getItemsCursor();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		Cursor cursor = (Cursor) adapter.getItemAtPosition(position);
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(cursor.getString(cursor
				.getColumnIndex("link")))));

	}
}
