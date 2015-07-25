package com.ha.halauncher.appmatrix;

import java.util.ArrayList;
import java.util.TreeSet;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.GridView;

import com.ha.halauncher.R;
import com.ha.halauncher.model.AppModel;

public class AppMatrixPagerAdapter extends PagerAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private int m_pages;
	static final int APPS_PERPAGE = 20;

	ArrayList<Application> allapps = new ArrayList<Application>();

	private TreeSet<Application> m_apps;
	private View mCurrentView;

	public View getmCurrentView() {
		return mCurrentView;
	}

	public AppMatrixPagerAdapter(Context context) {
		mContext = context;
		initilize();
	}

	private void initilize() {
		AppModel model = new AppModel(mContext);
		m_apps = model.getAllApps();
		m_pages = m_apps.size();
		allapps.addAll(m_apps);

	}

	@Override
	public int getCount() {
		return (m_pages / APPS_PERPAGE == 0) ? m_pages / APPS_PERPAGE : m_pages
				/ APPS_PERPAGE + 1;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		Log.e("Ashish", "instantiateItem " + position + " start");

		mInflater = LayoutInflater.from(mContext);

		ViewGroup rootView = (ViewGroup) mInflater.inflate(
				R.layout.app_matrix_page, container, false);

		GridView g = (GridView) rootView.findViewById(R.id.appmatrix);

		g.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				
				

				Log.e("Ashish", "OnTouch Grid View");
				// ((GridView)arg0).dispatchTouchEvent(arg1);
				
				if (arg1.getAction() == MotionEvent.ACTION_UP)
				{
					Log.e("Ashish", "Motion Event UP");
				}
				if (arg1.getAction() == MotionEvent.ACTION_MOVE)
				{
					Log.e("Ashish", "Motion Event MOVE");
				}

				return false;
			}
		});

		/*
		 * g.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * Log.e("Ashish","Grid View on Click Called");
		 * 
		 * } });
		 */

		/*
		 * rootView.setOnTouchListener(new OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) { // TODO
		 * Auto-generated method stub Log.e("Ashish",
		 * "OnTouch Layout linear View");
		 * 
		 * //GridView gv = (GridView) v;
		 * 
		 * return false; //return false; } });
		 */

		g.setAdapter(new AppMatrixAdapter(mContext, allapps, position));

		// new MatrixUpadateTask(new Page(g,position),"","").execute(new );

		container.addView(rootView);
		Log.e("Ashish", "instantiateItem " + position + " end");
		return rootView;
		// TODO Auto-generated method stub

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((ViewGroup) object);
	}
	
	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) { 
	if (mCurrentView != object) {
	mCurrentView = (View) object;
	}
	super.setPrimaryItem(container, position, object);
	}

}