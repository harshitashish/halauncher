package com.ha.halauncher;

import java.util.ArrayList;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Space;
import android.widget.Toast;

import com.ha.halauncher.home.DefaultLayout;
import com.ha.halauncher.home.HomeMatrixPager;
import com.ha.halauncher.home.HomeMatrixPagerAdapter;
import com.ha.halauncher.model.DBModel;
import com.ha.halauncher.model.Widget;

/*
 *   It is the main activity 
 * 
 * 
 * */

public class MainActivity extends Activity {

	static final String TAG = "com.ha.halauncher.MainActivity";
	private int currentApiVersion;

	final int home_screen_flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
			| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

	

	AppWidgetManager mAppWidgetManager;
	AppWidgetHost mAppWidgetHost;

	DefaultLayout mainlayout;
	
	DBModel db;
	private int appWidgetId;
	private AppWidgetHostView hostView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Log.e(TAG, "OnCreatre");
		
		db = new DBModel(this);


		super.onCreate(savedInstanceState);
		currentApiVersion = android.os.Build.VERSION.SDK_INT;
		setContentView(R.layout.home_screen);

		HomeMatrixPager pager = (HomeMatrixPager) findViewById(R.id.homepager);

		pager.setAdapter(new HomeMatrixPagerAdapter(this));

		mAppWidgetManager = AppWidgetManager.getInstance(this);
		mAppWidgetHost = new AppWidgetHost(this, R.id.APPWIDGET_HOST_ID);

		ImageButton home = (ImageButton) findViewById(R.id.launcher_home);
		ImageButton widgetLaunch = (ImageButton) findViewById(R.id.widgetLaunch);
		
		
		DefaultLayout tl =(DefaultLayout) ((HomeMatrixPagerAdapter) pager
				.getAdapter()).getmCurrentView();
		if (tl != null)
			mainlayout = tl;
		
		widgetLaunch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this,
						AllAppsActivity.class);
				// startActivity(intent);
				HomeMatrixPager pager = (HomeMatrixPager) findViewById(R.id.homepager);
				DefaultLayout tl = (DefaultLayout) ((HomeMatrixPagerAdapter) pager
						.getAdapter()).getmCurrentView();

				if (tl != null)
					mainlayout = tl;
				selectWidget();

			}
		});

		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this,
						AllAppsActivity.class);
				
				startActivity(intent);
				
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
		appWidgetId = extras
				.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
		AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager
				.getAppWidgetInfo(appWidgetId);

		 hostView = mAppWidgetHost.createView(this,
				appWidgetId, appWidgetInfo);

		hostView.setAppWidget(appWidgetId, appWidgetInfo);

	//	ItemGrid ig = positionInLayout(appWidgetInfo.minWidth,
	//			appWidgetInfo.minHeight);

		// hostView.setMinimumHeight(appWidgetInfo);
		// hostView.setMinimumWidth(appWidgetInfo);

	/*	DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

		float lyHeight = mainlayout.getHeight()
				/ (displayMetrics.densityDpi / 160);
		float lyWidth = mainlayout.getWidth()
				/ (displayMetrics.densityDpi / 160);
*/
		hostView.updateAppWidgetSize(null, appWidgetInfo.minWidth ,
				appWidgetInfo.minHeight, 0, 0);
		hostView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Log.e(TAG, "Host Long Clicked");
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
		
		
		//DefaultLayout.LayoutParams layoutParams = mainlayout.generateDefaultLayoutParams();
		
		/*layoutParams.row = 0;
		layoutParams.column = 0; 
		
		db.addWidget(new Widget(appWidgetId, layoutParams.row, layoutParams.column ));
		mainlayout.addView(hostView,layoutParams);*/
		
		InputDialoge d = new InputDialoge();
		d.setListener(new InputListener() {
			
			@Override
			public void onDialogPositiveClick(InputDialoge dialog) {
				// TODO Auto-generated method stub
				
				DefaultLayout.LayoutParams layoutParams = mainlayout.generateDefaultLayoutParams();
				
				//EditText r = dialog.findViewbyId()
				
				layoutParams.row = Integer.parseInt(((EditText)(dialog.getDialog()).findViewById(R.id.topx)).getEditableText().toString());
				layoutParams.column = Integer.parseInt(((EditText)(dialog.getDialog()).findViewById(R.id.topy)).getEditableText().toString());
				
				db.addWidget(new Widget(appWidgetId, layoutParams.row, layoutParams.column ));
				mainlayout.addView(hostView,layoutParams);
				
			}
			
			@Override
			public void onDialogNegativeClick(InputDialoge dialog) {
				// TODO Auto-generated method stub
				
			}
		});
		
        d.show(this.getFragmentManager(),"Input");
		//mainlayout.addView(hostView, new DefaultLayout.LayoutParams(this, ));
		Log.i(TAG, "The widget size is: " + appWidgetInfo.minWidth + "*"
				+ appWidgetInfo.minHeight);
	}
	
	
	
	/**
	 * Registers the AppWidgetHost to listen for updates to any widgets this app
	 * has.
	 */
	@Override
	protected void onStart() {
		super.onStart();
		Log.e(TAG, "OnStart");
		mAppWidgetHost.startListening();
	}

	/**
	 * Stop listen for updates for our widgets (saving battery).
	 */
	@Override
	protected void onStop() {
		Log.e(TAG, "OnStop");
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
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		
		Log.e(TAG," keydown");
	if(keyCode == KeyEvent.KEYCODE_HOME)
	{
	 Log.i("Home Button","Clicked");
	 //finish();
	}
	if(keyCode==KeyEvent.KEYCODE_BACK)
	{

	   // finish();
	 }
	   return false;
	};
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.e(TAG, "OnResume");
		super.onResume();
	}
	
	@Override
	protected void onPause(){
		Log.e(TAG, "OnPause");
		super.onPause();
		
		
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		Log.e(TAG, "onRestart");
		super.onRestart();
	}

	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		Log.e(TAG, "onPostResume");
		super.onPostResume();
		
		
		
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onNewIntent");
		super.onNewIntent(intent);
	}

	@Override
	protected void onUserLeaveHint() {
		// TODO Auto-generated method stub
		Log.e(TAG, "onUserLeaveHint");
		super.onUserLeaveHint();
	}

	@Override
	public void onVisibleBehindCanceled() {
		// TODO Auto-generated method stub
		Log.e(TAG, "onVisibleBehindCanceled");
		super.onVisibleBehindCanceled();
	}
	
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		
		savedInstanceState.putInt("ash", 1);
	    // Save the user's current game state
	    Log.e(TAG, "onSaveInstanceState");
	    super.onSaveInstanceState(savedInstanceState);
	}
	
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState){
	    // Save the user's current game state
	    Log.e(TAG, "onRestoreInstanceState");
	    super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState,
			PersistableBundle persistentState) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onCreate");
		super.onCreate(savedInstanceState, persistentState);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.e(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onContentChanged() {
		// TODO Auto-generated method stub
		Log.e(TAG, "onContentChanged");
		super.onContentChanged();
	}

	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		Log.e(TAG, "onAttachedToWindow");
		super.onAttachedToWindow();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	
	
}
