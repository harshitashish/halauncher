package com.ha.halauncher.model;

import java.util.List;
import java.util.TreeSet;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.ha.halauncher.appmatrix.Application;

public class AppModel {

	private List<ApplicationInfo> m_allApps;
	private final PackageManager m_packageManager;
	private Context m_context;

	public AppModel(Context context) {
		this.m_context = context;
		m_packageManager = m_context.getPackageManager();
	}

	public TreeSet<Application> getAllApps() {

		TreeSet<Application> all_apps = new TreeSet<Application>();
		m_allApps = m_packageManager
				.getInstalledApplications(PackageManager.GET_META_DATA);

		for (ApplicationInfo app_info : m_allApps) {

			if (m_packageManager
					.getLaunchIntentForPackage(app_info.packageName) != null) {
				String label = app_info.loadLabel(m_packageManager).toString();

				if (label.matches("Google(.)*")) {
					label = googleAppLabel(label);
				}
				all_apps.add(new Application(label, app_info
						.loadIcon(m_packageManager), app_info.packageName
						.toString()));
			}

		}

		return all_apps;
	}

	private String googleAppLabel(String label) {
		switch (label) {
		case "Google Dialler":
			return "Phone";
		case "Google App":
			return "Google";
		case "Google+":
			return label;

		}

		return label.substring(7);
	}

}
