package com.ha.halauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AddShortcutReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.e("Harshita", "onReceive...");
		if(intent.getAction().equals("com.android.launcher.action.INSTALL_SHORTCUT")) {
			Log.e("Harshita", "received action to install..");
		} else if(intent.getAction().equals("com.android.launcher.action.UNINSTALL_SHORTCUT")) {
			Log.e("Harshita", "received action to Uninstall..");
		}
		
	}

}
