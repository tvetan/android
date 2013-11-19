package com.cai.workhourstracker.Views;

import com.cai.workhourstracker.adapters.GroupViewAdapter;
import com.cai.workhourstracker.adapters.GroupViewAdapter.HasMorePagesListener;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

public class GroupListView extends ListView implements HasMorePagesListener {

	public GroupListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public GroupListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public GroupListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	View listFooter;
	boolean footerViewAttached = false;

	private View mHeaderView;
	private boolean mHeaderViewVisible;

	private int mHeaderViewWidth;
	private int mHeaderViewHeight;

	private GroupViewAdapter adapter;

	public void setPinnedHeaderView(View view) {
		mHeaderView = view;

		// Disable vertical fading when the pinned header is present
		// TODO change ListView to allow separate measures for top and bottom
		// fading edge;
		// in this particular case we would like to disable the top, but not the
		// bottom edge.
		if (mHeaderView != null) {
			setFadingEdgeLength(0);
		}
		requestLayout();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mHeaderView != null) {
			measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
			mHeaderViewWidth = mHeaderView.getMeasuredWidth();
			mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (mHeaderView != null) {
			mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			configureHeaderView(getFirstVisiblePosition());
		}
	}

	public void configureHeaderView(int position) {
		if (mHeaderView == null) {
			return;
		}
		
	//	if(adapter == null) return;
	//	Integer state = adapter.getPinnedHeaderState(position);
//		switch (state) {
//		case GroupViewAdapter.PINNED_HEADER_GONE: {
//			mHeaderViewVisible = false;
//			break;
//		}
//
//		case GroupViewAdapter.PINNED_HEADER_VISIBLE: {
//			adapter.configurePinnedHeader(mHeaderView, position, 255);
//			if (mHeaderView.getTop() != 0) {
//				mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
//			}
//			mHeaderViewVisible = true;
//			break;
//		}
//
//		case GroupViewAdapter.PINNED_HEADER_PUSHED_UP: {
//			View firstView = getChildAt(0);
//			if (firstView != null) {
//				int bottom = firstView.getBottom();
//				int headerHeight = mHeaderView.getHeight();
//				int y;
//				int alpha;
//				if (bottom < headerHeight) {
//					y = (bottom - headerHeight);
//					alpha = 255 * (headerHeight + y) / headerHeight;
//				} else {
//					y = 0;
//					alpha = 255;
//				}
//				adapter.configurePinnedHeader(mHeaderView, position, alpha);
//				if (mHeaderView.getTop() != y) {
//					mHeaderView.layout(0, y, mHeaderViewWidth,
//							mHeaderViewHeight + y);
//				}
//				mHeaderViewVisible = true;
//			}
//			break;
//		}
//		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mHeaderViewVisible) {
			drawChild(canvas, mHeaderView, getDrawingTime());
		}
	}

	public void setLoadingView(View listFooter) {
		this.listFooter = listFooter;
	}

	public View getLoadingView() {
		return listFooter;
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		if (!(adapter instanceof GroupViewAdapter)) {
			throw new IllegalArgumentException(
					GroupListView.class.getSimpleName()
							+ " must use adapter of type "
							+ GroupViewAdapter.class.getSimpleName());
		}

		// previous adapter
		if (this.adapter != null) {
			this.adapter.setHasMorePagesListener(null);
			this.setOnScrollListener(null);
		}

		this.adapter = (GroupViewAdapter) adapter;
		((GroupViewAdapter) adapter).setHasMorePagesListener(this);
		this.setOnScrollListener((GroupViewAdapter) adapter);

		View dummy = new View(getContext());
		super.addFooterView(dummy);
		super.setAdapter(adapter);
		super.removeFooterView(dummy);
	}

	@Override
	public GroupViewAdapter getAdapter() {
		return adapter;
	}

	@Override
	public void noMorePages() {
		if (listFooter != null) {
			this.removeFooterView(listFooter);
		}
		footerViewAttached = false;
	}

	@Override
	public void mayHaveMorePages() {
		if (!footerViewAttached && listFooter != null) {
			this.addFooterView(listFooter);
			footerViewAttached = true;
		}
	}

	public boolean isLoadingViewVisible() {
		return footerViewAttached;
	}
}
