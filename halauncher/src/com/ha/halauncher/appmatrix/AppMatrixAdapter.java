package com.ha.halauncher.appmatrix;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ha.halauncher.R;

public class AppMatrixAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Application> m_apps;
	private int m_page_no;
	private LayoutInflater mInflater;
	private int m_apps_size;

	public AppMatrixAdapter(Context context, ArrayList<Application> apps,
			int pageno) {
		mContext = context;
		m_apps = apps;
		m_page_no = pageno;
		m_apps_size = apps.size();
	}

	public int getCount() {
		return AppMatrixPagerAdapter.APPS_PERPAGE;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		AppViewHolder appViewHolder;

		if (convertView == null) {
			
			mInflater = LayoutInflater.from(mContext);

			convertView = (ViewGroup) mInflater.inflate(
					R.layout.app_matrix_item, parent, false);

			appViewHolder = new AppViewHolder();
			appViewHolder.icon = (ImageView) convertView
					.findViewById(R.id.app_icon);
			appViewHolder.label = (TextView) convertView
					.findViewById(R.id.app_label);

			convertView.setTag(appViewHolder);

			

		} else {
			appViewHolder = (AppViewHolder) convertView.getTag();
		}

		
		if (m_apps_size > (position + AppMatrixPagerAdapter.APPS_PERPAGE
				* m_page_no)) {
			
			Application app = m_apps.get(position
					+ AppMatrixPagerAdapter.APPS_PERPAGE * m_page_no);
			appViewHolder.icon.setBackground(app.getM_icon());
			appViewHolder.label.setText(app.getM_displayName());
			convertView.setOnClickListener(new AppClickListener(app
					.getM_packageName()));
			convertView.setLongClickable(true);
		
			convertView.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stu
					Log.e("Ashish", "On Long Click item");
					return false;
				}
			});


		}

		return convertView;
	}

	private class AppViewHolder {
		ImageView icon;
		TextView label;
	}

	private class AppClickListener implements View.OnClickListener {

		private final String m_pkgname;

		public AppClickListener(String packagename) {
			// TODO Auto-generated constructor stub
			m_pkgname = packagename;
		}

		@Override
		public void onClick(View v) {

			Intent launchIntent = mContext.getPackageManager()
					.getLaunchIntentForPackage(m_pkgname);
			mContext.startActivity(launchIntent);
		}
	}
}
