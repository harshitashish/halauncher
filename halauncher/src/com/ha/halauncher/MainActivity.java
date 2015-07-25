package com.ha.halauncher;

import java.util.ArrayList;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.ImageButton;
import android.widget.Space;
import android.widget.Toast;

import com.ha.halauncher.home.HomeMatrixPager;
import com.ha.halauncher.home.HomeMatrixPagerAdapter;

public class MainActivity extends Activity {

	static final String TAG = "com.ha.halauncher.MainActivity";
	private int currentApiVersion;

	final int home_screen_flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
			| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

	

	AppWidgetManager mAppWidgetManager;
	AppWidgetHost mAppWidgetHost;

	TableLayout mainlayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		currentApiVersion = android.os.Build.VERSION.SDK_INT;
		setContentView(R.layout.home_screen);

		HomeMatrixPager pager = (HomeMatrixPager) findViewById(R.id.homepager);

		pager.setAdapter(new HomeMatrixPagerAdapter(this));

		mAppWidgetManager = AppWidgetManager.getInstance(this);
		mAppWidgetHost = new AppWidgetHost(this, R.id.APPWIDGET_HOST_ID);

		ImageButton home = (ImageButton) findViewById(R.id.launcher_home);
		TableLayout tl = (TableLayout) ((HomeMatrixPagerAdapter) pager
				.getAdapter()).getmCurrentView();
		if (tl != null)
			mainlayout = tl;
		

		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this,
						AllAppsActivity.class);
				// startActivity(intent);
				HomeMatrixPager pager = (HomeMatrixPager) findViewById(R.id.homepager);
				TableLayout tl = (TableLayout) ((HomeMatrixPagerAdapter) pager
						.getAdapter()).getmCurrentView();

				if (tl != null)
					mainlayout = tl;
				selectWidget();

			}
		});

		// This work only for android 4.4+
		if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
			getWindow().getDecorView().setSystemUiVisibility(home_screen_flags);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
			getWindow().getDecorView().setSystemUiVisibility(home_screen_flags);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	/**
	 * Launches the menu to select the widget. The selected widget will be on
	 * the result of the activity.
	 */
	void selectWidget() {
		int appWidgetId = this.mAppWidgetHost.allocateAppWidgetId();
		Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
		pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		addEmptyData(pickIntent);
		startActivityForResult(pickIntent, R.id.REQUEST_PICK_APPWIDGET);
	}

	/**
	 * This avoids a bug in the com.android.settings.AppWidgetPickActivity,
	 * which is used to select widgets. This just adds empty extras to the
	 * intent, avoiding the bug.
	 * 
	 * See more: http://code.google.com/p/android/issues/detail?id=4272
	 */
	void addEmptyData(Intent pickIntent) {
		ArrayList<AppWidgetProviderInfo> customInfo = new ArrayList<AppWidgetProviderInfo>();
		pickIntent.putParcelableArrayListExtra(
				AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo);
		ArrayList<Bundle> customExtras = new ArrayList<Bundle>();
		pickIntent.putParcelableArrayListExtra(
				AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras);
	}

	/**
	 * If the user has selected an widget, the result will be in the 'data' when
	 * this function is called.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == R.id.REQUEST_PICK_APPWIDGET) {
				configureWidget(data);
			} else if (requestCode == R.id.REQUEST_CREATE_APPWIDGET) {
				createWidget(data);
			}
		} else if (resultCode == RESULT_CANCELED && data != null) {
			int appWidgetId = data.getIntExtra(
					AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
			if (appWidgetId != -1) {
				mAppWidgetHost.deleteAppWidgetId(appWidgetId);
			}
		}
	}

	/**
	 * Checks if the widget needs any configuration. If it needs, launches the
	 * configuration activity.
	 */
	private void configureWidget(Intent data) {
		Bundle extras = data.getExtras();
		int appWidgetId = extras
				.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
		AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager
				.getAppWidgetInfo(appWidgetId);
		if (appWidgetInfo.configure != null) {
			Intent intent = new Intent(
					AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
			intent.setComponent(appWidgetInfo.configure);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			startActivityForResult(intent, R.id.REQUEST_CREATE_APPWIDGET);
		} else {
			createWidget(data);
		}
	}

	/**
	 * Creates the widget and adds to our view layout.
	 */
	public void createWidget(Intent data) {
		Bundle extras = data.getExtras();
		int appWidgetId = extras
				.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
		AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager
				.getAppWidgetInfo(appWidgetId);

		AppWidgetHostView hostView = mAppWidgetHost.createView(this,
				appWidgetId, appWidgetInfo);

		hostView.setAppWidget(appWidgetId, appWidgetInfo);

		ItemGrid ig = positionInLayout(appWidgetInfo.minWidth,
				appWidgetInfo.minHeight);

		// hostView.setMinimumHeight(appWidgetInfo);
		// hostView.setMinimumWidth(appWidgetInfo);

		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

		float lyHeight = mainlayout.getHeight()
				/ (displayMetrics.densityDpi / 160);
		float lyWidth = mainlayout.getWidth()
				/ (displayMetrics.densityDpi / 160);

		hostView.updateAppWidgetSize(null, appWidgetInfo.minWidth / 2,
				appWidgetInfo.minHeight / 2, 0, 0);
		hostView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Log.e("Ashish", "Host Long Clicked");
				((AppWidgetHostView) v).updateAppWidgetSize(null,
						v.getWidth() / 2, v.getHeight() / 2, v.getWidth() / 2,
						v.getHeight() / 2);
				return false;
			}
		});
		Space s = new Space(this);

		s.setLayoutParams(new LayoutParams(0, 0));

		Space s1 = new Space(this);

		s1.setLayoutParams(new LayoutParams(0, 0));
		
		mainlayout.addView(hostView);

		Log.i(TAG, "The widget size is: " + appWidgetInfo.minWidth + "*"
				+ appWidgetInfo.minHeight);
	}

	private ItemGrid positionInLayout(int width, int height) {
		ItemGrid ig = new ItemGrid();

		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

		float lyHeight = mainlayout.getHeight()
				/ (displayMetrics.densityDpi / 160);
		float lyWidth = mainlayout.getWidth()
				/ (displayMetrics.densityDpi / 160);

		Log.e("Ashish", "densithy :: " + displayMetrics.densityDpi / 160);

		ig.numRow = (int) Math.ceil((height / 3) / (lyHeight / 4));
		ig.numColumn = (int) Math.ceil((width / 3) / (lyWidth / 4));

		Log.e("Ashish", "lyHeight :: " + lyHeight + "  lyWidth :: " + lyWidth);

		Log.e("Ashish", "rows :: " + ig.numRow + "  columns :: " + ig.numColumn);

		return ig;
	}

	private class ItemGrid {

		int numRow;
		int numColumn;
		int startRow;
		int startColumn;

	}

	/**
	 * Registers the AppWidgetHost to listen for updates to any widgets this app
	 * has.
	 */
	@Override
	protected void onStart() {
		super.onStart();
		mAppWidgetHost.startListening();
	}

	/**
	 * Stop listen for updates for our widgets (saving battery).
	 */
	@Override
	protected void onStop() {
		super.onStop();
		mAppWidgetHost.stopListening();
	}

	/**
	 * Removes the widget displayed by this AppWidgetHostView.
	 */
	public void removeWidget(AppWidgetHostView hostView) {
		mAppWidgetHost.deleteAppWidgetId(hostView.getAppWidgetId());
		mainlayout.removeView(hostView);
	}

	/**
	 * Handles the menu.
	 */
	/*
	 * @Override public boolean onOptionsItemSelected(MenuItem item) {
	 * Log.i(TAG, "Menu selected: " + item.getTitle() + " / " + item.getItemId()
	 * + " / " + R.id.addWidget); switch (item.getItemId()) { case
	 * R.id.addWidget: selectWidget(); return true; case R.id.removeWidget:
	 * removeWidgetMenuSelected(); return false; } return
	 * super.onOptionsItemSelected(item); }
	 */

	/**
	 * Handle the 'Remove Widget' menu.
	 */
	public void removeWidgetMenuSelected() {
		int childCount = mainlayout.getChildCount();
		if (childCount > 1) {
			View view = mainlayout.getChildAt(childCount - 1);
			if (view instanceof AppWidgetHostView) {
				removeWidget((AppWidgetHostView) view);
				Toast.makeText(this, R.string.widget_removed_popup,
						Toast.LENGTH_SHORT).show();
				return;
			}
		}
		Toast.makeText(this, R.string.no_widgets_popup, Toast.LENGTH_SHORT)
				.show();
	}

}
