package com.ha.halauncher.appmatrix;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class AppMatrixPager extends ViewPager {

	public AppMatrixPager(Context context, AttributeSet attrs) {
		super(context, attrs);

		// TODO Auto-generated constructor stub
	}

	public AppMatrixPager(Context context) {
		super(context);

		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// taking ownership of touch event
		return true;
	}

}
