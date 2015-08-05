package com.ha.halauncher.home;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class HomeMatrixPager extends ViewPager {

	public HomeMatrixPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		// TODO Auto-generated constructor stub
	}
	
	private void init()
	{
		this.setOnTouchListener(new View.OnTouchListener() {

			private boolean moved;
			private float xdown;
			private float ydown;
			private float lastX;
			private float lastY;
			private boolean hastouchControl = true;

			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {

				// Log.e("Ashish", "New OnTouch Pager ");
				if (!hastouchControl )
					return false;

				ViewGroup currentView = (ViewGroup) ((HomeMatrixPagerAdapter) ((ViewPager) view)
						.getAdapter()).getmCurrentView();

				if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
					((TestLayout) currentView).dispatchTouchEvent(motionEvent);
					xdown = motionEvent.getX();
					ydown = motionEvent.getY();
					moved = false;
				}
				if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
					lastX = motionEvent.getX();
					lastY = motionEvent.getY();
					if (Math.abs(lastX - xdown) > 10.0
							&& Math.abs(lastY - ydown) > 10.0) {

						moved = true;
					} else {
						((TestLayout) currentView)
								.dispatchTouchEvent(motionEvent);
						// Log.e("Ashish", "new if");
					}
				}
				if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
					if (!moved) {
						Log.e("Ashish", "Pager not moved OnClick");
						((TestLayout) currentView)
								.dispatchTouchEvent(motionEvent);

					}

					moved = false;
					xdown = 0;
					ydown = 0;
					lastX = 0;
					lastY = 0;

				}

				return false;
			}
		});
	}

	public HomeMatrixPager(Context context) {
		super(context);
		init();

		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// taking ownership of touch event
		return true;
	}

}
