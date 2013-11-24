package com.example.yarss.Adapters;

import com.example.yarss.R;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FeedsAdapter extends CursorAdapter {

	public FeedsAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context arg1, Cursor cursor) {
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		viewHolder.getFeedUrlTextView()
				.setText(cursor.getString(cursor.getColumnIndex("feed_url")));

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup root) {
		View view = LayoutInflater.from(context).inflate(R.layout.activity_feeds_list_view_row,
				root, false);
		ViewHolder viewHolder = new ViewHolder();
		TextView feedTextView = (TextView) view.findViewById(R.id.list_view_row_feeds_text_view);

		viewHolder.setFeedUrlTextView(feedTextView);
		view.setTag(viewHolder);
		
		return view;
	}

	private class ViewHolder {
		private TextView feedUrlTextView;

		public TextView getFeedUrlTextView() {
			return feedUrlTextView;
		}

		public void setFeedUrlTextView(TextView feedUrlTextView) {
			this.feedUrlTextView = feedUrlTextView;
		}
	}

}
