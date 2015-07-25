package com.ha.halauncher;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.ha.halauncher.appmatrix.AppMatrixPagerAdapter;

public class AllAppsActivity extends Activity {

	private ViewPager m_appMatrixPager;
	private AppMatrixPagerAdapter m_appMatrixPagerPagerAdapter;
	private boolean hastouchControl = true;
	
	Handler takeControl = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		setContentView(R.layout.activity_all_apps);

		// Instantiate a ViewPager and a PagerAdapter.
		m_appMatrixPager = (ViewPager) findViewById(R.id.pager);
		// m_appMatrixPager.setOffscreenPageLimit(12);
		m_appMatrixPagerPagerAdapter = new AppMatrixPagerAdapter(
			this);
		m_appMatrixPager.setAdapter(m_appMatrixPagerPagerAdapter);

		m_appMatrixPager.setOnTouchListener(new View.OnTouchListener() {

			private boolean moved;
			private float xdown;
			private float ydown;
			private float lastX;
			private float lastY;

			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {

				//Log.e("Ashish", "New OnTouch Pager ");
				if (!hastouchControl)
					return false;

				ViewGroup currentView = (ViewGroup) ((AppMatrixPagerAdapter) ((ViewPager) view)
						.getAdapter()).getmCurrentView();

				if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
					((GridView) currentView.getChildAt(0))
							.dispatchTouchEvent(motionEvent);
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
						((GridView) currentView.getChildAt(0))
								.dispatchTouchEvent(motionEvent);
						//Log.e("Ashish", "new if");
					}
				}
				if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
					if (!moved) {
						Log.e("Ashish", "Pager not moved OnClick");
						((GridView) currentView.getChildAt(0))
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

		m_appMatrixPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						invalidateOptionsMenu();
						// Log.e("Ashish", "onPageSelected..");
					}

					@Override
					public void onPageScrolled(int position,
							float positionOffset,
							int positionOffsetPixels) {
						// TODO Auto-generated method stub
						super.onPageScrolled(position, positionOffset,
								positionOffsetPixels);

					}

					@Override
					public void onPageScrollStateChanged(int state) {
						// TODO Auto-generated method stub
						super.onPageScrollStateChanged(state);
					}

				});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.all_apps, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
