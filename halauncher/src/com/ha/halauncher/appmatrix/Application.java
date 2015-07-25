package com.ha.halauncher.appmatrix;

import android.graphics.drawable.Drawable;

public class Application implements Comparable<Application>{
	
	private String m_displayName;
	private Drawable m_icon;
	private String m_packageName;
	
	public Application(String m_displayName, Drawable m_icon,
			String m_packageName) {
		super();
		this.m_displayName = m_displayName;
		this.m_icon = m_icon;
		this.m_packageName = m_packageName;
	}


	public Application() {
		// TODO Auto-generated constructor stub
	}

	
	public String getM_displayName() {
		return m_displayName;
	}
	public void setM_displayName(String m_displayName) {
		this.m_displayName = m_displayName;
	}
	public Drawable getM_icon() {
		return m_icon;
	}
	public void setM_icon(Drawable m_icon) {
		this.m_icon = m_icon;
	}
	public String getM_packageName() {
		return m_packageName;
	}
	public void setM_packageName(String m_packageName) {
		this.m_packageName = m_packageName;
	}
	
	@Override
	public int compareTo(Application obj) {
		// TODO Auto-generated method stub
		return this.m_displayName.toLowerCase().compareTo(obj.m_displayName.toLowerCase());
	}
	

}
