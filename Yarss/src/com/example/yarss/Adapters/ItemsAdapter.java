package com.example.yarss.Adapters;

import com.example.yarss.R;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ItemsAdapter extends CursorAdapter {

	public ItemsAdapter(Context context, Cursor c) {
		super(context, c, false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		viewHolder.getTitle().setText(cursor.getString(cursor.getColumnIndex("title")));
		viewHolder.getDescription().setText(cursor.getString(cursor.getColumnIndex("description")));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup root) {
		View view = LayoutInflater.from(context).inflate(R.layout.activity_main_list_view_row,
				root, false);
		ViewHolder viewHolder = new ViewHolder();
		TextView titleTextView = (TextView) view
				.findViewById(R.id.text_view_title_list_view_row_main);
		TextView descriptionTextView = (TextView) view
				.findViewById(R.id.text_view_description_list_view_row_main);

		viewHolder.setDescription(descriptionTextView);
		viewHolder.setTitle(titleTextView);
		view.setTag(viewHolder);

		return view;
	}

	private class ViewHolder {
		TextView title;
		TextView description;

		public TextView getTitle() {
			return title;
		}

		public void setTitle(TextView title) {
			this.title = title;
		}

		public TextView getDescription() {
			return description;
		}

		public void setDescription(TextView description) {
			this.description = description;
		}
	}
}
