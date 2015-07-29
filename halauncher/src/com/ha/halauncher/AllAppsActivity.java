package com.ha.halauncher;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.GridView;

import com.ha.halauncher.appmatrix.AppMatrixPagerAdapter;

public class AllAppsActivity extends Activity {
	
	
	private final static String tag = "com.ha.halauncher.AllAppsActivity";

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
		
		m_appMatrixPager.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				Log.e(tag,"View onkey");
				return false;
			}
		});

		m_appMatrixPager.setOnTouchListener(new View.OnTouchListener() {

			private boolean moved;
			private float xdown;
			private float ydown;
			private float lastX;
			private float lastY;

			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {

				//Log.e(tag, "New OnTouch Pager ");
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
						//Log.e(tag, "new if");
					}
				}
				if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
					if (!moved) {
						Log.e(tag, "Pager not moved OnClick");
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
						// Log.e(tag, "onPageSelected..");
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
		
		Log.e(tag, "onOptionsItemSelected");
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
    protected void onStop() {
		Log.e(tag,"onStop called");
		 super.onStop();
		finish();

       
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.e(tag," keon");
	if(keyCode == KeyEvent.KEYCODE_HOME)
	{
	 Log.i("Home Button","Clicked");
	 finish();
	}
	if(keyCode==KeyEvent.KEYCODE_BACK)
	{

	    finish();
	 }
	   return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState,
			PersistableBundle persistentState) {
		// TODO Auto-generated method stub
		Log.e(tag,"OnCreate1");
		super.onCreate(savedInstanceState, persistentState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.e(tag, "onRestoreInstanceState");
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState,
			PersistableBundle persistentState) {
		// TODO Auto-generated method stub
		Log.e(tag, "onRestoreInstanceState1");
		super.onRestoreInstanceState(savedInstanceState, persistentState);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.e(tag, "onPostCreate");
		super.onPostCreate(savedInstanceState);
	}

	@Override
	public void onPostCreate(Bundle savedInstanceState,
			PersistableBundle persistentState) {
		// TODO Auto-generated method stub
		Log.e(tag, "onPostCreate1");
		super.onPostCreate(savedInstanceState, persistentState);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.e(tag, "onStart");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		Log.e(tag, "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.e(tag, "onResume");
		super.onResume();
	}

	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		Log.e(tag, "onPostResume");
		super.onPostResume();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.e(tag, "onNewIntent");
		super.onNewIntent(intent);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		Log.e(tag, "onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState,
			PersistableBundle outPersistentState) {
		// TODO Auto-generated method stub
		Log.e(tag, "onSaveInstanceState1");
		super.onSaveInstanceState(outState, outPersistentState);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.e(tag, "onPause");
		super.onPause();
		finish();
	}

	@Override
	protected void onUserLeaveHint() {
		// TODO Auto-generated method stub
		Log.e(tag, "onUserLeaveHint");
		super.onUserLeaveHint();
	}

	@Override
	public boolean onCreateThumbnail(Bitmap outBitmap, Canvas canvas) {
		// TODO Auto-generated method stub
		Log.e(tag, "onCreateThumbnail");
		return super.onCreateThumbnail(outBitmap, canvas);
	}

	@Override
	public CharSequence onCreateDescription() {
		// TODO Auto-generated method stub
		Log.e(tag, "onCreateDescription");
		return super.onCreateDescription();
	}

	@Override
	public void onProvideAssistData(Bundle data) {
		// TODO Auto-generated method stub
		super.onProvideAssistData(data);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.e(tag, "onDestroy");
		super.onDestroy();
	}
	
	public boolean onKey(View v, int keyCode, KeyEvent event) {
	    switch (keyCode) {
	        case KeyEvent.KEYCODE_ENTER:
	         /* This is a sample for handling the Enter button */
	      return true;
	    }
	    return false;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		// TODO Auto-generated method stub
		return super.onRetainNonConfigurationInstance();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		Log.e(tag, "onLowMemory");
		
		super.onLowMemory();
	}

	@Override
	public void onTrimMemory(int level) {
		// TODO Auto-generated method stub
		super.onTrimMemory(level);
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		// TODO Auto-generated method stub
		super.onAttachFragment(fragment);
	}
	
	

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.e(tag, "onKeyLongPress");
		return super.onKeyLongPress(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.e(tag, "onKeyUp");
		return super.onKeyUp(keyCode, event);
	}

	
	
	@Override
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.e(tag, "onKeyMultiple");
		return super.onKeyMultiple(keyCode, repeatCount, event);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Log.e(tag, "onBackPressed");
		super.onBackPressed();
	}

	@Override
	public boolean onKeyShortcut(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.e(tag, "onKeyShortcut");
		return super.onKeyShortcut(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTrackballEvent(event);
	}

	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onGenericMotionEvent(event);
	}

	@Override
	public void onUserInteraction() {
		// TODO Auto-generated method stub
		Log.e(tag,"onContentChanged");
		super.onUserInteraction();
	}

	@Override
	public void onWindowAttributesChanged(LayoutParams params) {
		// TODO Auto-generated method stub
		super.onWindowAttributesChanged(params);
	}

	@Override
	public void onContentChanged() {
		// TODO Auto-generated method stub
		Log.e(tag, "onContentChanged");
		super.onContentChanged();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		Log.e(tag, "onWindowFocusChanged");
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
        Log.e(tag,"onAttachedToWindow");
		super.onAttachedToWindow();
	}

	@Override
	public void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		 Log.e(tag,"onDeAttachedToWindow");
		super.onDetachedFromWindow();
	}

	@Override
	public View onCreatePanelView(int featureId) {
		// TODO Auto-generated method stub
		return super.onCreatePanelView(featureId);
	}

	@Override
	public boolean onCreatePanelMenu(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreatePanelMenu(featureId, menu);
	}

	@Override
	public boolean onPreparePanel(int featureId, View view, Menu menu) {
		// TODO Auto-generated method stub
		return super.onPreparePanel(featureId, view, menu);
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		return super.onMenuOpened(featureId, menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onPanelClosed(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		super.onPanelClosed(featureId, menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onNavigateUp() {
		// TODO Auto-generated method stub
		return super.onNavigateUp();
	}

	@Override
	public boolean onNavigateUpFromChild(Activity child) {
		// TODO Auto-generated method stub
		return super.onNavigateUpFromChild(child);
	}

	@Override
	public void onCreateNavigateUpTaskStack(TaskStackBuilder builder) {
		// TODO Auto-generated method stub
		super.onCreateNavigateUpTaskStack(builder);
	}

	@Override
	public void onPrepareNavigateUpTaskStack(TaskStackBuilder builder) {
		// TODO Auto-generated method stub
		super.onPrepareNavigateUpTaskStack(builder);
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// TODO Auto-generated method stub
		super.onOptionsMenuClosed(menu);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onContextItemSelected(item);
	}

	@Override
	public void onContextMenuClosed(Menu menu) {
		// TODO Auto-generated method stub
		super.onContextMenuClosed(menu);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		return super.onCreateDialog(id);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		return super.onCreateDialog(id, args);
	}

	@Override
	@Deprecated
	protected void onPrepareDialog(int id, Dialog dialog) {
		// TODO Auto-generated method stub
		super.onPrepareDialog(id, dialog);
	}

	@Override
	@Deprecated
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		// TODO Auto-generated method stub
		super.onPrepareDialog(id, dialog, args);
	}

	@Override
	public boolean onSearchRequested() {
		// TODO Auto-generated method stub
		return super.onSearchRequested();
	}

	@Override
	protected void onApplyThemeResource(Theme theme, int resid, boolean first) {
		// TODO Auto-generated method stub
		super.onApplyThemeResource(theme, resid, first);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onActivityReenter(int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityReenter(resultCode, data);
	}

	@Override
	protected void onTitleChanged(CharSequence title, int color) {
		// TODO Auto-generated method stub
		super.onTitleChanged(title, color);
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		return super.onCreateView(name, context, attrs);
	}

	@Override
	public View onCreateView(View parent, String name, Context context,
			AttributeSet attrs) {
		// TODO Auto-generated method stub
		return super.onCreateView(parent, name, context, attrs);
	}

	@Override
	public void onVisibleBehindCanceled() {
		// TODO Auto-generated method stub
		super.onVisibleBehindCanceled();
	}

	@Override
	public void onEnterAnimationComplete() {
		// TODO Auto-generated method stub
		super.onEnterAnimationComplete();
	}

	@Override
	public ActionMode onWindowStartingActionMode(Callback callback) {
		// TODO Auto-generated method stub
		return super.onWindowStartingActionMode(callback);
	}

	@Override
	public void onActionModeStarted(ActionMode mode) {
		// TODO Auto-generated method stub
		super.onActionModeStarted(mode);
	}

	@Override
	public void onActionModeFinished(ActionMode mode) {
		// TODO Auto-generated method stub
		super.onActionModeFinished(mode);
	};

	
	
}
